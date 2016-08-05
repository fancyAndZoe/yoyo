package yoyocom.fancy.yoyo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import yoyocom.fancy.yoyo.tool.FontCustom;

/**
 * Created by fancy on 2016/1/29.
 */
public class FzltTextView extends TextView {

    public FzltTextView(Context context) {
        this(context, null);
    }

    public FzltTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FzltTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
