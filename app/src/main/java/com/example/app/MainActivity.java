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

        // 추가된 설정
        webSettings.setUseWideViewPort(true); // WebView가 ViewPort 기능을 사용하게 함
        webSettings.setLoadWithOverviewMode(true); // 페이지를 화면 크기에 맞게 로드
        webSettings.setSupportZoom(true); // 줌 기능 활성화
        webSettings.setBuiltInZoomControls(true); // 줌 컨트롤 추가
        webSettings.setDisplayZoomControls(false); // 줌 컨트롤 안보이게 설정

        // 타이틀바 숨기기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 웹 페이지 로드가 완료된 후 자바스크립트로 헤더를 숨기는 코드 실행
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                myWebView.loadUrl("javascript:(function() { " +
                        "var header = document.querySelector('header');" +
                        "if (header) { header.style.display='none'; } " +
                        "})()");
            }
        });

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
        //myWebView.loadUrl("http://astromentor.whenluck.com:5000"); // 팝업이 발생할 웹사이트
        myWebView.loadUrl("http://43.201.105.108:9876/"); // 팝업이 발생할 웹사이트
    }
}