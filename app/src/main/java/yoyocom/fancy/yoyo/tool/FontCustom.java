package yoyocom.fancy.yoyo.tool;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by fancy on 2016/1/29.
 */
public class FontCustom {
    static String fongUrl = "fonts/fzltx.TTF";
    static Typeface tf;

    /***
     * 设置字体
     *
     * @return
     */
    public static Typeface setFont(Context context) {
        if(tf==null){
            tf = Typeface.createFromAsset(context.getAssets(), fongUrl);
        }
        return tf;
    }
}
