package yoyocom.fancy.yoyo.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import yoyocom.fancy.yoyo.R;


/**
 * Created by fancy on 2016/2/4.
 */
public class CommentView extends RelativeLayout{

    private FzltEditText edit_comment;
    private FzltTextView txt_post;
    private OnPostClickListener onPostClickListener;

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    public FzltEditText getEditComment() {
        return edit_comment;
    }

    public FzltTextView getTxtPost() {
        return txt_post;
    }

    public CommentView(Context context) {
        super(context);
        init(context);
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_comment_view, this);
        edit_comment = (FzltEditText) findViewById(R.id.edit_comment);
        txt_post = (FzltTextView) findViewById(R.id.txt_post);

        txt_post.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    if (edit_comment != null && !TextUtils.isEmpty(edit_comment.getText().toString().trim())) {
                        onPostClickListener.postClick(edit_comment.getText().toString().trim());
                    } else {
                        onPostClickListener.inputEmpty();
                    }
                }
            }
        });
    }

    interface OnPostClickListener {
        void postClick(String comment);
        void inputEmpty();
    }
}
