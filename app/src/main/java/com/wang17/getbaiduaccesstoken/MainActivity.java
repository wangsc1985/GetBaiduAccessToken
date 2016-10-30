package com.wang17.getbaiduaccesstoken;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.pcs.BaiduPCSActionInfo;
import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSStatusListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button_getQuota, button_download,button_downloadByStream, button_getList;
    private Handler mbUiThreadHandler = null;
    private ProgressBar progressBar;

    public static final String ACCESS_TOKEN = "23.49b0c9b25b4a6431ce800c7cb3839a27.2592000.1478355666.1649802760-1641135";
    public static final int PROGRESS_MAX = 100;
    public static final String SOURCE = "/apps/wp2pcs/apps/寿康宝鉴日历.apk";
    public static final String APK_DIR_PATH = Environment.getExternalStorageDirectory() + "/360";
    public static final String TARGET = APK_DIR_PATH + "/寿康宝鉴日历.apk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("wangsc", "MainActivity is loadding ... ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbUiThreadHandler = new Handler();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button_download = (Button) findViewById(R.id.button_download);
        button_downloadByStream = (Button) findViewById(R.id.button_downloadByStream);
        button_getQuota = (Button) findViewById(R.id.button_getQuota);
        button_getList = (Button) findViewById(R.id.button_getList);


        progressBar.setMax(PROGRESS_MAX);
        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(SOURCE, TARGET);
            }
        });
        button_downloadByStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFileFromStream(SOURCE,TARGET);
            }
        });
        button_getQuota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuota();
            }
        });
        button_getList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
    }

    private void downloadFile(String source, String target) {
        final String s = source, t = target;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(ACCESS_TOKEN); //mbOauth为使用Oauth得到的access_token
                    final BaiduPCSActionInfo.PCSSimplefiedResponse pcsSimplefiedResponse = api.downloadFile(s, t, new BaiduPCSStatusListener() {
                        @Override
                        public void onProgress(long l, long l1) {
                            final int value = (int) (l * PROGRESS_MAX / l1);
                            mbUiThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (value <= PROGRESS_MAX)
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
        }).start();
    }

    private void downloadFileFromStream(String source, String target) {
        final String s = source, t = target;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(ACCESS_TOKEN); //mbOauth为使用Oauth得到的access_token
                    api.downloadFileFromStream(s, t, new BaiduPCSStatusListener() {
                        @Override
                        public void onProgress(long l, long l1) {
                            final int value = (int) (l * PROGRESS_MAX / l1);
                            mbUiThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (value <= PROGRESS_MAX)
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
        }).start();
    }

    private void getList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(ACCESS_TOKEN); //mbOauth为使用Oauth得到的access_token

                    BaiduPCSActionInfo.PCSListInfoResponse listResponse = api.list("/apps/wp2pcs", "", "");
                    List<BaiduPCSActionInfo.PCSCommonFileInfo> list = listResponse.list;

                    for (int i = 0; i < list.size(); i++) {
                        final BaiduPCSActionInfo.PCSCommonFileInfo fileInfo = list.get(i);
                        mbUiThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.textView_msg)).append("\n" + fileInfo.path);
                            }
                        });
                    }

                } catch (Exception e) {
                    showExceptionDialog(e);
                }
            }
        }).start();
    }

    //Quota(`kou,ta) n. 限额；配额；定额；最低票数；
    private void getQuota() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(ACCESS_TOKEN); //mbOauth为使用Oauth得到的access_token

                    final BaiduPCSActionInfo.PCSQuotaResponse info = api.quota();
                    mbUiThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView_msg = (TextView) findViewById(R.id.textView_msg);
                            textView_msg.setText("total: " + info.total + "\nused: " + info.used);
                        }
                    });
                } catch (Exception e) {
                    showExceptionDialog(e);
                }
            }
        }).start();
    }

    private void showExceptionDialog(Exception e) {
        final String msg = e.getMessage();
        mbUiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setMessage(msg).setPositiveButton("知道了", null).show();
            }
        });
    }

    public void showSnackbar(String message) {
        new AlertDialog.Builder(MainActivity.this).setMessage(message).setPositiveButton("知道了", null).show();
    }
}
