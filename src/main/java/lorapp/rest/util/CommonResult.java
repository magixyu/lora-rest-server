package lorapp.rest.util;

import java.util.Map;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/11
 */
public class CommonResult {
    private boolean isSuccess;
    private String errorMsg;
    private Map<String, Object> responseData;


    public boolean isSuccess() {
        return isSuccess;
    }

    public CommonResult setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public CommonResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Map<String, Object> getResponseData() {
        return responseData;
    }

    public CommonResult setResponseData(Map<String, Object> responseData) {
        this.responseData = responseData;
        return this;
    }
}
