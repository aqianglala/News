package com.example.qiang_pc.myredbaby.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qiang_pc.myredbaby.R;
import com.example.qiang_pc.myredbaby.adapter.TabFragmentAdapter;
import com.example.qiang_pc.myredbaby.bean.ChannelBean;
import com.example.qiang_pc.myredbaby.fragment.NoPicFragment;
import com.example.qiang_pc.myredbaby.fragment.PicFragment;
import com.example.qiang_pc.myredbaby.global.BaseActivity;
import com.example.qiang_pc.myredbaby.global.BaseFragment;
import com.example.qiang_pc.myredbaby.interfaces.Urls;
import com.example.qiang_pc.myredbaby.utils.DateUtils;
import com.example.qiang_pc.myredbaby.utils.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPage;
    private TabLayout mTabLayout;

    private TabFragmentAdapter tabFragmentAdapter;

    private List<ChannelBean.ShowapiResBodyEntity.ChannelListEntity> mChannelList;
    private ArrayList<String> mTabTitles;
    private LinearLayout ll_error;
    private Button btn_retry;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = getViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ll_error = getViewById(R.id.ll_error);
        btn_retry = getViewById(R.id.btn_retry);
    }

    @Override
    protected void setListener() {
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        getData();
    }

    private void getData() {
        OkHttpUtils
                .post()
                .url(Urls.URL_CHANNEL)
                .addParams("showapi_appid", Urls.appid)
                .addParams("showapi_sign", Urls.secret)
                .addParams("showapi_timestamp", DateUtils.getTimestamp())
                .tag(this)
                .build()
                .execute(new MyCallback());
    }

    private void initTabLayout() {
        mViewPage= (ViewPager) findViewById(R.id.id_viewpager);
        mTabLayout= (TabLayout) findViewById(R.id.id_tabLayout);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        int size = mTabTitles.size();

        ArrayList<BaseFragment> fragments = new ArrayList<>();

        for(int i=0;i<size;i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitles.get(i)));
            Bundle bundle = new Bundle();
            BaseFragment fragment ;
            if(i== 0){
                bundle.putString("URL",Urls.URL_ENTERTAINMENT);
                fragment = new PicFragment();
            }else if(i==1){
                bundle.putString("URL",Urls.URL_SPORT);
                fragment = new PicFragment();
            }else{
                bundle.putString("CHANNELID",mChannelList.get(i-2).getChannelId());
                fragment = new NoPicFragment();
            }
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }


        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments,
                mTabTitles);
        mViewPage.setAdapter(tabFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPage);
        mTabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);
    }

    private class MyCallback extends Callback<ChannelBean> {

        @Override
        public ChannelBean parseNetworkResponse(Response response) throws Exception {
            ChannelBean bean = new Gson().fromJson(response.body().string(), ChannelBean
                    .class);
            return bean;
        }

        @Override
        public void onError(Call call, Exception e) {
            L.i("数据加载错误");
            ll_error.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResponse(ChannelBean response) {
            ll_error.setVisibility(View.GONE);
            L.i("数据长度："+response.getShowapi_res_body().getChannelList().size()+"");

            mTabTitles = new ArrayList<>();
            mTabTitles.add("娱乐花边");
            mTabTitles.add("体育新闻");
            mChannelList = response
                    .getShowapi_res_body().getChannelList();
            // 取头5条
            for(int i=0;i<5;i++){
                mTabTitles.add(mChannelList.get(i).getName());
            }

            initTabLayout();
        }
    }

    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
