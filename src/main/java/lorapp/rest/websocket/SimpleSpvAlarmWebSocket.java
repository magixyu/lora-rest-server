package lorapp.rest.websocket;

import lorapp.db.supervision.enity.Alarm;
import lorapp.rest.handler.AlarmHandler;
import lorapp.rest.handler.AlarmHandlerCollection;
import lorapp.rest.service.JacksonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value="/websocket/simpleSpvAlarm")
@Component
public class SimpleSpvAlarmWebSocket implements AlarmHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSpvAlarmWebSocket.class);

/*    @Autowired
    RealtimedataMQSubscribeService realtimedataMQSubscribeService;*/
    @Autowired
    JacksonService jacksonService;
    
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        AlarmHandlerCollection.getMsgHandlerSet().add(this);
    }

    @OnClose
    public void onClose(){
        AlarmHandlerCollection.getMsgHandlerSet().remove(this);
    }

    @OnMessage
    public void onMessage(String msg, Session session){
        //when received msg from client, do nothing
    }

    @OnError
    public void onError(Session session, Throwable error){
        LOGGER.error("occurred error:" + error.getMessage());
    }

    @Override
    public void handleAlarm(Alarm alarm) {
        try {
            this.session.getBasicRemote().sendText(jacksonService.toJsonString(alarm));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
