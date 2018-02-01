package com.zzh.mt.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/10/26.
 */

public class NewsActivity extends BaseActivity {

    private static final String TAG = NewsActivity.class.getSimpleName();
    @BindView(R.id.new_webview)
    WebView webView;
    private String cookies = null;
    private String url = null;
    public static ValueCallback mFilePathCallback;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getAppContext().add(this);
        hasToolBar(false);
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("cookie"))){
            cookies = SharedPreferencesUtil.getInstance(mContext).getString("cookie");
        }
        url = Contants.BASEURL+Contants.Information+SharedPreferencesUtil.getInstance(mContext).getString("userid");
        initView(url);
    }

    @SuppressLint("JavascriptInterface")
    private void initView(final String url){
        if (cookies != null){
            syncCookie(url,cookies.substring(0,cookies.indexOf(";")));
        }
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true); // 显示放大缩小
        webView.getSettings().setSupportZoom(true); // 可以缩放
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);

        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback = filePathCallback;
                return true;
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {// android 系统版本>4.1.1
                mFilePathCallback = uploadMsg;
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {//android 系统版本<3.0
                mFilePathCallback = uploadMsg;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {//android 系统版本3.0+
                mFilePathCallback = uploadMsg;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                //hitTestResult==null解决重定向问题
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    if (openWithWebView(url)){
                        view.loadUrl(url);
                    }

                }
                return false;//false 可以点击超链接 反之不可
            }

            @Override
            public void onPageStarted(WebView view, String startUrl, Bitmap favicon) {
                super.onPageStarted(view, startUrl, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String endurl) {
                super.onPageFinished(view, endurl);
            }
        });
        webView.loadUrl(url);
        webView.addJavascriptInterface(new WebAppInterface(this),"app");

    }

    @Override
    public int getLayoutId() {
        return R.layout.news_layout;
    }

    private class WebAppInterface{
        Activity mActivity;
        WebAppInterface(Activity activity) {
            mActivity = activity;
        }
        @JavascriptInterface
        public void backApp() {
            //TODO 处理代码
            finish();

        }
        @JavascriptInterface
        public void targetApp(String url){
            String ur = url;
            Intent intent = new Intent(mContext, BirthdayActivity.class);
            intent.putExtra("url",Contants.BASEURL+url);
            SharedPreferencesUtil.getInstance(mContext).putString("url",Contants.BASEURL+url);
            startActivity(intent);
        }
    }
    public void syncCookie(String url,String cookie) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
            cookieManager.setCookie(url, cookie);
            cookieManager.flush();  //强制立即同步cookie

        } else {
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);
            cookieSyncManager.sync();
        }
    }
    protected boolean openWithWebView(String url) {//处理判断url的合法性
// TODO Auto-generated method stub
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return true;
        }
        return false;
    }
}
