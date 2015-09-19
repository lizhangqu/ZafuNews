package cn.edu.zafu.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.jayfeng.lesscode.core.KeyBoardLess;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.fragment.HistoryFragment;
import cn.edu.zafu.news.fragment.HistoryFragment.OnHistory;
import cn.edu.zafu.news.fragment.SearchFragment;
import cn.edu.zafu.news.model.History;


public class SearchActivity extends BaseActivity {

    private EditText search;
    private ImageView clear;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        showToolbar("");
        initView();
        initFragemnt();
    }

    private void initFragemnt() {
        HistoryFragment historyFragment = HistoryFragment.newInstance();
        historyFragment.setOnHistory(new OnHistory() {
            @Override
            public void search(String content) {

                search.setText(content);
                SearchActivity.this.search(content);
            }
        });
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_content, historyFragment, "HISTORY");
        transaction.commit();
    }

    private void initView() {
        search = (EditText) findViewById(R.id.search_content);
        clear = (ImageView) findViewById(R.id.clear_content);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search(search.getText().toString());
                    KeyBoardLess.$hide(SearchActivity.this, search);
                    return true;
                }
                return false;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (search.getText().toString().length() > 0) {
                    if (clear.getVisibility() == View.GONE) {
                        clear.setVisibility(View.VISIBLE);
                    }
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
    }


    public void search(String content) {
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this,"请输入搜索内容！",Toast.LENGTH_SHORT).show();
            return;
        }
        History history = new History(content);
        History t = new Select()
                .from(History.class)
                .where("title = ?", history.getTitle())
                .executeSingle();
        if (t == null) {
            history.save();
        }

        transaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = SearchFragment.newInstance(content);
        transaction.replace(R.id.ll_content, searchFragment).show(searchFragment);


        transaction.commit();

    }
}
