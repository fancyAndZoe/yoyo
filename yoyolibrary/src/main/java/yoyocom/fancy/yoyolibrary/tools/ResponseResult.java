package yoyocom.fancy.yoyolibrary.tools;

import java.lang.reflect.Type;

/**
 * Created by db on 15-6-19.
 */
public class ResponseResult<E> {
    public static final int RESPONSE_STATE_NO_NETWORK = 100;
    public static final int RESPONSE_STATE_TIME_OUR = RESPONSE_STATE_NO_NETWORK+1;

    /**
     * 是否成功
     */
    public boolean success;
    public int state;
    public String message="";
    public E data;
    public int action;
    private Type type;

    public boolean isSuccess(){
        return success;
    }

    public int getState(){
        return state;
    }

    public String getMessage(){
        return message;
    }

    public E getData(){
        return data;
    }


    public int getAction(){
        return action;
    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }


    @Override
    public String toString() {
        return "ResponseResult{" +
                "success=" + success +
                ", state=" + state +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", action=" + action +
                '}';
    }
}
