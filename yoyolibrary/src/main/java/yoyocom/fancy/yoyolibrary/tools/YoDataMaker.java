package yoyocom.fancy.yoyolibrary.tools;

import android.content.Context;
import android.os.Message;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import yoyocom.fancy.yoyolibrary.model.Post;
import yoyocom.fancy.yoyolibrary.model.YoyoUser;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoDataMaker {


    public static ResponseResult makeDataFromAction(Context context, int action, Object params,
        CallBackListener callBackListener, final ResponseHandler responseHandler) {
        final ResponseResult result = new ResponseResult();
        switch (action) {
            case YoDataFactory.ACTION_ADD_USER:
                YoyoUser yoyoUser = (YoyoUser) params;
                yoyoUser.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        result.success = true;
                        Message message = new Message();
                        message.what = ResponseHandler.WHAT_SUCCESS;
                        message.obj = result;
                        if (responseHandler != null) {
                            responseHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        result.success = false;
                        result.message = s;
                        Message message = new Message();
                        message.what = ResponseHandler.WHAT_ERROR;
                        message.obj = result;
                        if (responseHandler != null) {
                            responseHandler.sendMessage(message);
                        }
                    }
                });
                break;


            case YoDataFactory.ACTION_ADD_POST:
                Post post = (Post) params;
                post.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        result.success = true;
                        Message message = new Message();
                        message.what = ResponseHandler.WHAT_SUCCESS;
                        message.obj = result;
                        if (responseHandler != null) {
                            responseHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        result.success = false;
                        result.message = s;
                        Message message = new Message();
                        message.what = ResponseHandler.WHAT_ERROR;
                        message.obj = result;
                        if (responseHandler != null) {
                            responseHandler.sendMessage(message);
                        }
                    }
                });
                break;

            case YoDataFactory.ACTION_QUERY_POST_ALL:
                BmobQuery<Post> queryPost = new BmobQuery<Post>();
                queryPost.setLimit(20);
                queryPost.order("-createdAt");
                queryPost.findObjects(context, new FindListener<Post>() {
                    @Override
                    public void onSuccess(List<Post> list) {
                        result.success = true;
                        result.data = list;
                        Message message = new Message();
                        message.what = ResponseHandler.WHAT_SUCCESS;
                        message.obj = result;
                        if (responseHandler != null) {
                            responseHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
                break;
            }

        return result;
    }

    /**
     * 添加用户啊
     */

}
