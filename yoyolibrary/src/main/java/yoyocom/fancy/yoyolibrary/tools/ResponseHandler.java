package yoyocom.fancy.yoyolibrary.tools;

import android.os.Handler;
import android.os.Message;

/**
 * Created by fancy on 2016/2/16.
 */
public class ResponseHandler extends Handler {

    public static final int WHAT_SUCCESS = 0;

    public static final int WHAT_ERROR = 1;

    private OnCompleteListener onCompleteListener;

    public OnCompleteListener getOnCompleteListener () {
        return onCompleteListener;
    }

    public ResponseHandler(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public interface OnCompleteListener {
        void success(ResponseResult data);
        void error(ResponseResult data);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_SUCCESS:
                if (onCompleteListener != null) {
                    onCompleteListener.success((ResponseResult) msg.obj);
                }
                break;

            case WHAT_ERROR:
                if (onCompleteListener != null) {
                    onCompleteListener.error((ResponseResult) msg.obj);
                }
                break;
        }
    }
}
