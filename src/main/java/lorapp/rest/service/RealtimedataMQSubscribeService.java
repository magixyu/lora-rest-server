package lorapp.rest.service;

import lorapp.db.entity.UploadMessage;
import lorapp.rest.handler.MsgHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

@RabbitListener(queues = "rawdata")
@Component(value = "realtimedataMQSubscribeService")
public class RealtimedataMQSubscribeService {
    @Value("${lora.rawdata.exchange}")
    private String exch;

    private static CopyOnWriteArraySet<MsgHandler> msgHandlerSet = new CopyOnWriteArraySet<MsgHandler>();

    @Autowired(required = true)
    JacksonService jacksonService;

    @Bean
    public TopicExchange rawdataExchange(){
        return new TopicExchange(exch, true, false);
    }

    @Bean
    public Queue rawdataQueue(){
        return new Queue("rawdata", true);
    }

    @Bean
    public Binding rawdataExchangeBinding(TopicExchange rawdataExchange, Queue rawdataQueue){
        return BindingBuilder.bind(rawdataQueue).to(rawdataExchange).with("#.#");
    }

    @RabbitHandler
    public void onceMsgReceived(@Payload String msgStr){
        UploadMessage msgObj = jacksonService.toObject(msgStr, UploadMessage.class);
        for(MsgHandler msgHandler: msgHandlerSet){
            msgHandler.handleMsg(msgStr, msgObj);
        }
    }

    public static void registerMsgHandler(MsgHandler msgHandler){
        msgHandlerSet.add(msgHandler);
    }

    public static void unregisterMsgHandler(MsgHandler msgHandler){
        msgHandlerSet.remove(msgHandler);
    }
}
