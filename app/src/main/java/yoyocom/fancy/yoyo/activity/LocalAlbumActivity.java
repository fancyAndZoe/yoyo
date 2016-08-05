package yoyocom.fancy.yoyo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yoyocom.fancy.yoyo.R;
import yoyocom.fancy.yoyo.YoyoApplication;
import yoyocom.fancy.yoyo.common.ExtraKey;
import yoyocom.fancy.yoyo.common.ImageUtils;
import yoyocom.fancy.yoyo.common.LocalImageHelper;
import yoyocom.fancy.yoyo.common.StringUtils;
import yoyocom.fancy.yoyo.headstone.BaseActivity;
import yoyocom.fancy.yoyo.widget.FzltTextView;

/**
 * Created by fancy on 2016/2/18.
 */
public class LocalAlbumActivity extends BaseActivity {
    private ListView local_album_list;
    private ImageView progress_bar;
    private LocalImageHelper helper;
    private List<String> folderNames;
    public static final int ACTION_GO_PICTURE_DETAIL = 2;
    public static final String KEY_SELECT_BACK = "key_select_back";
    private String cameraPath = "";

    @Override
    protected void createView() {
        setContentView(R.layout.activity_local_album);
    }

    @Override
    protected String customTitle() {
        return "本地相册";
    }

    @Override
    protected void initView() {
        local_album_list = (ListView) findViewById(R.id.local_album_list);
        progress_bar = (ImageView) findViewById(R.id.progress_bar);
        helper = LocalImageHelper.getInstance();
    }

    @Override
    protected void initEvent() {
        local_album_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LocalAlbumActivity.this, LocalPictureActivity.class);
                intent.putExtra(ExtraKey.LOCAL_FOLDER_NAME, folderNames.get(i));
//                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivityForResult(intent, ACTION_GO_PICTURE_DETAIL);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ACTION_GO_PICTURE_DETAIL:
                    if (data.getStringExtra(KEY_SELECT_BACK).equals("1")) {
                        finish();
                    }
                    break;

                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
//                    String cameraPath = LocalImageHelper.getInstance().getCameraImgPath();
                    if (StringUtils.isEmpty(cameraPath)) {
                        Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File file = new File(cameraPath);
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        LocalImageHelper.LocalFile localFile = new LocalImageHelper.LocalFile();
                        localFile.setThumbnailUri(uri.toString());
                        localFile.setOriginalUri(uri.toString());
                        localFile.setOrientation(getBitmapDegree(cameraPath));
                        localFile.setFilePath(cameraPath);
                        LocalImageHelper.getInstance().getCheckedItems().add(localFile);
                        LocalImageHelper.getInstance().setResultOk(true);
                        new  Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        //这里本来有个弹出progressDialog的，在拍照结束后关闭，但是要延迟1秒，原因是由于三星手机的相机会强制切换到横屏，
                        //此处必须等它切回竖屏了才能结束，否则会有异常
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },1000);
                    } else {
                        Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void initData() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        progress_bar.startAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启线程初始化本地图片列表，该方法是synchronized的，因此当AppContent在初始化时，此处阻塞
                LocalImageHelper.getInstance().initImage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //初始化完毕后，显示文件夹列表
                        if (!isDestroy) {
                            initAdapter();
                            progress_bar.clearAnimation();
                            ((View) progress_bar.getParent()).setVisibility(View.GONE);
                            local_album_list.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    public void initAdapter() {
        local_album_list.setAdapter(new FolderAdapter(this, helper.getFolderMap()));
    }

    /**
     * 读取图片的旋转的角度，还是三星的问题，需要根据图片的旋转角度正确显示
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class FolderAdapter extends BaseAdapter {
        Map<String, List<LocalImageHelper.LocalFile>> folders;
        Context context;
        DisplayImageOptions options;

        FolderAdapter(Context context, Map<String, List<LocalImageHelper.LocalFile>> folders) {
            this.folders = folders;
            this.context = context;
            folderNames = new ArrayList<>();

            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .showImageForEmptyUri(R.drawable.dangkr_no_picture_small)
                    .showImageOnFail(R.drawable.dangkr_no_picture_small)
                    .showImageOnLoading(R.drawable.dangkr_no_picture_small)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .setImageSize(new ImageSize(((YoyoApplication) context.getApplicationContext()).getQuarterWidth(), 0))
                    .displayer(new SimpleBitmapDisplayer()).build();

            Iterator iter = folders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                folderNames.add(key);
            }
            //根据文件夹内的图片数量降序显示
            Collections.sort(folderNames, new Comparator<String>() {
                public int compare(String arg0, String arg1) {
                    Integer num1 = helper.getFolder(arg0).size();
                    Integer num2 = helper.getFolder(arg1).size();
                    return num2.compareTo(num1);
                }
            });
        }

        @Override
        public int getCount() {
            return folders.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_albumfoler, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.textView = (FzltTextView) convertView.findViewById(R.id.textview);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String name = folderNames.get(i);
            List<LocalImageHelper.LocalFile> files = folders.get(name);
            viewHolder.textView.setText(name + "(" + files.size() + ")");
            if (files.size() > 0) {
                ImageLoader.getInstance().displayImage(files.get(0).getThumbnailUri(), new ImageViewAware(viewHolder.imageView), options,
                        null, null, files.get(0).getOrientation());
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            FzltTextView textView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_picture_camera, menu);

        MenuItem completeItem = menu.findItem(R.id.action_complete);
        completeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(LocalImageHelper.getInstance().getCurrentSize()+LocalImageHelper.getInstance().getCheckedItems().size()>=9){
                    Toast.makeText(LocalAlbumActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //  拍照后保存图片的绝对路径
                String status= Environment.getExternalStorageState();
                if(status.equals(Environment.MEDIA_MOUNTED))
                {
                    try {
                        File dir = new File(Environment.getExternalStorageDirectory() + "/yoyo");
                        if(!dir.exists()) {
                            dir.mkdirs();
                        }

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                                .format(new Date()) + ".jpg";
                        File file = new File(dir, timeStamp);
                        cameraPath = file.getPath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent,
                                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LocalAlbumActivity.this, "sd卡不可用", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
