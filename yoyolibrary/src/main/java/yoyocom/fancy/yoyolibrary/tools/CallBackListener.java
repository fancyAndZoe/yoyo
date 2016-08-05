package yoyocom.fancy.yoyolibrary.tools;

/**
 * Created by fancy on 2016/2/16.
 */
public interface CallBackListener<E> {

    void onTaskPreExecute(int action);

    boolean onSuccess(ResponseResult<E> obj);

    void onError(ResponseResult<E> obj);

    void onCancel(int action);
}
