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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.UpdateLess;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.activity.AboutActivity;
import cn.edu.zafu.news.activity.CollectActivity;
import cn.edu.zafu.news.common.http.client.NewsOkHttpClient;
import cn.edu.zafu.news.common.parser.impl.UpdateParser;
import cn.edu.zafu.news.model.History;
import cn.edu.zafu.news.model.NewsItem;
import cn.edu.zafu.news.model.Update;

public class MenuFragment extends Fragment implements View.OnClickListener{
    private View view;
    private LinearLayout mCollect,mClear,mUpdate,mAbout;
    private final static int UPDATE = 0x0001;
    public static MenuFragment newInstance() {
        MenuFragment f = new MenuFragment();
        return f;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    Update update = (Update) msg.obj;
                    boolean result = UpdateLess.$check(getActivity(), update);
                    if (!result) {
                        ToastLess.$(getActivity(), "您的版本已经是最新版");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public interface OnMenuClickListner{
        void closeMenu();
    }
    private OnMenuClickListner listener;
    public void setOnMenuClickListner(OnMenuClickListner listner){
        this.listener=listner;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCollect= (LinearLayout) view.findViewById(R.id.collect);
        mClear= (LinearLayout) view.findViewById(R.id.clear);
        mUpdate= (LinearLayout) view.findViewById(R.id.update);
        mAbout= (LinearLayout) view.findViewById(R.id.about);
        mCollect.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collect:
                collect();
                break;
            case R.id.update:
                update();
                break;
            case R.id.clear:
                clear();

                break;
            case R.id.about:
                about();
                break;
            default:
                break;
        }
        if(listener!=null){
            listener.closeMenu();
        }
    }

    private void collect() {
        mAbout.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), CollectActivity.class);
                startActivity(intent);
            }
        }, 50);
    }

    private void clear() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("您确定要清空缓存？")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Delete().from(History.class).execute();
                        new Delete().from(NewsItem.class).execute();

                        Toast.makeText(getActivity(), "清除成功！", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void about() {
        mAbout.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        }, 50);

    }

    private void update() {

        OkHttpClient client = NewsOkHttpClient.getInstance();
        final Request request = new Request.Builder()
                .url("http://fir.im/api/v2/app/version/5561c6c166bca9fe39001c75")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                UpdateParser updateParser = new UpdateParser();
                Update update = updateParser.convert(response.body().string());
                Message message = handler.obtainMessage();
                message.what = UPDATE;
                message.obj = update;
                handler.sendMessage(message);
            }
        });
    }
}