package cn.edu.zafu.news.ui.app;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.edu.zafu.corepage.base.BaseFragment;
import cn.edu.zafu.news.R;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-19
 * Time: 22:19
 * FIXME
 */
public class ToolbarFragment extends BaseFragment {
    protected Toolbar toolbar;

    protected boolean showNavigationIcon() {
        return true;
    }
    protected void showToolbar(View view, String title) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (title != null) {
            toolbar.setTitle(title);
        }

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.showOverflowMenu();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
        tintManager.setStatusBarTintEnabled(true);
        if (showNavigationIcon()) {
            toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popToBack();
                }
            });
        }
    }
}
