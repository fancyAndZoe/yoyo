package yoyocom.fancy.yoyo.headstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fancy on 2016/1/27.
 */
public abstract class BaseFragment extends Fragment {

    protected View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createView(inflater);
        initView();
        initEvent();
        initData();
        return contentView;
    }

    protected abstract void createView(LayoutInflater inflater);
    protected abstract void initView();
    protected abstract void initEvent();
    protected abstract void initData();

    protected View findViewById(int resId) {
        if (contentView == null) {
            return null;
        }
        return contentView.findViewById(resId);
    }
}
