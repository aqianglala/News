package com.example.qiang_pc.myredbaby.adapter;

import android.content.Context;

import com.example.qiang_pc.myredbaby.R;
import com.example.qiang_pc.myredbaby.bean.NewsSearchBean;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by qiang-pc on 2016/3/11.
 */
public class NoPicAdapter extends BGAAdapterViewAdapter<NewsSearchBean.ShowapiResBodyEntity
        .PagebeanEntity.ContentlistEntity>{

    private Context mContext;
    public NoPicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mContext=context;
    }

    @Override
    protected void fillData(BGAViewHolderHelper bgaViewHolderHelper, int i, NewsSearchBean
            .ShowapiResBodyEntity.PagebeanEntity.ContentlistEntity newslistEntity) {
        bgaViewHolderHelper.setText(R.id.tv_title,newslistEntity.getTitle()).setText(R.id
                .tv_time,newslistEntity.getPubDate());
    }
}
