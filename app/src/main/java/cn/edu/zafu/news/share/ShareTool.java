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
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;
import com.umeng.socialize.ynote.media.YNoteShareContent;

import cn.edu.zafu.news.R;

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
        setShareContent(context, title, content);
    }

    public void share(Activity context){
        mController.openShare(context, false);
    }
    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent(Activity context,String title,String content) {
        UMImage localImage = new UMImage(context, R.drawable.share);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(title + "|" + content);
        qzone.setTargetUrl(content);
        qzone.setTitle(title);
        qzone.setShareMedia(localImage);
        mController.setShareMedia(qzone);

        //QQ
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(title + "|" + content);
        qqShareContent.setTitle(title);
        qqShareContent.setTargetUrl(content);
        qqShareContent.setShareMedia(localImage);
        mController.setShareMedia(qqShareContent);

        //微信好友

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(title + "|" + content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(content);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(title + "|" + content);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(content);
        mController.setShareMedia(circleMedia);

        // 人人
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(title+"|"+content);
        mController.setAppWebSite(SHARE_MEDIA.RENREN,content);
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
    private void addCustomPlatforms(Activity activity) {
        // 添加微信平台
        addWXPlatform(activity);
        // 添加QQ平台
        addQQQZonePlatform(activity);
        // 添加印象笔记平台
        addEverNote(activity);
        // 添加有道云平台
        addYNote(activity);
        // 添加短信平台
        addSMS();
        // 添加email平台
        addEmail();
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        //mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
         // 添加人人网SSO授权
        //RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(activity,"201874", "28401c0964f04a72a14c812d6132fcef","3bf66e42db1e4fa9829b955cc300b737");
        //mController.getConfig().setSsoHandler(renrenSsoHandler);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,
                SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.YNOTE);

    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform(Activity context) {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context,
                appId, appKey);
       // qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
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
     * 有道云笔记分享。有道云笔记只支持图片，文本，图文分享
     */
    private void addYNote(Context context) {
        UMYNoteHandler yNoteHandler = new UMYNoteHandler(context);
        yNoteHandler.addToSocialSDK();
    }
    /**
     * 添加Email平台
     */
    private void addEmail() {
        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
    }
    /**
     * 添加短信平台
     */
    private void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }
}
