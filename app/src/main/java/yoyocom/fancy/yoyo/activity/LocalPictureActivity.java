package yoyocom.fancy.yoyo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import yoyocom.fancy.yoyo.R;
import yoyocom.fancy.yoyo.YoyoApplication;
import yoyocom.fancy.yoyo.common.ExtraKey;
import yoyocom.fancy.yoyo.common.LocalImageHelper;
import yoyocom.fancy.yoyo.headstone.BaseActivity;
import yoyocom.fancy.yoyo.widget.AlbumViewPager;
import yoyocom.fancy.yoyo.widget.FzltTextView;
import yoyocom.fancy.yoyo.widget.MatrixImageView;

/**
 * Created by fancy on 2016/2/19.
 */
public class LocalPictureActivity extends BaseActivity implements MatrixImageView.OnSingleTapListener,
        CompoundButton.OnCheckedChangeListener {
    private GridView gridview;
    private FrameLayout pagerview;
    private AlbumViewPager albumviewpager;
    private RelativeLayout album_item_header_bar;
    private ImageView header_bar_photo_back;
    private FzltTextView header_bar_photo_count, header_finish;
    private CheckBox checkbox;
    private LocalImageHelper helper = LocalImageHelper.getInstance();
    private List<LocalImageHelper.LocalFile> checkedItems ;
    private List<LocalImageHelper.LocalFile> currentFolder = null;
    private String folder = "";


    @Override
    protected void createView() {
        setContentView(R.layout.activity_local_picture);
        folder = getIntent().getExtras().getString(ExtraKey.LOCAL_FOLDER_NAME);
        if(!LocalImageHelper.getInstance().isInited()){
            finish();
            return;
        }
    }

    @Override
    protected String customTitle() {
        return folder;
    }

    @Override
    protected void initView() {
        gridview = (GridView) findViewById(R.id.gridview);
        pagerview = (FrameLayout) findViewById(R.id.pagerview);
        albumviewpager = (AlbumViewPager) findViewById(R.id.albumviewpager);
        album_item_header_bar = (RelativeLayout) findViewById(R.id.album_item_header_bar);
        header_bar_photo_back = (ImageView) findViewById(R.id.header_bar_photo_back);
        header_bar_photo_count = (FzltTextView) findViewById(R.id.header_bar_photo_count);
        header_finish = (FzltTextView) findViewById(R.id.header_finish);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
    }

    @Override
    protected void initEvent() {

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //防止停留在本界面时切换到桌面，导致应用被回收，图片数组被清空，在此处做一个初始化处理
                helper.initImage();
                //获取该文件夹下地所有文件
                final List<LocalImageHelper.LocalFile> folders = helper.getFolder(folder);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (folders != null) {
                            currentFolder = folders;
                            MyAdapter adapter = new MyAdapter(LocalPictureActivity.this, folders);
//                            title.setText(folder);
                            gridview.setAdapter(adapter);
                            //设置当前选中数量
                            if (checkedItems.size()+LocalImageHelper.getInstance().getCurrentSize() > 0) {
//                                finish.setText("完成(" + (checkedItems.size()+LocalImageHelper.getInstance().getCurrentSize()) + "/9)");
//                                finish.setEnabled(true);
//                                headerFinish.setText("完成(" + (checkedItems.size()+LocalImageHelper.getInstance().getCurrentSize()) + "/9)");
//                                headerFinish.setEnabled(true);
                            } else {
//                                finish.setText("完成");
////                                finish.setEnabled(false);
//                                headerFinish.setText("完成");
////                                headerFinish.setEnabled(false);
                            }
                        }
                    }
                });
            }
        }).start();
        checkedItems = helper.getCheckedItems();
        LocalImageHelper.getInstance().setResultOk(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            if (checkedItems.contains(compoundButton.getTag())) {
                checkedItems.remove(compoundButton.getTag());
            }
        } else {
            if (!checkedItems.contains(compoundButton.getTag())) {
                if(checkedItems.size()+LocalImageHelper.getInstance().getCurrentSize() > 9){
                    Toast.makeText(this,"最多选择9张图片",Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                    return;
                }
                checkedItems.add((LocalImageHelper.LocalFile) compoundButton.getTag());
            }
        }
        if (checkedItems.size()+ LocalImageHelper.getInstance().getCurrentSize()> 0) {
            setTitleOnToolBar(folder + " (" + (checkedItems.size()+LocalImageHelper.getInstance().getCurrentSize()) + "/9)");
//            finish.setText();
//            finish.setEnabled(true);
//            headerFinish.setText("完成(" +(checkedItems.size()+LocalImageHelper.getInstance().getCurrentSize()) + "/9)");
//            headerFinish.setEnabled(true);
        } else {
            setTitleOnToolBar(folder + " 完成");
//            finish.setEnabled(false);
//            headerFinish.setText("完成");
//            headerFinish.setEnabled(false);
        }
    }

    @Override
    public void onSingleTap() {

    }

    public class MyAdapter extends BaseAdapter {
        private Context m_context;
        private LayoutInflater miInflater;
        DisplayImageOptions options;
        List<LocalImageHelper.LocalFile> paths;

        public MyAdapter(Context context, List<LocalImageHelper.LocalFile> paths) {
            m_context = context;
            this.paths = paths;
            options=new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .showImageForEmptyUri(R.drawable.dangkr_no_picture_small)
                    .showImageOnFail(R.drawable.dangkr_no_picture_small)
                    .showImageOnLoading(R.drawable.dangkr_no_picture_small)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .setImageSize(new ImageSize(((YoyoApplication) context.getApplicationContext()).getQuarterWidth(), 0))
                    .displayer(new SimpleBitmapDisplayer()).build();
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public LocalImageHelper.LocalFile getItem(int i) {
            return paths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.simple_list_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                viewHolder.checkBox.setOnCheckedChangeListener(LocalPictureActivity.this);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = viewHolder.imageView;
            LocalImageHelper.LocalFile localFile = paths.get(i);
//            FrescoLoader.getInstance().localDisplay(localFile.getThumbnailUri(), imageView, options);
            ImageLoader.getInstance().displayImage(localFile.getThumbnailUri(), new ImageViewAware(viewHolder.imageView), options,
                    loadingListener, null, localFile.getOrientation());
            viewHolder.checkBox.setTag(localFile);
            viewHolder.checkBox.setChecked(checkedItems.contains(localFile));
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showViewPager(i);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }
    }

    private void showViewPager(int index) {
//        pagerContainer.setVisibility(View.VISIBLE);
//        gridView.setVisibility(View.GONE);
//        findViewById(R.id.album_title_bar).setVisibility(View.GONE);
//        viewpager.setAdapter(viewpager.new LocalViewPagerAdapter(currentFolder));
//        viewpager.setCurrentItem(index);
//        mCountView.setText((index + 1) + "/" + currentFolder.size());
//        //第一次载入第一张图时，需要手动修改
//        if(index==0){
//            checkBox.setTag(currentFolder.get(index));
//            checkBox.setChecked(checkedItems.contains(currentFolder.get(index)));
//        }
//        AnimationSet set = new AnimationSet(true);
//        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1, pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
//        scaleAnimation.setDuration(300);
//        set.addAnimation(scaleAnimation);
//        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
//        alphaAnimation.setDuration(200);
//        set.addAnimation(alphaAnimation);
//        pagerContainer.startAnimation(set);
    }

    SimpleImageLoadingListener loadingListener=new   SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, final Bitmap bm) {
            if (TextUtils.isEmpty(imageUri)) {
                return;
            }
            //由于很多图片是白色背景，在此处加一个#eeeeee的滤镜，防止checkbox看不清
            try {
                ((ImageView) view).getDrawable().setColorFilter(Color.argb(0xff, 0xee, 0xee, 0xee), PorterDuff.Mode.MULTIPLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_post, menu);

        MenuItem completeItem = menu.findItem(R.id.action_complete);
        completeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(LocalImageHelper.getInstance().getCurrentSize()+LocalImageHelper.getInstance().getCheckedItems().size()>9){
                    Toast.makeText(LocalPictureActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    return false;
                }

                LocalImageHelper.getInstance().setResultOk(true);
                Intent intent = new Intent();
                intent.putExtra(LocalAlbumActivity.KEY_SELECT_BACK, "1");
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
