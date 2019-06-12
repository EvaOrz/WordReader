package com.sunvote.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;

import com.sunvote.myapplication.R;
import com.sunvote.myapplication.WordUtil;

public class HtmlActivity extends Activity {
    private final static String TAG = "HtmlActivity";
    private WebView wv_content;

    private final String PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/word_test/test.doc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_html);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wv_content = findViewById(R.id.webview);

        WordUtil wordUtil = new WordUtil(PATH);

        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    wv_content.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/word_test/test.html");
                    break;
            }
        }
    };


}
