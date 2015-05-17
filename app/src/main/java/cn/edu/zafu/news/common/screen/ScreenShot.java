package cn.edu.zafu.news.common.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jayfeng.lesscode.core.DisplayLess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.edu.zafu.news.model.NewsContent;

/**
 * Created by lizhangqu on 2015/5/17.
 */
public class ScreenShot {
    private static final String TAG = "ScreenShot";

    public static Bitmap getRecyclerViewScreenshot(RecyclerView view, List<NewsContent> list) {
        int size = view.getAdapter().getItemCount();
        int height=0;
        int width=0;
        NewsContent newsContent=null;
        RecyclerView.ViewHolder holder=null;
        for (int i=0;i<size;i++){
            newsContent=list.get(i);
            holder = view.getAdapter().createViewHolder(view, newsContent.getType());
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
            width=view.getMeasuredWidth();
            if( newsContent.getType()==NewsContent.TYPE_IMAGE){
                height=height+ DisplayLess.$dp2px(200);
            }else{
                height=height+holder.itemView.getMeasuredHeight();
            }

        }
        Bitmap bigBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < size; i++) {
            newsContent=list.get(i);
            holder = view.getAdapter().createViewHolder(view, newsContent.getType());
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();

            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        return bigBitmap;
    }

    // 保存到sdcard
    public static void savePic(Bitmap b, String strFileName) {
        File file=new File("/sdcard/zafu_news/");
        if(!file.exists()){
            file.mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("/sdcard/zafu_news/"+strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 50, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null){
                    fos.flush();
                    fos.close();
                    fos=null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
