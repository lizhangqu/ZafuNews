package cn.edu.zafu.news.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jayfeng.lesscode.core.UpdateLess;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.adapter.NewsPagerAdapter;
import cn.edu.zafu.news.common.http.client.NewsOkHttpClient;
import cn.edu.zafu.news.common.parser.impl.UpdateParser;
import cn.edu.zafu.news.fragment.MenuFragment;
import cn.edu.zafu.news.model.NewsItem;
import cn.edu.zafu.news.model.Update;
import cn.edu.zafu.news.view.PagerSlidingTabStrip;
import cn.edu.zafu.news.zxing.android.CaptureActivity;


public class MainActivity extends BaseActivity{
    private DrawerLayout mDrawerLayout;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private MenuFragment menuFragment;
    private ViewPager mViewPager;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private final static int UPDATE = 0x0001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    Update update = (Update) msg.obj;
                    boolean result = UpdateLess.$check(MainActivity.this, update);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToolbar(null);
        initDrawLayout();
        initViewPager();
        requestUpdateData();
    }

    private void initViewPager() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new NewsPagerAdapter(this, getSupportFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        initTabsValue();
    }
    /**
     * mPagerSlidingTabStrip默认值配置
     *
     */
    private void initTabsValue() {
        Resources resources=getResources();
        DisplayMetrics metrics=resources.getDisplayMetrics();
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(resources.getColor(R.color.tab_selected_strip));
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(resources.getColor(R.color.tab_background));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                0, metrics));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3, metrics));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(resources.getColor(R.color.tab_text_selected));
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(resources.getColor(R.color.tab_text_unselected));
        //设置字体大小
        mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                13, metrics));
    }

    private void initDrawLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
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
        menuFragment= (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);
        menuFragment.setOnMenuClickListner(new MenuFragment.OnMenuClickListner() {
            @Override
            public void closeMenu() {
                if(mDrawerLayout.isDrawerOpen(Gravity.START)){
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
            }
        });
    }


    @Override
    protected boolean showNavigationIcon() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_search){
            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
            startActivity(intent);
        }else if(id==R.id.action_qrcode){
            Intent intent = new Intent(MainActivity.this,
                    CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                if(content.startsWith("zafu")){
                    String[] temp=content.split("\\|");
                    Intent intent = new Intent(this, ContentActivity.class);
                    Bundle bundle = new Bundle();
                    NewsItem item=new NewsItem();
                    item.setUrl(temp[1]);
                    item.setTitle(temp[2]);
                    bundle.putSerializable("news_item", item);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"非法二维码，请使用新闻网客户端生成的二维码！",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void requestUpdateData() {
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
