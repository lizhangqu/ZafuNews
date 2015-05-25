package cn.edu.zafu.news.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.adapter.NewsRecyclerViewAdapter;
import cn.edu.zafu.news.model.NewsItem;
import cn.edu.zafu.news.view.DividerItemDecoration;


public class CollectActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private List<NewsItem> list = new ArrayList<NewsItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        showToolbar("我的收藏");
        initData();
        initView();
    }

    private void initData() {
        List<NewsItem> items = new Select().from(NewsItem.class).execute();
        list.addAll(items);
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //如果布局大小一致有利于优化
        mRecyclerView.setHasFixedSize(true);

        //使用线性布局管理器
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器并绑定适配器
        mAdapter = new NewsRecyclerViewAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new NewsRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CollectActivity.this, ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("news_item", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(CollectActivity.this)
                        .setTitle("提示")
                        .setMessage("您确定要删除该条记录吗？")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NewsItem item = list.get(position);
                                list.remove(item);
                                item.delete();
                                mAdapter.notifyItemRemoved(position);
                                Toast.makeText(CollectActivity.this, "删除记录成功！", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

}
