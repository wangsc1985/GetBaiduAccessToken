package com.wang17.getbaiduaccesstoken;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSStatusListener;

public class PcsYunActivity extends AppCompatActivity {


    public static final String accessToken = "23.49b0c9b25b4a6431ce800c7cb3839a27.2592000.1478355666.1649802760-1641135";
    public static final String baiduPcsPath = "/apps/wp2pcs/apps/寿康宝鉴日历.apk";
    public static final int progressMax = 100;
    public static final String cacheDir = Environment.getExternalStorageDirectory() + "/360";
    public static final String cacheFile = cacheDir + "/寿康宝鉴日历.apk";

    private Button button;
    private ProgressBar progressBar;
    private TextView textView;
    private Handler uiThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcs_yun);

        uiThreadHandler = new Handler();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(MainActivity.PROGRESS_MAX);
        textView = (TextView) findViewById(R.id.textView);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download();
                    }
                }).start();
            }
        });
    }

    private void download() {
        try {
            BaiduPCSClient api = new BaiduPCSClient();
            api.setAccessToken(accessToken);
            api.downloadFileFromStream(baiduPcsPath, cacheFile, new BaiduPCSStatusListener() {
                @Override
                public void onProgress(long l, long l1) {
                    final long size = l;
                    final int value = (int) (l * progressMax / l1);
                    uiThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(size / 1024 / 1024 + "MB");
                            if (value <= progressMax)
                                progressBar.setProgress(value);
                        }
                    });
                }
            });
            progressBar.setProgress(0);
        } catch (Exception e) {
            showExceptionDialog(e);
        }
    }

    private void showExceptionDialog(Exception e) {
        final String msg = e.getMessage();
        uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(PcsYunActivity.this).setMessage(msg).setPositiveButton("知道了", null).show();
            }
        });
    }
}
