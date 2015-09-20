package cn.edu.zafu.news.ui.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.update.UmengUpdateAgent;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.zafu.corepage.core.CoreAnim;
import cn.edu.zafu.news.R;
import cn.edu.zafu.news.common.http.client.NewsOkHttpClient;
import cn.edu.zafu.news.common.parser.impl.WeatherParser;
import cn.edu.zafu.news.db.dao.BaseDao;
import cn.edu.zafu.news.db.helper.DatabaseHelper;
import cn.edu.zafu.news.db.model.History;
import cn.edu.zafu.news.db.model.NewsItem;
import cn.edu.zafu.news.model.weather.Weather;
import cn.edu.zafu.news.ui.app.ToolbarFragment;
import cn.edu.zafu.news.ui.main.adapter.NewsPagerAdapter;


public class MainFragment extends ToolbarFragment {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private final static int WEATHER = 0x0001;

    private static final int REQUEST_CODE_SCAN = 1;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private TextView temp;
    private TextView place;
    private TextView min2max;
    private TextView weather;
    private ImageView weatherIcon;
    private TextView date;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEATHER: {
                    Weather weather = (Weather) msg.obj;
                    if (weather != null) {
                        MainFragment.this.weather.setText(weather.getToday().getWeatherStart() + "-" + weather.getToday().getWeatherEnd());
                        MainFragment.this.temp.setText(weather.getRealtime().getTemp());
                        // MenuFragment.this.place.setText(weather.getRealtime().getCity());
                        MainFragment.this.min2max.setText(weather.getToday().getTempMin() + "℃/" + weather.getToday().getTempMax() + "℃");
                        String t = weather.getRealtime().getWeather();
                        if (t.contains("晴")) {
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_sun));
                        } else if (t.contains("云")) {
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_cloudy));
                        } else if (t.contains("雨")) {
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_rain));
                        } else if (t.contains("阴")) {
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_cloud));
                        } else if (t.contains("雪")) {
                            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_rain));
                        }
                    }
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showToolbar(view, null);
        initDrawLayout(view);
        initNavigationView(view);
        initViewPager(view);
        initWeather(view);
    }

    private void initViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);

        mViewPager.setAdapter(new NewsPagerAdapter(getActivity(), getFragmentManager()));
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mViewPager.getAdapter());
    }


    private void initDrawLayout(View view) {
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.dl_main_drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void initNavigationView(View view) {
        NavigationView navigationView =
                (NavigationView) view.findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.collect:
                                openPage("collect", null, CoreAnim.slide);
                                break;
                            case R.id.update:
                                UmengUpdateAgent.forceUpdate(getActivity());
                                break;
                            case R.id.clear:
                                clear();

                                break;
                            case R.id.about:
                                openPage("about", null, CoreAnim.slide);
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            openPage("search",null,CoreAnim.slide);
        } else if (id == R.id.action_qrcode) {
            /*Intent intent = new Intent(getActivity(),
                    CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);*/

            openPageForResult(true,"scan",null,CoreAnim.slide,REQUEST_CODE_SCAN);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Log.e("TAG",content);
                if(content.startsWith("zafu")){
                    String[] temp=content.split("\\|");
                    Bundle bundle = new Bundle();
                    NewsItem item=new NewsItem();
                    item.setUrl(temp[1]);
                    item.setTitle(temp[2]);
                    bundle.putSerializable("news_item", item);
                    openPage("newscontent",bundle, CoreAnim.slide,true);
                }else{
                    Toast.makeText(getActivity(), "非法二维码，请使用新闻网客户端生成的二维码！", Toast.LENGTH_LONG).show();
                }

            }
            return ;
        }
    }



    private void clear() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("您确定要清空缓存？")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BaseDao<History, Integer> historyDao = DatabaseHelper.getHistoryDao(getActivity());
                        BaseDao<NewsItem, Integer> newsItemDao = DatabaseHelper.getNewsItemDao(getActivity());
                        try {
                            historyDao.deleteAll();
                            newsItemDao.deleteAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), "清除成功！", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
    private String getDate(){
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd EEEE");
        String result = sdf.format(date);
        return result;

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
                WeatherParser parser = new WeatherParser();
                Weather weather = parser.convert(response.body().string());
                Message message = handler.obtainMessage();
                message.what = WEATHER;
                message.obj = weather;
                handler.sendMessage(message);
                Log.d("TAG", weather.getRealtime().getTemp());
                Log.d("TAG", weather.getRealtime().getWeather());
                Log.d("TAG", weather.getToday().getTempMin() + "");
                Log.d("TAG", weather.getToday().getTempMax() + "");
                Log.d("TAG", weather.getToday().getWeatherStart() + "");
                Log.d("TAG", weather.getToday().getWeatherEnd() + "");
            }
        });
    }
}
