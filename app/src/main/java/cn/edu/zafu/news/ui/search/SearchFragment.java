package cn.edu.zafu.news.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.model.History;
import cn.edu.zafu.news.ui.app.ToolbarFragment;
import cn.edu.zafu.news.ui.history.HistoryFragment;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-19
 * Time: 23:04
 * FIXME
 */
public class SearchFragment extends ToolbarFragment {
    private EditText search;
    private ImageView clear;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        showToolbar(view, "");
        initView(view);
        initFragemnt();
        return view;

    }

    private void initFragemnt() {
        HistoryFragment historyFragment = HistoryFragment.newInstance();
        historyFragment.setOnHistory(new HistoryFragment.OnHistory() {
            @Override
            public void search(String content) {

                search.setText(content);
                SearchFragment.this.search(content);
            }
        });
        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_content, historyFragment, "HISTORY");
        transaction.commit();
    }
    /**
     * 隐藏键盘
     *
     * @param context 上下文
     * @param view    The currently focused view
     */
    public static void hideInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }


        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initView(View view) {
        search = (EditText) view.findViewById(R.id.search_content);
        clear = (ImageView) view.findViewById(R.id.clear_content);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search(search.getText().toString());
                    hideInputMethod(getActivity(), search);
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
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getActivity(), "请输入搜索内容！", Toast.LENGTH_SHORT).show();
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
        SearchResultFragment searchFragment = SearchResultFragment.newInstance(content);
        transaction.replace(R.id.ll_content, searchFragment).show(searchFragment);


        transaction.commit();

    }
}
