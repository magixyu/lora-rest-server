package lorapp.rest.handler;

import lorapp.db.supervision.enity.Alarm;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class AlarmHandlerCollection{

    private static CopyOnWriteArraySet<AlarmHandler> msgHandlerSet = new CopyOnWriteArraySet<AlarmHandler>();

    public static void invokeHandlers(Alarm alarm){
        for(AlarmHandler alarmHandler : msgHandlerSet){
            alarmHandler.handleAlarm(alarm);
        }
    }


    public static CopyOnWriteArraySet<AlarmHandler> getMsgHandlerSet() {
        return msgHandlerSet;
    }
}