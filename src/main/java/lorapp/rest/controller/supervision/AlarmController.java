package lorapp.rest.controller.supervision;

import lorapp.db.supervision.repo.AlarmRepo;
import lorapp.rest.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/19
 */
@RestController
@RequestMapping(value = "/alarm")
public class AlarmController {
    @Autowired
    AlarmRepo alarmRepo;


    @RequestMapping(value = "/{alarmId}", method = RequestMethod.DELETE)
    public CommonResult deleteAlarm(@PathVariable String alarmId){
        CommonResult commRes = new CommonResult();
        try{
            alarmRepo.delete(Long.parseLong(alarmId));
        } catch(Exception e){
            commRes.setErrorMsg(e.getMessage());
        }
        commRes.setSuccess(true);
        return commRes;
    }
}
