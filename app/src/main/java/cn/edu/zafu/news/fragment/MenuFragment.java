/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.edu.zafu.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.edu.zafu.news.R;

public class MenuFragment extends Fragment implements View.OnClickListener{
    private View view;
    private LinearLayout mCollect,mClear,mUpdate,mAbout;
    public static MenuFragment newInstance() {
        MenuFragment f = new MenuFragment();
        return f;
    }
    public interface OnMenuClickListner{
        void closeMenu();
    }
    private OnMenuClickListner listener;
    public void setOnMenuClickListner(OnMenuClickListner listner){
        this.listener=listner;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCollect= (LinearLayout) view.findViewById(R.id.collect);
        mClear= (LinearLayout) view.findViewById(R.id.clear);
        mUpdate= (LinearLayout) view.findViewById(R.id.update);
        mAbout= (LinearLayout) view.findViewById(R.id.about);
        mCollect.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collect:
                Toast.makeText(getActivity(),"collect",Toast.LENGTH_LONG).show();
                break;
            case R.id.update:
                Toast.makeText(getActivity(),"update",Toast.LENGTH_LONG).show();
                break;
            case R.id.clear:
                Toast.makeText(getActivity(),"clear",Toast.LENGTH_LONG).show();
                break;
            case R.id.about:
                Toast.makeText(getActivity(),"about",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        if(listener!=null){
            listener.closeMenu();
        }
    }
}