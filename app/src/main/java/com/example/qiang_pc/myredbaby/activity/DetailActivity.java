package com.example.qiang_pc.myredbaby.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qiang_pc.myredbaby.R;
import com.example.qiang_pc.myredbaby.global.BaseActivity;

public class DetailActivity extends BaseActivity {


    private WebView mWebView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        mWebView = getViewById(R.id.webView);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("URL");
        mWebView.loadUrl(url);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
