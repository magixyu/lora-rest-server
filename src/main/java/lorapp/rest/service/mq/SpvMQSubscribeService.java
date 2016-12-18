package lorapp.rest.service.mq;

import lorapp.db.supervision.enity.SimpleSupervision;
import lorapp.db.supervision.repo.SimpleSupervisionRepo;
import lorapp.rest.handler.SimpleSpvHandler;
import lorapp.rest.util.ObjectCloneUtil;
import lorapp.rest.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/16
 */
@Component
@DependsOn(value = {"springContextUtil"})
public class SpvMQSubscribeService {
/*    @Autowired
    MQConfig mqConfig;
    @Autowired
    SimpleSupervisionRepo simpleSupervisionRepo;*/

    MQConfig mqConfig = (MQConfig) SpringContextUtil.getBean("mqConfig");
    SimpleSupervisionRepo simpleSupervisionRepo = (SimpleSupervisionRepo) SpringContextUtil.getBean("simpleSupervisionRepo");


    public SpvMQSubscribeService(){
        if(mqConfig.getQueueListenerMap().size() == 0){
            List<SimpleSupervision> simpleSpvList = simpleSupervisionRepo.findAllByEnabled(true);
            for(SimpleSupervision simpleSpv : simpleSpvList){
                newSubsription4SimpleSpv(simpleSpv);
            }
        }
    }


    public void newSubsription4SimpleSpv(SimpleSupervision spv) {
        SpringContextUtil.getBean("simpleSupervisionRepo");

        String queueName = getQueueName(spv);
        String bindKey = spv.getAppEui() + "." + spv.getDevEui();

        mqConfig.addNewQueueAndListener(queueName, mqConfig.getExch(), bindKey,
                new SimpleSpvHandler(ObjectCloneUtil.deepClone(spv)));
    }


    public void delSubscription4SimpleSpv(SimpleSupervision spv){
        String queueName = getQueueName(spv);
        mqConfig.delQueueAndListener(queueName);
    }


    public void updateSubsription4SimpleSpv(SimpleSupervision spv) {
        this.delSubscription4SimpleSpv(spv);
        this.newSubsription4SimpleSpv(spv);
    }


    private String getQueueName(SimpleSupervision spv){
        return new StringBuilder().append(spv.getId()).append("_").append(spv.getAppEui()).append("_").append(spv.getDevEui()).toString();
    }
}
