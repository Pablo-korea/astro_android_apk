package com.example.app;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.WebViewTransport;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.webview);

        // WebView 설정
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true); // 팝업을 허용

        // WebViewClient 설정
        myWebView.setWebViewClient(new WebViewClient());

        // WebChromeClient 설정 (팝업 처리)
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, android.os.Message resultMsg) {
                WebView newWebView = new WebView(MainActivity.this);
                WebSettings newWebSettings = newWebView.getSettings();
                newWebSettings.setJavaScriptEnabled(true);

                // 새로운 팝업 WebView를 WebChromeClient로 처리
                newWebView.setWebChromeClient(this);
                newWebView.setWebViewClient(new WebViewClient());

                // 부모 레이아웃에 새로운 WebView 추가
                myWebView.addView(newWebView);

                // WebViewTransport로 WebView 연결
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onCloseWindow(WebView window) {
                // 팝업 WebView 제거
                myWebView.removeView(window);
            }
        });

        // 로드할 URL 설정
        myWebView.loadUrl("http://astromentor.whenluck.com:5000"); // 팝업이 발생할 웹사이트
    }
}