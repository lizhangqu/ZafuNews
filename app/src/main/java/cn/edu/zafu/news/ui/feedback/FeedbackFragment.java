package cn.edu.zafu.news.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.ui.app.ToolbarFragment;

/**
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-22
 * Time: 15:41
 */
public class FeedbackFragment extends ToolbarFragment {
    private Button submit;
    private EditText email,qq,phone,content;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedback,container,false);
        showToolbar(view,"意见反馈");
        initView(view);
        return view;
    }
    private void initView(View view) {
        submit= (Button)view.findViewById(R.id.submit);
        email= (EditText) view.findViewById(R.id.email);
        qq= (EditText) view.findViewById(R.id.qq);
        phone= (EditText) view.findViewById(R.id.phone);
        content= (EditText) view.findViewById(R.id.content);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e=email.getText().toString();
                String q=qq.getText().toString();
                String p=phone.getText().toString();
                String c=content.getText().toString();
                feedback(e,q,p,c);
            }
        });
    }

    private void feedback(String email,String qq,String phone,String content) {
        final FeedbackAgent agent = new FeedbackAgent(getActivity());
        UserInfo info = agent.getUserInfo();
        if (info == null) {
            info = new UserInfo();
        }
        Map<String, String> contact = info.getContact();
        if (contact == null)
            contact = new HashMap<String, String>();
        contact.put("email", email+"");
        contact.put("qq", qq+"");
        contact.put("phone", phone+"'");

        agent.setUserInfo(info);
        new Thread(new Runnable() {
            @Override
            public void run() {
                agent.updateUserInfo();
            }
        }).start();


        Conversation defaultConversation = agent.getDefaultConversation();
        defaultConversation.addUserReply(content);
        defaultConversation.sync(new SyncListener() {
            @Override
            public void onReceiveDevReply(List<Reply> list) {

            }

            @Override
            public void onSendUserReply(List<Reply> list) {
                Toast.makeText(getActivity(), "反馈成功，感谢您的支持!", Toast.LENGTH_LONG).show();
                popToBack();
            }
        });
    }
}
