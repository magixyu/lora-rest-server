package lorapp.rest.controller.supervision;

import com.fasterxml.jackson.core.JsonProcessingException;
import lorapp.db.supervision.enity.SimpleSupervision;
import lorapp.db.supervision.repo.SimpleSupervisionRepo;
import lorapp.rest.service.JacksonService;
import lorapp.rest.service.mq.SpvMQSubscribeService;
import lorapp.rest.util.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/14
 */
@RestController
@RequestMapping(value = "/simpleSpv")
public class SimpleSpvController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleSpvController.class);

    @Autowired
    SimpleSupervisionRepo simpleSupervisionRepo;
    @Autowired
    JacksonService jacksonService;
    @Autowired
    SpvMQSubscribeService spvMQSubscribeService;


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResult getAllSimpleSpvs(){
        CommonResult commRes = new CommonResult();
        Iterable<SimpleSupervision> simpleSpvList = simpleSupervisionRepo.findAll();
        return commRes.setSuccess(true).setResponseData(simpleSpvList);
    }

    @RequestMapping(value = "/enabledSimpleSpvs", method = RequestMethod.GET)
    public CommonResult getSimpleSpvsByEnabled(@RequestParam boolean isEnabled){
        CommonResult commRes = new CommonResult();
        List<SimpleSupervision> simpleSpvList = simpleSupervisionRepo.findAllByEnabled(isEnabled);
        return commRes.setSuccess(true).setResponseData(simpleSpvList);
    }

    @RequestMapping(value = "/creation", method = RequestMethod.PUT)
    public CommonResult addSimpleSpv(@RequestBody SimpleSupervision simpleSupervision){
        CommonResult commRes = new CommonResult();

        String errorMsg = validateBean(simpleSupervision);
        if(errorMsg != null){
            commRes.setErrorMsg(errorMsg);
            return commRes;
        }
        simpleSupervisionRepo.save(simpleSupervision);
        spvMQSubscribeService.newSubsription4SimpleSpv(simpleSupervision);
        commRes.setSuccess(true);
        return commRes;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateSimpleSpv(@RequestBody SimpleSupervision simpleSupervision){
        CommonResult commRes = new CommonResult();

        String errorMsg = validateBean(simpleSupervision);
        if(errorMsg == null){
            commRes.setErrorMsg(errorMsg);
            return commRes;
        }
        Long simpleSpvId = simpleSupervision.getId();
        if(simpleSpvId == null || simpleSpvId == 0){
            try {
                LOGGER.error("要更新SimpleSupervision的id不能为空：" + jacksonService.toJsonString(simpleSupervision));
                commRes.setErrorMsg("不存在要更新的监控项。");
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }
            return commRes;
        }
        simpleSupervisionRepo.save(simpleSupervision);
        spvMQSubscribeService.updateSubsription4SimpleSpv(simpleSupervision);
        commRes.setSuccess(true);
        return commRes;
    }

    @RequestMapping(value = "/remove/{simpleSpvIdStr}", method = RequestMethod.DELETE)
    public CommonResult deleteSimpleSpv(@PathVariable String simpleSpvIdStr){
        CommonResult commRes = new CommonResult();

        long simpleSpvId;
        try{
            simpleSpvId = (long)Integer.parseInt(simpleSpvIdStr);
        }catch (Exception e) {
            LOGGER.error("要删除的监控项id必须为整数，id：" + simpleSpvIdStr);
            commRes.setErrorMsg("要删除的监控项id必须为整数，id：" + simpleSpvIdStr);
            return commRes;
        }
        SimpleSupervision simpleSupervision = simpleSupervisionRepo.findOne(simpleSpvId);
        simpleSupervisionRepo.delete(simpleSpvId);
        spvMQSubscribeService.delSubscription4SimpleSpv(simpleSupervision);
        commRes.setSuccess(true);
        return commRes;
    }


    private String validateBean(SimpleSupervision simpleSupervision){
        String appEUI = simpleSupervision.getAppEui();
        String spvdMetric = simpleSupervision.getSpvdMetric();
        String comparator = simpleSupervision.getComparator();
        if(appEUI == null || "".equals(appEUI)
                || spvdMetric == null || "".equals(spvdMetric)
                || comparator == null || "".equals(comparator)){
            String errorMsg = "appEUI，spvdMetric，comparator中任何一个都不能为空。";
            return errorMsg;
        }
        return null;
    }

}
