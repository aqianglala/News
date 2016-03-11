package com.example.qiang_pc.myredbaby.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qiang_pc.myredbaby.R;
import com.example.qiang_pc.myredbaby.activity.DetailActivity;
import com.example.qiang_pc.myredbaby.adapter.NoPicAdapter;
import com.example.qiang_pc.myredbaby.bean.NewsSearchBean;
import com.example.qiang_pc.myredbaby.global.BaseFragment;
import com.example.qiang_pc.myredbaby.interfaces.Urls;
import com.example.qiang_pc.myredbaby.utils.DateUtils;
import com.example.qiang_pc.myredbaby.utils.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qiang-pc on 2016/3/11.
 */
public class NoPicFragment extends BaseFragment {

    private ListView lv_content;
    private NoPicAdapter mAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_list);
        lv_content = getViewById(R.id.lv_content);
    }

    @Override
    protected void setListener() {
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                NewsSearchBean.ShowapiResBodyEntity.PagebeanEntity.ContentlistEntity item =
                        (NewsSearchBean.ShowapiResBodyEntity.PagebeanEntity.ContentlistEntity) parent.getAdapter().getItem
                        (position);
                intent.putExtra("URL",item.getLink());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String channelid = arguments.getString("CHANNELID");
        getData(channelid);
    }

    private void getData(String channelid) {
        OkHttpUtils
                .post()
                .url(Urls.URL_NEWSLISTBYID)
                .addParams("showapi_appid", Urls.appid)
                .addParams("showapi_sign", Urls.secret)
                .addParams("showapi_timestamp", DateUtils.getTimestamp())
                .addParams("channelId", channelid)
                .tag(this)
                .build()
                .execute(new MyCallback());
    }


    private class MyCallback extends Callback<NewsSearchBean> {

        @Override
        public NewsSearchBean parseNetworkResponse(Response response) throws Exception {
            L.i("数据："+response.body());
            NewsSearchBean bean = new Gson().fromJson(response.body().string(), NewsSearchBean
                    .class);
            return bean;
        }

        @Override
        public void onError(Call call, Exception e) {
            L.i("数据加载错误");
        }

        @Override
        public void onResponse(NewsSearchBean response) {
//            L.i("数据长度："+response.getShowapi_res_body().getNewslist().size()+"");
            mAdapter = new NoPicAdapter(mActivity, R.layout.item_pic_layout);
            mAdapter.setDatas(response.getShowapi_res_body().getPagebean().getContentlist());
            lv_content.setAdapter(mAdapter);

        }
    }

    @Override
    protected void onUserVisible() {

    }
}
