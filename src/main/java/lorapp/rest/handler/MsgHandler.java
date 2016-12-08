package lorapp.rest.handler;

import lorapp.db.entity.UploadMessage;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/8
 */
public interface MsgHandler {
    public void handleMsg(String msgInJsonStr, UploadMessage msgObj);
}
