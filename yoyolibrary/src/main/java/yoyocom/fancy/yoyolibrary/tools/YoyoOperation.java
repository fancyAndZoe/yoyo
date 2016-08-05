package yoyocom.fancy.yoyolibrary.tools;

import android.content.Context;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoyoOperation implements Operation {

    private Context context;
    private int action;
    private Object parameter;
    private CallBackListener callBackListener;
    private ResponseHandler responseHandler;

    public YoyoOperation(Context context, int action, Object parameter, CallBackListener callBackListener,
        ResponseHandler responseHandler) {
        this.context = context;
        this.action = action;
        this.parameter = parameter;
        this.callBackListener = callBackListener;
        this.responseHandler = responseHandler;
    }

    @Override
    public Operation setParams(Object... params) {
        this.parameter = params;
        return this;
    }

    @Override
    public Object[] getParams() {
        return new Object[0];
    }

    @Override
    public Object onBackgroundTask(Object... params) {
        ResponseResult result = YoDataMaker.makeDataFromAction(context, action, parameter, callBackListener, responseHandler);
        return result;
    }

    @Override
    public boolean onPostExecute(ResponseResult obj) {
        if (callBackListener != null) {
            boolean result = callBackListener.onSuccess(obj);
            return result;
        } else {
            return true;
        }
    }

    @Override
    public void onTaskCancel() {

    }

    @Override
    public void onTaskPreExecute() {

    }
}
