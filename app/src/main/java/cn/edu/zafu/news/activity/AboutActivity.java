package cn.edu.zafu.news.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AppLess;

import cn.edu.zafu.news.R;


public class AboutActivity extends BaseActivity {
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        showToolbar("关于我们");
        initView();
    }


    private void initView() {
        mVersion = (TextView) findViewById(R.id.version);
        mVersion.setText("当前版本：" + AppLess.$vername(this));
    }

}
