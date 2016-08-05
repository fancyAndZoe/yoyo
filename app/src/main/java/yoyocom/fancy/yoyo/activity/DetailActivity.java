package yoyocom.fancy.yoyo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import yoyocom.fancy.yoyo.R;
import yoyocom.fancy.yoyo.headstone.BaseActivity;
import yoyocom.fancy.yoyo.model.Place;

/**
 * Created by fancy on 2016/1/27.
 */
public class DetailActivity extends BaseActivity {

    public static final String KEY_INDEX = "key_index";
    public static final String KEY_PLACE = "key_place";
    private ImageView img_detail;
    private Place place;
    private TextView txt_help;
    private FloatingActionButton fab_help;

    @Override
    protected void createView() {
        setContentView(R.layout.activity_detail);
        place = (Place) getIntent().getSerializableExtra(KEY_PLACE);
    }

    @Override
    protected String customTitle() {
        return place.name;
    }

    @Override
    protected void initView() {
        img_detail = (ImageView) findViewById(R.id.img_detail);
        txt_help = (TextView) findViewById(R.id.txt_help);
        fab_help = (FloatingActionButton) findViewById(R.id.fab_help);
    }

    @Override
    protected void initEvent() {
        fab_help.setOnClickListener(mClickListener);
    }

    @Override
    protected void initData() {
        if (place != null) {
            Picasso.with(DetailActivity.this).load(place.resId).into(img_detail);
        }
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId = v.getId();
            switch (resId) {
                case R.id.fab_help:
                    if (txt_help.getVisibility() == View.INVISIBLE) {
                        revealView(txt_help);
                    } else {
                        hiddenView(txt_help);
                    }
                    break;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void revealView(View view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void hiddenView(final View view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

}
