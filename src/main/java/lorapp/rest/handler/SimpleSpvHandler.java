package lorapp.rest.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lorapp.db.entity.UploadMessage;
import lorapp.db.supervision.enity.Alarm;
import lorapp.db.supervision.enity.SimpleSupervision;
import lorapp.db.supervision.repo.AlarmRepo;
import lorapp.rest.service.JacksonService;
import lorapp.rest.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.util.Map;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/16
 */
public class SimpleSpvHandler implements MessageListener {
    private Logger LOGGER = LoggerFactory.getLogger(SimpleSpvHandler.class);

    private JacksonService jacksonService = (JacksonService) SpringContextUtil.getBean("jacksonService");
    private AlarmRepo alarmRepo = (AlarmRepo) SpringContextUtil.getBean("alarmRepo");

    private SimpleSupervision simpleSpv;


    public SimpleSpvHandler(){}
    public SimpleSpvHandler(SimpleSupervision simpleSpv){
        this.simpleSpv = simpleSpv;
    }


    @Override
    public void onMessage(Message message) {
        String metricName = this.simpleSpv.getSpvdMetric();
        float metricThreshold = this.simpleSpv.getThreshold();
        String compartor = this.simpleSpv.getComparator();

        Map msgAsMap = jacksonService.toObject(new String(message.getBody()), Map.class);
        double metricVal = (double) msgAsMap.get(metricName);

        if(alarmShouldBeTriggered(metricVal, metricThreshold, compartor)){
            Alarm alarm = new Alarm(this.simpleSpv.getAppEui(), this.simpleSpv.getDevEui(),
                    new StringBuilder(metricName).append(" value:").append(metricVal)
                            .append(" ").append(compartor).append(" threshold:")
                            .append(metricThreshold).toString());
            alarmRepo.save(alarm);
            //trigger web socket
            AlarmHandlerCollection.invokeHandlers(alarm);
        }
    }

    private boolean alarmShouldBeTriggered(double metricVal, double metricThreshold, String compartor){
        if("=".equals(compartor)){
            return metricVal == metricThreshold;
        }else if(">=".equals(compartor)){
            return metricVal >= metricThreshold;
        }else if(">".equals(compartor)){
            return metricVal > metricThreshold;
        }else if("<=".equals(compartor)){
            return metricVal <= metricThreshold;
        }else if("<".equals(compartor)){
            return metricVal < metricThreshold;
        }
        return false;
    }
}
