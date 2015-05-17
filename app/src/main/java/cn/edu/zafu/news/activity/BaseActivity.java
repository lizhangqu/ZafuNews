package cn.edu.zafu.news.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import java.lang.reflect.Method;

import cn.edu.zafu.news.R;

/**
 * Created by lizhangqu on 2015/5/14.
 */
public class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;

    protected void showToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(title!=null){
            toolbar.setTitle(title);
        }
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        if (showNavigationIcon()) {
            toolbar.setNavigationIcon(R.mipmap.toolbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    protected boolean showNavigationIcon() {
        return true;
    }
    /**
     *显示溢出菜单图标
     **/
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public  <T extends View> T $(int viewId) {
        return (T) findViewById(viewId);
    }
}
