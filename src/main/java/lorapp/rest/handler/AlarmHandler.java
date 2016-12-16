package lorapp.rest.handler;

import lorapp.db.supervision.enity.Alarm;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/16
 */
public interface AlarmHandler {
    public void handleAlarm(Alarm alarm);
}
