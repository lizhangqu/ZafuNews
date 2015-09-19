package cn.edu.zafu.news.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.jayfeng.lesscode.core.DisplayLess;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.zxing.encode.CodeCreator;


public class QrcodeActivity extends BaseActivity {
    private ImageView qrcode;
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        showToolbar("生成二维码");
        getParams();
        initView();
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        data = bundle.getString("data");
    }

    private void initView() {
        qrcode= (ImageView) findViewById(R.id.qrcode);
        try {
            Bitmap bitmap= CodeCreator.createQRCode(data, DisplayLess.$dp2px(250),DisplayLess.$dp2px(250));
            qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
