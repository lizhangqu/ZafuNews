package cn.edu.zafu.news.ui.collect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.corepage.core.CoreAnim;
import cn.edu.zafu.news.R;
import cn.edu.zafu.news.db.dao.BaseDao;
import cn.edu.zafu.news.db.helper.DatabaseHelper;
import cn.edu.zafu.news.db.model.NewsItem;
import cn.edu.zafu.news.ui.app.ToolbarFragment;
import cn.edu.zafu.news.ui.main.adapter.NewsAdapter;
import cn.edu.zafu.news.widget.DividerItemDecoration;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-19
 * Time: 22:48
 * FIXME
 */
public class CollectFragment extends ToolbarFragment {
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private List<NewsItem> list = new ArrayList<NewsItem>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_collect,container,false);
        showToolbar(view,"我的收藏");
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        BaseDao<NewsItem, Integer> newsItemDao = DatabaseHelper.getNewsItemDao(getActivity());
        try {
            List<NewsItem> items =   newsItemDao.queryAll();
            list.addAll(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //如果布局大小一致有利于优化
        mRecyclerView.setHasFixedSize(true);

        //使用线性布局管理器
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器并绑定适配器
        mAdapter = new NewsAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new NewsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //Intent intent = new Intent(CollectActivity.this, ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("news_item", list.get(position));
                //  intent.putExtras(bundle);
                //  startActivity(intent);

                openPage("newscontent",bundle, CoreAnim.slide);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("您确定要删除该条记录吗？")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NewsItem item = list.get(position);
                                list.remove(item);
                                BaseDao<NewsItem, Integer> newsItemDao = DatabaseHelper.getNewsItemDao(getActivity());
                                try {
                                    newsItemDao.delete(item);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                mAdapter.notifyItemRemoved(position);
                                Toast.makeText(getActivity(), "删除记录成功！", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }


}
