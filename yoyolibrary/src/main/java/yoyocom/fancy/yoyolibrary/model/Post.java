package yoyocom.fancy.yoyolibrary.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by fancy on 2016/2/16.
 */
public class Post extends BmobObject {
    /**
     * 状态内容
     */
    private String content;
    /**
     * 发布者
     */
    private YoyoUser author;
    /**
     * 状态图片
     */
    private BmobFile[] images;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public YoyoUser getAuthor() {
        return author;
    }

    public void setAuthor(YoyoUser author) {
        this.author = author;
    }

    public BmobFile[] getImages() {
        return images;
    }

    public void setImages(BmobFile[] images) {
        this.images = images;
    }
}
