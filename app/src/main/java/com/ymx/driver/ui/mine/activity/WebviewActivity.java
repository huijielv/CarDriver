package com.ymx.driver.ui.mine.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityWebviewBinding;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.keyboardutil.KeyBoardEventBus;
import com.ymx.driver.viewmodel.mine.WebViewViewModel;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class WebviewActivity extends BaseActivity<ActivityWebviewBinding, WebViewViewModel> {
    public static final String TITLE = "title";
    public static final String URL = "url";

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, WebviewActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_enter_left, R.anim.slide_exit_right);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_webview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(Bundle savedInstanceState) {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        binding.webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        binding.webView.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        binding.webView.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.cancel();
            }

            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                binding.webView.loadUrl(s);
                return true;
            }
        });

        if (getIntent() != null) {
            String title = getIntent().getStringExtra(TITLE);
            String url = getIntent().getStringExtra(URL);
            if (!TextUtils.isEmpty(url)) {
                binding.webView.loadUrl(url);
            }
            binding.tvTitle.setText(title);
        }

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.progressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    protected void onDestroy() {
        binding.webView.destroy();
        KeyBoardEventBus.getDefault().unregister(activity);
        super.onDestroy();
    }
}
