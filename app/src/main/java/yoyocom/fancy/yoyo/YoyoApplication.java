package yoyocom.fancy.yoyo;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

import cn.bmob.v3.Bmob;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import yoyocom.fancy.yoyo.common.LocalImageHelper;

/**
 * Created by fancy on 2016/1/28.
 */
public class YoyoApplication extends Application {

    public static final String BMOB_KEY = "d390eb43ae0c47b8e87ef53d04a00eda";
    //singleton
    private static YoyoApplication appContext = null;
    private Display display;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/fzltx.TTF")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        /**
         * 初始化bmob服务
         */
        Bmob.initialize(this, BMOB_KEY);

        appContext = this;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initImageLoader(getApplicationContext());
        //本地图片辅助类初始化
        LocalImageHelper.init(this);
        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
    }

    public static YoyoApplication getInstance() {
        return appContext;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(appContext, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的宽度
     */
    public int getWindowWidth() {
        return display.getWidth();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的高度
     */
    public int getWindowHeight() {
        return display.getHeight();
    }

    /**
     * @return
     * @Description： 获取当前屏幕一半宽度
     */
    public int getHalfWidth() {
        return display.getWidth() / 2;
    }

    /**
     * @return
     * @Description： 获取当前屏幕1/4宽度
     */
    public int getQuarterWidth() {
        return display.getWidth() / 4;
    }
}
