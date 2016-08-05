package yoyocom.fancy.yoyo.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.List;

import yoyocom.fancy.yoyo.R;
import yoyocom.fancy.yoyo.activity.AddPostActivity;
import yoyocom.fancy.yoyo.adapter.TravelListAdapter;
import yoyocom.fancy.yoyo.headstone.BaseFragment;
import yoyocom.fancy.yoyo.widget.CommentView;
import yoyocom.fancy.yoyolibrary.model.Post;
import yoyocom.fancy.yoyolibrary.tools.ResponseHandler;
import yoyocom.fancy.yoyolibrary.tools.ResponseResult;
import yoyocom.fancy.yoyolibrary.tools.SharePreferenceUtil;
import yoyocom.fancy.yoyolibrary.tools.YoDataFactory;
import yoyocom.fancy.yoyolibrary.tools.YoTaskManager;

/**
 * Created by fancy on 2016/1/27.
 */
public class HomeFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TravelListAdapter travelListAdapter;
    private RecyclerViewMaterialAdapter mAdapter;
    private static final int ITEM_COUNT = 100;
    private CommentView commentView;

    private List<Object> mContentItems = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ProgressBar progressBar;
    private FloatingActionButton fab_add;

    @Override
    protected void createView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        commentView = (CommentView) findViewById(R.id.commentView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
    }

    @Override
    protected void initEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        fab_add.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_add:
                    Intent intent = new Intent(getActivity(), AddPostActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        if (getActivity() == null || mRecyclerView == null) {
            return;
        }

        travelListAdapter = new TravelListAdapter(getActivity(), posts);
        travelListAdapter.setOnCommentClickListener(new TravelListAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClick(String id) {
                if (commentView != null) {
                    if (commentView.getVisibility() == View.GONE) {
                        commentView.setVisibility(View.VISIBLE);

                        commentView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                commentView.requestFocus();
                                commentView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                                commentView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                            }
                        }, 500);
                    } else {
                        commentView.setVisibility(View.GONE);
                    }
                }
            }
        });

        travelListAdapter.setOnItemClickListener(new TravelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (commentView != null) {
                    if (commentView.getVisibility() == View.VISIBLE) {
                        commentView.setVisibility(View.GONE);
                    }
                }
            }
        });
        mAdapter = new RecyclerViewMaterialAdapter(travelListAdapter);
        mRecyclerView.setAdapter(mAdapter);

        refreshZoom();

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharePreferenceUtil.getIntValue("shouldRefresh", getActivity()) == 1) {
            refreshZoom();
            SharePreferenceUtil.saveInt("shouldRefresh", 0, getActivity());
        }
    }

    private void refreshZoom() {
        YoDataFactory.queryPost(YoTaskManager.getInstance(1), getActivity(),
                new ResponseHandler(new ResponseHandler.OnCompleteListener() {
                    @Override
                    public void success(ResponseResult data) {
                        ArrayList<Post> posts = (ArrayList<Post>) data.data;
                        travelListAdapter.setPosts(posts);
                        mAdapter.mvp_notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void error(ResponseResult data) {
                        Toast.makeText(getActivity(), "获取动态失败", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }));
    }
}
