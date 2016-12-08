package lorapp.rest.handler;

import lorapp.db.entity.UploadMessage;

public interface MsgHandler {
    public void handleMsg(String msgInJsonStr, UploadMessage msgObj);
}
