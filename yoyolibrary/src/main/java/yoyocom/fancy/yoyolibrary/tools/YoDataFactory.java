package yoyocom.fancy.yoyolibrary.tools;

import android.content.Context;

import yoyocom.fancy.yoyolibrary.model.Post;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoDataFactory {

    /**
     * 添加用户
     */
    public static final int ACTION_ADD_USER = 0;

    /**
     * 编辑用户
     */
    public static final int ACTION_EDIT_USER = 1;

    /**
     * 删除用户
     */
    public static final int ACTION_DELETE_USER = 2;

    /**
     * 查询用户
     */
    public static final int ACTION_QUERY_USER = 3;

    /**
     * 发表动态
     */
    public static final int ACTION_ADD_POST = 4;

    /**
     * 查询所有动态
     */
    public static final int ACTION_QUERY_POST_ALL = 5;

    public static void addPost(YoTaskManager taskManager, Context context, Post post, ResponseHandler responseHandler) {
        taskManager.addOperation(new YoyoOperation(context, ACTION_ADD_POST, post, null, responseHandler)).execute();
    }

    public static void queryPost(YoTaskManager taskManager, Context context, ResponseHandler responseHandler) {
        taskManager.addOperation(new YoyoOperation(context, ACTION_QUERY_POST_ALL, null, null, responseHandler)).execute();
    }
}
