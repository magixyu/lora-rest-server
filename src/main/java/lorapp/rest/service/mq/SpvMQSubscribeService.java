package lorapp.rest.service.mq;

import lorapp.db.supervision.enity.SimpleSupervision;
import lorapp.db.supervision.repo.SimpleSupervisionRepo;
import lorapp.rest.handler.SimpleSpvHandler;
import lorapp.rest.util.ObjectCloneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/16
 */
@Component
public class SpvMQSubscribeService {
    @Value("${lora.rawdata.exchange}")
    private String exch;

    @Autowired
    MQConfig mqConfig;
    @Autowired
    SimpleSupervisionRepo simpleSpvRepo;


    public SpvMQSubscribeService(){
        if(mqConfig.getQueueListenerMap().size() == 0){
            List<SimpleSupervision> simpleSpvList = simpleSpvRepo.findAllByEnabled(true);
            for(SimpleSupervision simpleSpv : simpleSpvList){
                newSubsription4SimpleSpv(simpleSpv);
            }
        }
    }


    public void newSubsription4SimpleSpv(SimpleSupervision spv) {
        String queueName = getQueueName(spv);
        String bindKey = spv.getAppEUI() + "." + spv.getDevEUI();

        mqConfig.addNewQueueAndListener(queueName, exch, bindKey,
                new SimpleSpvHandler(ObjectCloneUtil.deepClone(spv)));
    }

    public void delSubscription4SimpleSPV(SimpleSupervision spv){
        String queueName = getQueueName(spv);
        mqConfig.delQueueAndListener(queueName);
    }

    public void updateSubsription4SimpleSpv(SimpleSupervision spv) {
        this.delSubscription4SimpleSPV(spv);
        this.newSubsription4SimpleSpv(spv);
    }


    private String getQueueName(SimpleSupervision spv){
        return new StringBuilder().append(spv.getId()).append("_").append(spv.getAppEUI()).append("_").append(spv.getDevEUI()).toString();
    }
}
