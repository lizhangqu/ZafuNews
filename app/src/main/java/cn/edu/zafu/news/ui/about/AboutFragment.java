package cn.edu.zafu.news.ui.about;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.ui.app.ToolbarFragment;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-19
 * Time: 22:31
 * FIXME
 */
public class AboutFragment extends ToolbarFragment {
    private TextView mVersion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about,container,false);
        showToolbar(view,"关于我们");
        initView(view);
        return view;
    }

    private void initView(View view) {
        mVersion = (TextView) view.findViewById(R.id.version);
        mVersion.setText("当前版本：" + getVersionName(getActivity()));
    }

    /**
     * 获得版本名
     *
     * @param context 上下文
     * @return 版本名
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
