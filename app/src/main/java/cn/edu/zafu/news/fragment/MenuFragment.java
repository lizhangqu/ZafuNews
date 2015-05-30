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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.UpdateLess;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.activity.AboutActivity;
import cn.edu.zafu.news.activity.CollectActivity;
import cn.edu.zafu.news.common.http.client.NewsOkHttpClient;
import cn.edu.zafu.news.common.parser.impl.UpdateParser;
import cn.edu.zafu.news.common.parser.impl.WeatherParser;
import cn.edu.zafu.news.model.History;
import cn.edu.zafu.news.model.NewsItem;
import cn.edu.zafu.news.model.Update;
import cn.edu.zafu.news.model.weather.Weather;

public class MenuFragment extends Fragment implements View.OnClickListener{
    private View view;
    private LinearLayout mCollect,mClear,mUpdate,mAbout;
    private TextView date;
    private final static int UPDATE = 0x0001;
    private final static int WEATHER = 0x0002;

    private TextView temp;
    private TextView place;
    private TextView min2max;
    private TextView weather;
    private ImageView weatherIcon;
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
                case WEATHER:
                    Weather weather = (Weather) msg.obj;
                    if (weather!=null) {
                        MenuFragment.this.weather.setText(weather.getToday().getWeatherStart()+"-"+weather.getToday().getWeatherEnd());
                        MenuFragment.this.temp.setText(weather.getRealtime().getTemp());
                      // MenuFragment.this.place.setText(weather.getRealtime().getCity());
                        MenuFragment.this.min2max.setText(weather.getToday().getTempMin()+"℃/"+weather.getToday().getTempMax()+"℃");
                        String t=weather.getRealtime().getWeather();
                        if(t.contains("晴")){
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_sun));
                        }else if(t.contains("云")){
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_cloudy));
                        }else if(t.contains("雨")){
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_rain));
                        }else if(t.contains("阴")){
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_cloud));
                        }else if(t.contains("雪")){
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_rain));
                        }
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
        initWeather(view);
        return view;
    }

    private void initWeather(View view) {
        place  = (TextView) view.findViewById(R.id.place);
        min2max  = (TextView) view.findViewById(R.id.min_max);
        weather  = (TextView) view.findViewById(R.id.weather);
        temp= (TextView) view.findViewById(R.id.temperate);
        weatherIcon= (ImageView) view.findViewById(R.id.pic);
        date= (TextView) view.findViewById(R.id.date);
        date.setText(getDate());
        OkHttpClient client = NewsOkHttpClient.getInstance();
        final Request request = new Request.Builder()
                .url("http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=101210107")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                WeatherParser parser=new WeatherParser();
                Weather weather = parser.convert(response.body().string());
                Message message = handler.obtainMessage();
                message.what = WEATHER;
                message.obj = weather;
                handler.sendMessage(message);
                Log.d("TAG",weather.getRealtime().getTemp());
                Log.d("TAG",weather.getRealtime().getWeather());
                Log.d("TAG",weather.getToday().getTempMin()+"");
                Log.d("TAG",weather.getToday().getTempMax()+"");
                Log.d("TAG",weather.getToday().getWeatherStart()+"");
                Log.d("TAG",weather.getToday().getWeatherEnd()+"");
            }
        });
    }
    private String getDate(){
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd EEEE");
        String result = sdf.format(date);
        return result;

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