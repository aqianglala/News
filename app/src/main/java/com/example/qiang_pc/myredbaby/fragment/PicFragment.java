package com.example.qiang_pc.myredbaby.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qiang_pc.myredbaby.R;
import com.example.qiang_pc.myredbaby.activity.DetailActivity;
import com.example.qiang_pc.myredbaby.adapter.PicAdapter;
import com.example.qiang_pc.myredbaby.bean.EntertainmentBean;
import com.example.qiang_pc.myredbaby.global.BaseFragment;
import com.example.qiang_pc.myredbaby.interfaces.Urls;
import com.example.qiang_pc.myredbaby.utils.DateUtils;
import com.example.qiang_pc.myredbaby.utils.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.net.URLEncoder;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qiang-pc on 2016/3/11.
 */
public class PicFragment extends BaseFragment  implements BGARefreshLayout
        .BGARefreshLayoutDelegate {

    private ListView lv_content;
    private PicAdapter mAdapter;

    private boolean isRefresh = true;
    private boolean isLoading;

    private int page = 1;
    public boolean mHasMore=true;

    private BGARefreshLayout mRefreshLayout;
    private String url;
    private List<EntertainmentBean.ShowapiResBodyEntity.NewslistEntity> mData;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_list);
        lv_content = getViewById(R.id.lv_content);

        mRefreshLayout = getViewById(R.id.rl_recyclerview_refresh);
        initRefreshViewHolder();

        mAdapter = new PicAdapter(mActivity, R.layout.item_pic_layout);
        mAdapter.setDatas(mData);
        lv_content.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                EntertainmentBean.ShowapiResBodyEntity.NewslistEntity item = (EntertainmentBean
                        .ShowapiResBodyEntity.NewslistEntity) parent.getAdapter().getItem
                                (position);
                intent.putExtra("URL",item.getUrl());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        url = arguments.getString("URL");
        mRefreshLayout.beginRefreshing();
    }

    private void getData() {
        isLoading=true;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("showapi_appid", Urls.appid)
                .addParams("showapi_sign", Urls.secret)
                .addParams("showapi_timestamp", DateUtils.getTimestamp())
                .addParams("num", URLEncoder.encode("20"))
                .addParams("page", URLEncoder.encode(page+""))
                .tag(this)
                .build()
                .execute(new MyCallback());
    }

    @Override
    protected void onUserVisible() {

    }

    private class MyCallback extends Callback<EntertainmentBean> {

        @Override
        public EntertainmentBean parseNetworkResponse(Response response) throws Exception {
            L.i("数据："+response.body());
            EntertainmentBean bean = new Gson().fromJson(response.body().string(), EntertainmentBean
                    .class);
            return bean;
        }

        @Override
        public void onError(Call call, Exception e) {
            L.i("数据加载错误");
            isLoading = false;
            stopWait();
        }

        @Override
        public void onResponse(EntertainmentBean response) {
            isLoading = false;
            stopWait();
//            L.i("数据长度："+response.getShowapi_res_body().getNewslist().size()+"");

            mData = response.getShowapi_res_body().getNewslist();

            if(isRefresh){
                mAdapter.clear();
                //将数据添加在开头，形成一种下拉加载更多的效果
//            mAdapter.addNewDatas(data);
                mAdapter.addMoreDatas(mData);
            }else{
                if(mData.size()==20){
                    mHasMore=true;
                }else{
                    mHasMore=false;
                }
                mAdapter.addMoreDatas(mData);
            }

            stopWait();
        }
    }

    private void initRefreshViewHolder() {
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new
                BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.colorPrimary);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.talkpal_logo);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
    }

    public void stopWait(){
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(!isLoading){
            isRefresh=true;
            //加载数据
            page = 1;
            getData();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(!isLoading) {
            if(mHasMore){
                isRefresh = false;
                page++;
                getData();
                return true;
            }else{
                showToast("没有更多数据了");
                return false;
            }
        }
        return true;
    }

}
