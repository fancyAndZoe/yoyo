package yoyocom.fancy.yoyo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import yoyocom.fancy.yoyo.tool.FontCustom;

/**
 * Created by fancy on 2016/2/4.
 */
public class FzltEditText extends EditText {
    public FzltEditText(Context context) {
        super(context);
        init(context);
    }

    public FzltEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FzltEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /***
     * 设置字体
     *
     * @return
     */
    public void init(Context context) {
        setTypeface(FontCustom.setFont(context));
    }
}
