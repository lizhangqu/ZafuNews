package cn.edu.zafu.news.ui.main;

import android.os.Bundle;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import cn.edu.zafu.corepage.base.BaseActivity;
import cn.edu.zafu.corepage.core.CoreAnim;


public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage("main", null, CoreAnim.none);

        UpdateConfig.setDebug(true);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
       // UmengUpdateAgent.silentUpdate(this);
        //UmengUpdateAgent.forceUpdate(this);
    }
}
