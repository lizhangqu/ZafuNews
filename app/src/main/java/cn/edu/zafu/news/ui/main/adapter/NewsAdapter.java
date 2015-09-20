package cn.edu.zafu.news.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.model.NewsItem;

/**
 * Created by lizhangqu on 2015/5/15.
 */
public class NewsAdapter extends
        RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    //数据集
    private List<NewsItem> list;
    private OnItemClickLitener mOnItemClickLitener;

    public NewsAdapter(List<NewsItem> list) {
        this.list = list;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        //绑定布局
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_news, parent,false);
        //创建ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        //绑定数据
        viewHolder.title.setText(list.get(position).getTitle());
        viewHolder.author.setText(list.get(position).getAuthor());
        viewHolder.time.setText(list.get(position).getTime());
        viewHolder.click.setText(list.get(position).getClick());
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView title;
        public  TextView time;
        public  TextView click;
        public  TextView author;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView.findViewById(R.id.title);
            time = (TextView) itemLayoutView.findViewById(R.id.time);
            click = (TextView) itemLayoutView.findViewById(R.id.click);
            author = (TextView) itemLayoutView.findViewById(R.id.author);
        }
    }
}
