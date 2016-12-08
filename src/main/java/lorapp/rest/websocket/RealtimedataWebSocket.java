package lorapp.rest.websocket;

import lorapp.db.entity.UploadMessage;
import lorapp.rest.handler.MsgHandler;
import lorapp.rest.service.JacksonService;
import lorapp.rest.service.RealtimedataMQSubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/7
 */
@ServerEndpoint(value="/websocket/realtimedata")
@Component
public class RealtimedataWebSocket implements MsgHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimedataWebSocket.class);

    private static int onlineCount = 0;
    private Session session;
    private static final String APP_EUI_PARAM = "appEui";
    private static final String DEV_EUI_PARAM = "devEui";
    private String appEuiTag;
    private String devEuiTag;


    @Autowired
    private JacksonService jacksonService;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        Map<String, List<String>> paramMap = session.getRequestParameterMap();
        String paramVal4AppEui = paramMap.get(APP_EUI_PARAM).get(0);
        String paramVal4DevEui = paramMap.get(DEV_EUI_PARAM).get(0);

        if(paramVal4AppEui == null || "".equals(paramVal4AppEui) || paramVal4DevEui == null || "".equals(paramVal4DevEui) ){
            LOGGER.error(session.getId() + ", neither the value of appEui nor the value of devEui can be empty");
            try {
                session.close();
            } catch (IOException e) {
                session = null;
            }
        }
        this.appEuiTag = paramVal4AppEui;
        this.devEuiTag = paramVal4DevEui;

        RealtimedataMQSubscribeService.registerMsgHandler(this);
        addOnlineCount();

        LOGGER.info("A new realtime data related web socket connection connected to server, current online realtime data web socket num is " + getOnlineCount());
    }

    @OnClose
    public void onClose(){
        RealtimedataMQSubscribeService.unregisterMsgHandler(this);
        subOnlineCount();

        LOGGER.info("A realtime data related web socket closed from server, current online realtime data web socket num is " + getOnlineCount());
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
    public void handleMsg(String msgInJsonStr, UploadMessage msgJsonObj) {
        String appEUI = msgJsonObj.getDevAppMap().getAppEUI();
        String devEUI = msgJsonObj.getDevAppMap().getDevEUI();

        if(!this.appEuiTag.equals(appEUI) || !this.devEuiTag.equals(devEUI)){
            return;
        }

        try {
            this.session.getBasicRemote().sendText(msgInJsonStr);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

}
