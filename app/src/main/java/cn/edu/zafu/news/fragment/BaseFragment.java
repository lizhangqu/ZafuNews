/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.edu.zafu.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.activity.ContentActivity;
import cn.edu.zafu.news.adapter.NewsRecyclerViewAdapter;
import cn.edu.zafu.news.common.http.client.NewsOkHttpClient;
import cn.edu.zafu.news.common.parser.impl.NewsParser;
import cn.edu.zafu.news.model.Category;
import cn.edu.zafu.news.model.NewsItem;
import cn.edu.zafu.news.view.DividerItemDecoration;

public class BaseFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    private List<NewsItem> list = new ArrayList<NewsItem>();
    private static final String CATEGORY = "category";

    private Category category;
    private int page = 1;
    private int max = 0;
    private static final int REFRESH = 0x01;
    private static final int REFRESH_ERROR = 0x02;
    private static final int LOADMORE = 0x03;
    private static final int LOADMORE_ERROR = 0x04;
    private boolean isLoadingMore = false;
    private boolean isRefreshing=false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    swipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    isRefreshing=false;
                    break;
                case REFRESH_ERROR:

                    swipeRefreshLayout.setRefreshing(false);
                    isRefreshing=false;
                    Toast.makeText(getActivity(), "网络异常！", Toast.LENGTH_LONG).show();


                    break;
                case LOADMORE:
                    isLoadingMore = false;
                    mAdapter.notifyDataSetChanged();
                    break;
                case LOADMORE_ERROR:
                    Toast.makeText(getActivity(), "网络异常！", Toast.LENGTH_LONG).show();
                    isLoadingMore = false;

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    public static BaseFragment newInstance(Category category) {
        BaseFragment f = new BaseFragment();
        Bundle b = new Bundle();
        b.putSerializable(CATEGORY, category);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = (Category) getArguments().getSerializable(CATEGORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base, container, false);
        initSwipeRefreshLayout(view);
        refreshData();
        initRecyclerView(view);
        return view;
    }

    private void getMaxPage(String content) {
        try {
            Document document = Jsoup.parse(content);
            Elements select = document.select("span.step-links a");
            String temp = select.get(select.size() - 1).attr("href");
            temp = temp.substring(temp.indexOf("=") + 1);
            max = Integer.parseInt(temp);
        } catch (ArrayIndexOutOfBoundsException e) {
            max = 1;
        }

    }

    private void refreshData() {
        isRefreshing=true;
        page = 1;
        OkHttpClient client = NewsOkHttpClient.getInstance();
        final Request request = new Request.Builder()
                .url(category.getUrl() + page)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(REFRESH_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                NewsParser newsParser = new NewsParser();
                String str = response.body().string();
                if (max == 0) {
                    getMaxPage(str);
                }
                list.clear();
                list.addAll(newsParser.convert(str));
                handler.sendEmptyMessage(REFRESH);
            }
        });

    }

    private synchronized void loadMore() {
        isLoadingMore = true;
        if (page >= max) {
            Toast.makeText(getActivity(),"没有更多新闻了！",Toast.LENGTH_LONG).show();
            return;
        }
        page++;
        OkHttpClient client = NewsOkHttpClient.getInstance();
        final Request request = new Request.Builder()
                .url(category.getUrl() + page)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(LOADMORE_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                NewsParser newsParser = new NewsParser();
                list.addAll(newsParser.convert(response.body().string()));
                handler.sendEmptyMessage(LOADMORE);
            }
        });
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //如果布局大小一致有利于优化
        mRecyclerView.setHasFixedSize(true);

        //使用线性布局管理器
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        //初始化适配器并绑定适配器
        mAdapter = new NewsRecyclerViewAdapter(list);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 5 && dy > 0) {
                    if (!isLoadingMore) {
                        loadMore();
                    }
                }
            }
        });
        mAdapter.setOnItemClickLitener(new NewsRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("news_item", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }


    private void initSwipeRefreshLayout(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_layout_progress_1, R.color.swipe_refresh_layout_progress_2, R.color.swipe_refresh_layout_progress_3, R.color.swipe_refresh_layout_progress_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }


}