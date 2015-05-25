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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
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
import cn.edu.zafu.news.adapter.SearchAdapter;
import cn.edu.zafu.news.common.http.client.NewsOkHttpClient;
import cn.edu.zafu.news.common.parser.impl.SearchParser;
import cn.edu.zafu.news.model.NewsItem;
import cn.edu.zafu.news.model.SearchItem;
import cn.edu.zafu.news.view.DividerItemDecoration;

public class SearchFragment extends Fragment {
    private static String searchURL = "http://news.zafu.edu.cn/search/?keyword=KEYWORD&page=PAGE";
    private int page = 1;
    private int max=0;
    private View view;
    private LinearLayout ll;
    private RecyclerView mRecyclerView;
    private SearchAdapter adapter;
    private List<SearchItem> list;
    private CircleProgressBar circleProgressBar;
    private static final int SEARCH=0x01;
    private static final String SEARCH_CONTENT="search";
    private String searchContent;
    private boolean isLoadingMore = false;
    private static final int LOADMORE = 0x03;
    private static final int LOADMORE_ERROR = 0x04;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH:
                    circleProgressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    if(list.size()==0){
                        ll.setVisibility(View.VISIBLE);
                    }
                    break;
                case LOADMORE:
                    isLoadingMore = false;
                    adapter.notifyDataSetChanged();
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
    public static SearchFragment newInstance(String str) {
        SearchFragment f = new SearchFragment();
        Bundle b = new Bundle();
        b.putString(SEARCH_CONTENT, str);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchContent = getArguments().getString(SEARCH_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initRecyclerView(view);
        initData();
        return view;
    }

    private void initData() {
        search(searchContent);
    }

    private void initRecyclerView(View view) {
        list=new ArrayList<SearchItem>();
        circleProgressBar= (CircleProgressBar) view.findViewById(R.id.progressBar);
        ll= (LinearLayout) view.findViewById(R.id.ll);
        adapter=new SearchAdapter(list);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.result);
        mRecyclerView.setAdapter(adapter);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickLitener(new SearchAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                Bundle bundle = new Bundle();
                NewsItem item = new NewsItem();
                item.setAuthor("");
                item.setClick(list.get(position).getCategory());
                item.setTime(list.get(position).getTime());
                item.setUrl(list.get(position).getUrl());
                item.setTitle(list.get(position).getTitle());
                bundle.putSerializable("news_item", item);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
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
        Log.d("TAG", "max page:" + max);

    }
    private void search(String str){
        ll.setVisibility(View.GONE);
        page=1;
        max=0;
        OkHttpClient client = NewsOkHttpClient.getInstance();
        final Request request = new Request.Builder()
                .url(searchURL.replace("KEYWORD", str).replace("PAGE", page + ""))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(LOADMORE_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                if (max == 0) {
                    getMaxPage(str);
                }
                SearchParser searchParser = new SearchParser();
                List<SearchItem> items = searchParser.convert(str);
                list.clear();
                list.addAll(items);
                handler.sendEmptyMessage(SEARCH);
            }
        });
    }
    private synchronized void loadMore() {
        isLoadingMore = true;
        if (page >= max) {
            Toast.makeText(getActivity(), "没有更多新闻了！", Toast.LENGTH_LONG).show();
            return;
        }
        page++;
        OkHttpClient client = NewsOkHttpClient.getInstance();
        final Request request = new Request.Builder()
                .url(searchURL.replace("KEYWORD", searchContent).replace("PAGE", page + ""))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(LOADMORE_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                SearchParser searchParser = new SearchParser();
                list.addAll(searchParser.convert(response.body().string()));
                handler.sendEmptyMessage(LOADMORE);
            }
        });
    }


}