package yoyocom.fancy.yoyo.headstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import yoyocom.fancy.yoyo.R;

/**
 * Created by fancy on 2016/1/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar myToolbar;
    //应用是否销毁标志
    protected boolean isDestroy;
    protected ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(true);
        initView();
        initEvent();

        if (myToolbar != null) {
            setSupportActionBar(myToolbar);

            myToolbar.setNavigationIcon(getNavigationIcon());
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationClick();
                }
            });
            if (!TextUtils.isEmpty(customTitle())) {
                getSupportActionBar().setTitle(customTitle());
            }
        }
        initData();
        isDestroy = false;
    }

    protected void setTitleOnToolBar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected abstract void createView();
    protected abstract String customTitle();
    protected abstract void initView();
    protected abstract void initEvent();
    protected abstract void initData();

    /**
     * 修改toolbar icon
     * @return
     */
    protected int getNavigationIcon() {
        return R.drawable.ic_back;
    }

    /**
     * 修改点击toolbar icon 后的点击事件
     */
    protected void onNavigationClick() {
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }

    protected void showToast(String toastString) {
        Toast.makeText(BaseActivity.this, toastString, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
