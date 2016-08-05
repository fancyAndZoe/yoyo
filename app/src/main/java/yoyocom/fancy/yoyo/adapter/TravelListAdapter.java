package yoyocom.fancy.yoyo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import yoyocom.fancy.yoyo.R;
import yoyocom.fancy.yoyo.YoyoApplication;
import yoyocom.fancy.yoyo.widget.CircleImageView;
import yoyocom.fancy.yoyo.widget.FzltTextView;
import yoyocom.fancy.yoyo.widget.MyGridView;
import yoyocom.fancy.yoyolibrary.model.Post;

/**
 * Created by fancy on 2016/1/27.
 */
public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.ViewHolder> {

   Context context;
   ArrayList<Post> posts = new ArrayList<Post>();
   OnItemClickListener mItemClickListener;
   OnCommentClickListener mCommentClickListener;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void refreshWith(ArrayList<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public TravelListAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_zoom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = posts.get(position);

        holder.txt_name.setText("user");
        holder.txt_content.setText(post.getContent());
        holder.txt_time.setText(post.getCreatedAt());
        Picasso.with(context).load(R.drawable.head_1).into(holder.img_head);
        BmobFile[] images = post.getImages();
        if (images != null && images.length > 0) {
            holder.gridView.setAdapter(new MyAdapter(context, images));
            holder.gridView.setVisibility(View.VISIBLE);
        } else {
            holder.gridView.setVisibility(View.GONE);
        }

        holder.post = post;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public FzltTextView txt_name;
        public FzltTextView txt_content;
        public CircleImageView img_head;
        public FzltTextView txt_comment;
        public FzltTextView txt_time;
        public LinearLayout linear_zoom;
        public Post post;
        public MyGridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (FzltTextView) itemView.findViewById(R.id.txt_name);
            txt_content = (FzltTextView) itemView.findViewById(R.id.txt_content);
            img_head = (CircleImageView) itemView.findViewById(R.id.img_head);
            txt_comment = (FzltTextView) itemView.findViewById(R.id.txt_comment);
            txt_time = (FzltTextView) itemView.findViewById(R.id.txt_time);
            linear_zoom = (LinearLayout) itemView.findViewById(R.id.linear_zoom);
            gridView = (MyGridView) itemView.findViewById(R.id.gridView);

            txt_comment.setOnClickListener(this);
            linear_zoom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.linear_zoom:
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemView, getPosition());
                    }
                    break;

                case R.id.txt_comment:
                    if (mCommentClickListener != null && post != null) {
                        mCommentClickListener.onCommentClick(post.getObjectId());
                    }
                    break;
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnCommentClickListener {
        void onCommentClick(String id);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setOnCommentClickListener(OnCommentClickListener mCommentClickListener) {
        this.mCommentClickListener = mCommentClickListener;
    }


        /*Bitmap photo = BitmapFactory.decodeResource(context.getResources(), place.resId);
        Palette.generateAsync(photo, new Palette.PaletteAsyncListener(){

            @Override
            public void onGenerated(Palette palette) {
                int bgColor = palette.getLightMutedColor(context.getResources().getColor(android.R.color.black));
                holder.placeNameHolder.setBackgroundColor(bgColor);
            }
        });*/

    public class MyAdapter extends BaseAdapter {
        private Context m_context;
        private LayoutInflater miInflater;
        DisplayImageOptions options;
        BmobFile[] bmobFiles;

        public MyAdapter(Context context, BmobFile[] bmobFiles) {
            m_context = context;
            this.bmobFiles = bmobFiles;
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
            return bmobFiles.length;
        }

        @Override
        public BmobFile getItem(int i) {
            return bmobFiles[i];
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
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.simple_list_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                viewHolder.checkBox.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = viewHolder.imageView;
            int width = YoyoApplication.getInstance().getWindowWidth() / 3;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, width);
            imageView.setLayoutParams(layoutParams);
            BmobFile localFile = bmobFiles[i];
            if (localFile != null) {
                ImageLoader.getInstance().displayImage(localFile.getUrl(), new ImageViewAware(viewHolder.imageView), options,
                        null, null, 0);
            }
//            FrescoLoader.getInstance().localDisplay(localFile.getThumbnailUri(), imageView, options);
//            Picasso.with(context).load(localFile.getUrl()).into(imageView);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showViewPager(i);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }
    }
}
