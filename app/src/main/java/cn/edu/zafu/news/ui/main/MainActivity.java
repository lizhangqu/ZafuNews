package cn.edu.zafu.news.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import cn.edu.zafu.corepage.base.BaseActivity;
import cn.edu.zafu.corepage.core.CoreAnim;


public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_SCAN = 0x0002;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage("main", null, CoreAnim.none);

        UpdateConfig.setDebug(true);
        UpdateConfig.setDeltaUpdate(false);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);

        UmengUpdateAgent.update(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
