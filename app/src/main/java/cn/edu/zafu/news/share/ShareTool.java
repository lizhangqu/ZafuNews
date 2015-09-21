package cn.edu.zafu.news.share;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMEvernoteHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.media.EvernoteShareContent;
import com.umeng.socialize.media.DoubanShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;
import com.umeng.socialize.ynote.media.YNoteShareContent;

/**
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-21
 * Time: 16:57
 */
public class ShareTool {

    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");


    public ShareTool(Activity context,String title,String content){
        addCustomPlatforms(context);
        setShareContent(context,title,content);
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent(Activity context,String title,String content) {


        UMImage urlImage = new UMImage(context,
                "http://www.umeng.com/images/pic/social/integrated_3.png");

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
        weixinContent.setTitle("友盟社会化分享组件-微信");
        weixinContent.setTargetUrl("http://www.umeng.com/social");
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(urlImage);
        circleMedia.setTargetUrl(content);
        mController.setShareMedia(circleMedia);

        // 人人
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(title+"|"+content);
        mController.setShareMedia(renrenShareContent);

        //微博
        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setShareContent(title+"|"+content);
        mController.setShareMedia(tencent);

        // 邮件
        MailShareContent mail = new MailShareContent();
        mail.setTitle(title);
        mail.setShareContent(content);
        mController.setShareMedia(mail);

        // 短信
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(title+"|"+content);
        mController.setShareMedia(sms);


        //豆瓣
        DoubanShareContent doubanShareContent=new DoubanShareContent();
        doubanShareContent.setShareContent(title+"|"+content);
        mController.setShareMedia(doubanShareContent);

        //有道云笔记
        YNoteShareContent yNoteShareContent = new YNoteShareContent();
        yNoteShareContent.setTitle(title);
        yNoteShareContent.setShareContent(content);
        mController.setShareMedia(yNoteShareContent);


        // 印象笔记
        EvernoteShareContent shareContent = new EvernoteShareContent(title+"|"+content);
        mController.setShareMedia(shareContent);
    }
    /**
     * 添加所有的平台</br>
     */
    private void addCustomPlatforms(Activity context) {
        // 添加微信平台
        addWXPlatform(context);
        // 添加印象笔记平台
        addEverNote(context);
        // 添加有道笔记
        addYNote(context);
        // 添加短信平台
        addSMS();
        // 添加email平台
        addEmail();
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN, SHARE_MEDIA.EMAIL, SHARE_MEDIA.EVERNOTE,
                SHARE_MEDIA.SMS ,SHARE_MEDIA.YNOTE );
        mController.openShare(context, false);
    }
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform(Context context) {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wxe65ebd97420ce551";
        String appSecret = "9836f632d0b7cec503078c41bac3808d";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
    /**
     * 添加印象笔记平台
     */
    private void addEverNote(Context context) {
        UMEvernoteHandler evernoteHandler = new UMEvernoteHandler(context);
        evernoteHandler.addToSocialSDK();
    }
    /**
     * 有道云笔记分享。有道云笔记只支持图片，文本，图文分享</br>
     */
    private void addYNote(Context context) {
        UMYNoteHandler yNoteHandler = new UMYNoteHandler(context);
        yNoteHandler.addToSocialSDK();
    }
    /**
     * 添加Email平台</br>
     */
    private void addEmail() {
        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
    }
    /**
     * 添加短信平台</br>
     */
    private void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }
}
