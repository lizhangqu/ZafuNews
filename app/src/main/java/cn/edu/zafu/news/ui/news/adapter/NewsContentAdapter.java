package cn.edu.zafu.news.ui.news.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.model.NewsContent;

/**
 * Created by lizhangqu on 2015/5/15.
 */
public class NewsContentAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String INDENT="        ";
    //数据集
    private List<NewsContent> list;
    private Context context;
    public NewsContentAdapter(Context context,List<NewsContent> list) {
        this.context=context;
        this.list = list;
    }

    /**
     * 将dip的值转化为px值
     *
     * @param context  上下文
     * @param dipValue dip的值
     * @return px的值
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        View itemLayoutView=null;
        switch (viewType){
            case NewsContent.TYPE_TITLE:
                //绑定布局
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_content_title, parent,false);
                //创建ViewHolder
                viewHolder= new TextViewHolder(itemLayoutView);
                break;
            case NewsContent.TYPE_INFO:
                //绑定布局
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_content_title, parent,false);
                //创建ViewHolder
                viewHolder= new TextViewHolder(itemLayoutView);
                break;
            case NewsContent.TYPE_IMAGE:
                //绑定布局
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_content_img, parent,false);
                //创建ViewHolder
                viewHolder= new ImgViewHolder(itemLayoutView);
                break;
            case NewsContent.TYPE_PARAM:
                //绑定布局
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_content_param, parent,false);
                //创建ViewHolder
                viewHolder= new TextViewHolder(itemLayoutView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (list.get(position).getType()){
            case NewsContent.TYPE_TITLE:
                TextView title=((TextViewHolder)viewHolder).content;
                title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                title.setText(Html.fromHtml(list.get(position).getContent()));
                break;
            case NewsContent.TYPE_INFO:
                TextView info=((TextViewHolder)viewHolder).content;
                info.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                info.setText(Html.fromHtml(list.get(position).getContent()));
                break;
            case NewsContent.TYPE_IMAGE:
                ImageView imageView=((ImgViewHolder) viewHolder).img;

                Picasso.with(context)
                        .load(list.get(position).getContent())
                        .placeholder(R.mipmap.icon_image_default)
                        .resize(dip2px(viewHolder.itemView.getContext(),240), dip2px(viewHolder.itemView.getContext(),180))
                        .centerCrop()
                        .into(imageView);
                break;
            case NewsContent.TYPE_PARAM:
                TextView param=((TextViewHolder)viewHolder).content;
                param.setText(INDENT+Html.fromHtml(list.get(position).getContent()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

     static class TextViewHolder extends RecyclerView.ViewHolder {
        public  TextView content;

        public TextViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            content = (TextView) itemLayoutView.findViewById(R.id.content);

        }
    }
    static class ImgViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public ImgViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            img = (ImageView) itemLayoutView.findViewById(R.id.img);


        }
    }
}
