package yoyocom.fancy.yoyo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import yoyocom.fancy.yoyo.R;
import yoyocom.fancy.yoyo.common.ImageUtils;
import yoyocom.fancy.yoyo.common.LocalImageHelper;
import yoyocom.fancy.yoyo.headstone.BaseActivity;
import yoyocom.fancy.yoyo.widget.FilterImageView;
import yoyocom.fancy.yoyo.widget.FzltEditText;
import yoyocom.fancy.yoyo.widget.FzltTextView;
import yoyocom.fancy.yoyolibrary.model.Post;
import yoyocom.fancy.yoyolibrary.tools.ResponseHandler;
import yoyocom.fancy.yoyolibrary.tools.ResponseResult;
import yoyocom.fancy.yoyolibrary.tools.SharePreferenceUtil;
import yoyocom.fancy.yoyolibrary.tools.YoDataFactory;
import yoyocom.fancy.yoyolibrary.tools.YoTaskManager;

/**
 * Created by fancy on 2016/2/17.
 */
public class AddPostActivity extends BaseActivity {

    private FzltEditText edit_post_content;
    private FzltTextView post_text_remain, post_pic_remain;
    private FilterImageView filter_img_picture;
    int size;//小图大小
    int padding;//小图间距
    DisplayImageOptions options;
//    private ImageView img_camera;
    private List<LocalImageHelper.LocalFile> pictures = new ArrayList<>();//图片路径数组
    private LinearLayout post_pic_container;
    private HorizontalScrollView post_scrollview;
    private boolean isAdding = false;
    private Button btn_add;
    private int change = 1;

    @Override
    protected void createView() {
        setContentView(R.layout.activity_add_post);
    }

    @Override
    protected String customTitle() {
        return "添加动态";
    }

    @Override
    protected void initView() {
        edit_post_content = (FzltEditText) findViewById(R.id.edit_post_content);
        filter_img_picture = (FilterImageView) findViewById(R.id.filter_img_picture);
        post_text_remain = (FzltTextView) findViewById(R.id.post_text_remain);
        post_pic_remain = (FzltTextView) findViewById(R.id.post_pic_remain);
        post_pic_container = (LinearLayout) findViewById(R.id.post_pic_container);
        post_scrollview = (HorizontalScrollView) findViewById(R.id.post_scrollview);
        btn_add = (Button) findViewById(R.id.btn_add);
//        img_camera = (ImageView) findViewById(R.id.img_camera);
        //设置ImageLoader参数
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageForEmptyUri(R.drawable.dangkr_no_picture_small)
                .showImageOnFail(R.drawable.dangkr_no_picture_small)
                .showImageOnLoading(R.drawable.dangkr_no_picture_small)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        size = (int) getResources().getDimension(R.dimen.size_100);
        padding = (int) getResources().getDimension(R.dimen.padding_10);
    }

    @Override
    protected void initEvent() {
        filter_img_picture.setOnClickListener(mClickListener);
        edit_post_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable content) {
                post_text_remain.setText(content.toString().length() + "/800");
            }
        });

        btn_add.setOnClickListener(mClickListener);
//        img_camera.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.filter_img_picture:
                    Intent intent = new Intent(AddPostActivity.this, LocalAlbumActivity.class);
                    startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
                    break;

                case R.id.btn_add:
                    final String content = edit_post_content.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        showProgressDialog();
                        if (pictures != null && !pictures.isEmpty()) {
                            String[] filePaths = new String[pictures.size()];
                            for (int i = 0; i < pictures.size(); i ++) {
                                LocalImageHelper.LocalFile file = pictures.get(i);
                                filePaths[i] = getRealPathFromURI(Uri.parse(file.getThumbnailUri()));
                            }

                            BmobProFile.getInstance(AddPostActivity.this).uploadBatch(filePaths, new com.bmob.btp.callback.UploadBatchListener() {
                                @Override
                                public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                                    Log.i("uploadBatch", "onSuccess b = " + b + " , bmobFiles length = " + bmobFiles.length + " , strings length = " + strings.length);
                                    if (b) {
                                        Log.i("uploadBatch", "批量上传完毕 正式添加");
                                        for (int i = 0; i < bmobFiles.length; i ++) {
                                            Log.i("uploadBatch", bmobFiles[i].getUrl() + "\n" + " # " + bmobFiles[i].getFileUrl(AddPostActivity.this));
                                        }
                                        isAdding = true;
                                        Post post = new Post();
                                        post.setContent(content);
                                        post.setImages(bmobFiles);

                                        YoDataFactory.addPost(YoTaskManager.getInstance(3),
                                                AddPostActivity.this,
                                                post,
                                                new ResponseHandler(new ResponseHandler.OnCompleteListener() {
                                                    @Override
                                                    public void success(ResponseResult data) {
                                                        Log.i("uploadBatch", "发表动态成功!");
                                                        Toast.makeText(AddPostActivity.this, "发表动态成功!", Toast.LENGTH_SHORT).show();
                                                        AddPostActivity.this.finish();
                                                        pictures.clear();
                                                        isAdding = false;
                                                        closeProgressDialog();
                                                    }

                                                    @Override
                                                    public void error(ResponseResult data) {
                                                        pictures.clear();
                                                        isAdding = false;
                                                        closeProgressDialog();
                                                    }
                                                }));
                                    }

                                }

                                @Override
                                public void onProgress(int i, int i1, int i2, int i3) {
                                    Log.i("uploadBatch", "onProgress  i = " + i + " , i1 = " + i1 + " , i2 = " + i2 + " , i3 = " + i3);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.i("uploadBatch", "onError  i = " + i + " , s = " + s);
                                    showToast("上传图片失败 " + i + " , " + s);
                                }
                            });
                        } else {
                            Post post = new Post();
                            post.setContent(content);

                            YoDataFactory.addPost(YoTaskManager.getInstance(3),
                                    AddPostActivity.this,
                                    post,
                                    new ResponseHandler(new ResponseHandler.OnCompleteListener() {
                                        @Override
                                        public void success(ResponseResult data) {
                                            Toast.makeText(AddPostActivity.this, "发表动态成功!", Toast.LENGTH_SHORT).show();
                                            AddPostActivity.this.finish();
                                            closeProgressDialog();
                                        }

                                        @Override
                                        public void error(ResponseResult data) {
                                            closeProgressDialog();
                                        }
                                    }));
                        }

                    }
                    break;
            }
        }
    };

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                if (LocalImageHelper.getInstance().isResultOk()) {
                    LocalImageHelper.getInstance().setResultOk(false);
                    //获取选中的图片
                    List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
                    for (int i = 0; i < files.size(); i++) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                        params.rightMargin = padding;
                        FilterImageView imageView = new FilterImageView(this);
                        imageView.setLayoutParams(params);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        Picasso.with(AddPostActivity.this).load(files.get(i).getThumbnailUri()).into(imageView);
                        ImageLoader.getInstance().displayImage(files.get(i).getThumbnailUri(), new ImageViewAware(imageView), options,
                                null, null, files.get(i).getOrientation());
                        imageView.setOnClickListener(mClickListener);
                        pictures.add(files.get(i));
                        if (pictures.size() == 10) {
                            filter_img_picture.setVisibility(View.GONE);
                        } else {
                            filter_img_picture.setVisibility(View.VISIBLE);
                        }
                        post_pic_container.addView(imageView, post_pic_container.getChildCount() - 1);
                        post_pic_remain.setText(pictures.size() + "/9");
                        LocalImageHelper.getInstance().setCurrentSize(pictures.size());
                    }
                    //清空选中的图片
                    files.clear();
                    //设置当前选中的图片数量
                    LocalImageHelper.getInstance().setCurrentSize(pictures.size());
                    //延迟滑动至最右边
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            post_scrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 50L);
                }
                //清空选中的图片
                LocalImageHelper.getInstance().getCheckedItems().clear();
                break;
            default:
                break;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_post, menu);

        MenuItem completeItem = menu.findItem(R.id.action_complete);
        completeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final String content = edit_post_content.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    showProgressDialog();
                    if (pictures != null && !pictures.isEmpty()) {
                        String[] filePaths = new String[pictures.size()];
                        for (int i = 0; i < pictures.size(); i ++) {
                            LocalImageHelper.LocalFile file = pictures.get(i);
                            if (!TextUtils.isEmpty(file.getFilePath())) {
                                filePaths[i] = file.getFilePath();
                            } else {
                                filePaths[i] = getRealPathFromURI(Uri.parse(file.getThumbnailUri()));
                            }
                        }

                        BmobProFile.getInstance(AddPostActivity.this).uploadBatch(filePaths, new com.bmob.btp.callback.UploadBatchListener() {
                            @Override
                            public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                                Log.i("uploadBatch", "onSuccess b = " + b + " , bmobFiles length = " + bmobFiles.length + " , strings length = " + strings.length);
                                if (b) {
                                    Log.i("uploadBatch", "批量上传完毕 正式添加");
                                    for (int i = 0; i < bmobFiles.length; i ++) {
                                        Log.i("uploadBatch", bmobFiles[i].getUrl() + "\n" + " # " + bmobFiles[i].getFileUrl(AddPostActivity.this));
                                    }
                                    isAdding = true;
                                    Post post = new Post();
                                    post.setContent(content);
                                    post.setImages(bmobFiles);

                                    YoDataFactory.addPost(YoTaskManager.getInstance(3),
                                            AddPostActivity.this,
                                            post,
                                            new ResponseHandler(new ResponseHandler.OnCompleteListener() {
                                                @Override
                                                public void success(ResponseResult data) {
                                                    Log.i("uploadBatch", "发表动态成功!");
                                                    Toast.makeText(AddPostActivity.this, "发表动态成功!", Toast.LENGTH_SHORT).show();
                                                    AddPostActivity.this.finish();
                                                    pictures.clear();
                                                    isAdding = false;
                                                    closeProgressDialog();
                                                    SharePreferenceUtil.saveInt("shouldRefresh", 1, AddPostActivity.this);
                                                    LocalImageHelper.getInstance().getCheckedItems().clear();
                                                    LocalImageHelper.getInstance().setCurrentSize(0);
                                                }

                                                @Override
                                                public void error(ResponseResult data) {
                                                    pictures.clear();
                                                    isAdding = false;
                                                    closeProgressDialog();
                                                }
                                            }));
                                }

                            }

                            @Override
                            public void onProgress(int i, int i1, int i2, int i3) {
                                Log.i("uploadBatch", "onProgress  i = " + i + " , i1 = " + i1 + " , i2 = " + i2 + " , i3 = " + i3);
                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.i("uploadBatch", "onError  i = " + i + " , s = " + s);
                                showToast("上传图片失败 " + i + " , " + s);
                            }
                        });
                    } else {
                        Post post = new Post();
                        post.setContent(content);

                        YoDataFactory.addPost(YoTaskManager.getInstance(3),
                                AddPostActivity.this,
                                post,
                                new ResponseHandler(new ResponseHandler.OnCompleteListener() {
                                    @Override
                                    public void success(ResponseResult data) {
                                        Toast.makeText(AddPostActivity.this, "发表动态成功!", Toast.LENGTH_SHORT).show();
                                        AddPostActivity.this.finish();
                                        closeProgressDialog();
                                    }

                                    @Override
                                    public void error(ResponseResult data) {
                                        closeProgressDialog();
                                    }
                                }));
                    }

                }

                return true;
            }
        });

        return true;
    }

}
