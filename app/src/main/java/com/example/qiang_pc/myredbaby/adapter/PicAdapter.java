package com.example.qiang_pc.myredbaby.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.qiang_pc.myredbaby.R;
import com.example.qiang_pc.myredbaby.bean.EntertainmentBean;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by qiang-pc on 2016/3/11.
 */
public class PicAdapter extends BGAAdapterViewAdapter<EntertainmentBean.ShowapiResBodyEntity
        .NewslistEntity>{

    private Context mContext;
    public PicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mContext=context;
    }

    @Override
    protected void fillData(BGAViewHolderHelper bgaViewHolderHelper, int i, EntertainmentBean
            .ShowapiResBodyEntity.NewslistEntity newslistEntity) {
        bgaViewHolderHelper.setText(R.id.tv_title,newslistEntity.getTitle()).setText(R.id
                .tv_time,newslistEntity.getCtime());
        ImageView imageView = bgaViewHolderHelper.getView(R.id.ImageView);
        if(!TextUtils.isEmpty(newslistEntity.getPicUrl())){
            Glide.with(mContext).load(newslistEntity.getPicUrl()).into(imageView);
        }else{
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
