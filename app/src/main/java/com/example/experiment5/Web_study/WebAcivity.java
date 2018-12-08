package com.example.experiment5.Web_study;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.experiment5.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebAcivity extends Activity {

    private  static  final String URL = "http://www.baidu.com";
    private EditText et_address;
    private WebView myWebView;
    private ProgressBar progressBar;
    private TextView titleText;
    private ImageView iconImage;


    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);

        iconImage = (ImageView) findViewById(R.id.urt_icon);
        titleText = (TextView) findViewById(R.id.url_title);
        et_address = (EditText) findViewById(R.id.et_address);
        myWebView = (WebView) findViewById(R.id.myWebView);
        mProgressDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.prog);
        WebSettings mysettings = myWebView.getSettings();
        mysettings.setJavaScriptEnabled(true);
        mysettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mysettings.setLoadsImagesAutomatically(true);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if (s.indexOf("google")!=-1)
                {
                    Toast.makeText(WebAcivity.this,"该网址已经被拦截,跳转至百度页面",Toast.LENGTH_LONG).show();
                    myWebView.loadUrl(URL);
                }
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);

                String title = webView.getTitle();
                titleText.setText(title);
//                String faviconUrl = Uri.parse(s).getHost() + "/favicon.ico";
//                Bitmap icon = webView.getFavicon();
//                if(icon == null)
//                {
//                    Log.i("zxj","icon is null!");
//                }
//                iconImage.setImageBitmap(icon);
            }


            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                String data = "Page NO FOUND！";
                myWebView.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    // 加载中
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                iconImage.setImageBitmap(icon);
            }

        });
        myWebView.loadUrl(URL);
    }

    public void click(View v){
        String url = et_address .getText().toString().trim();
        if(url == null || url.isEmpty()){
            url =URL;
        }
        myWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&myWebView.canGoBack()){
            myWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode,event);
    }
}
