package cn.edu.zafu.news.ui.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.ui.app.ToolbarFragment;
import cn.edu.zafu.news.zxing.encode.CodeCreator;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-19
 * Time: 22:48
 * FIXME
 */
public class QrcodeFragment extends ToolbarFragment {
    private ImageView qrcode;
    private String data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_qrcode,container,false);
        showToolbar(view,"生成二维码");
        getParams();
        initView(view);
        return view;
    }

    private void getParams() {
        Bundle bundle =getArguments();
        data = bundle.getString("data");
    }

    private void initView(View view) {
        qrcode= (ImageView) view.findViewById(R.id.qrcode);
        try {
            Bitmap bitmap= CodeCreator.createQRCode(data, dip2px(getActivity(),250), dip2px(getActivity(),250));
            qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将dip的值转化为px值
     *
     * @param context  上下文
     * @param dipValue dip的值
     * @return px的值
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
