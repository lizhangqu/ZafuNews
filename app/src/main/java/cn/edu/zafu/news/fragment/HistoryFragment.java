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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.adapter.HistoryAdapter;
import cn.edu.zafu.news.model.History;
import cn.edu.zafu.news.view.DividerItemDecoration;

public class HistoryFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private HistoryAdapter adapter;
    private List<History> list;
    public static HistoryFragment newInstance() {
        HistoryFragment f = new HistoryFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        initData();
        initRecyclerView(view);
        return view;
    }

    private void initData() {
        list=new ArrayList<History>();
        List<History> histories = new Select()
                .from(History.class).execute();
        list.addAll(histories);
    }

    private void initRecyclerView(View view) {
        adapter=new HistoryAdapter(list);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickLitener(new HistoryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(history!=null){
                    history.search(list.get(position).getTitle());
                }

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("您确定要删除该条搜索记录吗？")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                History item = list.get(position);
                                list.remove(item);
                                item.delete();
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(getActivity(), "删除搜索记录成功！", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }
    public interface OnHistory{
        void search(String content);
    }
    private OnHistory history;
    public void setOnHistory(OnHistory history){
        this.history=history;
    }

}