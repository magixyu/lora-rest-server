package lorapp.rest.controller;

import lorapp.db.entity.DevConfig;
import lorapp.db.entity.component.AppDevMap;
import lorapp.db.repo.DevConfigRepo;
import lorapp.rest.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author MoseC
 * @Desc
 * @Date 2017/1/5
 */
@RestController
@RequestMapping(value = "/deveui/")
public class DevEUIController {
    @Autowired
    DevConfigRepo devConfigRepo;

    @RequestMapping(value = "new", method = RequestMethod.PUT)
    public CommonResult add(@RequestBody DevConfig devConfig){
        CommonResult commRes = new CommonResult();

        if (devConfig != null){
            AppDevMap appDevMap = devConfig.getDevAppMap();
            if(appDevMap != null){
                String appEUI = appDevMap.getAppEUI();
                String devEUI = appDevMap.getDevEUI();
                if(appEUI != null && !"".equals(appEUI) && devEUI != null && !"".equals(devEUI)){
                    List<DevConfig> devList = devConfigRepo.findAllByDevAppMapDevEUI(devEUI);
                    if(devList != null && devList.size() > 0){
                        commRes.setErrorMsg("已存在DevEUI为"+devEUI+"的设备，不能重复添加。");
                        return commRes;
                    }
                    devConfigRepo.save(devConfig);
                    commRes.setSuccess(true);
                    return commRes;
                }
            }
        }
        commRes.setErrorMsg("要新增的设备appEUI和devEUI均不能为空。");
        return commRes;
    }

    @RequestMapping(value = "{devEUI}", method = RequestMethod.DELETE)
    public CommonResult delete(@PathVariable String devEUI){
        CommonResult commRes = new CommonResult();
        try {
            List<DevConfig> devList = devConfigRepo.findAllByDevAppMapDevEUI(devEUI);
            if (devList != null && devList.size() > 0) {
                devConfigRepo.delete(devList);
            }
            commRes.setSuccess(true);
        }catch(Exception e){
            commRes.setErrorMsg(e.getMessage());
        }finally {
            return commRes;
        }
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody DevConfig devConfig){
        CommonResult commRes = new CommonResult();

        if (devConfig != null) {
            long devId = devConfig.getId();
            AppDevMap appDevMap = devConfig.getDevAppMap();
            if (appDevMap != null) {
                String appEUI = appDevMap.getAppEUI();
                String devEUI = appDevMap.getDevEUI();
                if (appEUI != null && !"".equals(appEUI) && devEUI != null && !"".equals(devEUI)) {
                    if(devId == 0){
                        List<DevConfig> devList = devConfigRepo.findAllByDevAppMapDevEUI(devEUI);
                        if(devList != null && devList.size() > 0){
                            devConfig.setId(devList.get(0).getId());
                            devConfigRepo.save(devConfig);
                            commRes.setSuccess(true);
                            return commRes;
                        }
                    }else{
                        devConfigRepo.save(devConfig);
                        commRes.setSuccess(true);
                        return commRes;
                    }
                }
            }
        }
        commRes.setErrorMsg("要更新的设备的DevEUI和AppEUI均不能为空。");
        return commRes;
    }
}
