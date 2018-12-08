package com.example.experiment5.Post_study;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.experiment5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import androidx.core.content.FileProvider;



public class PostActivity extends Activity {
    private Button domDeal;
    private Button saxDeal;
    private Button pullDeal;
    private Button jsonDeal;
    private Button getPicture;
    private Button getApk;
    private MyImageView myImageView;
    private static final String pictureUrl = "http://pic28.photophoto.cn/20130818/0020033143720852_b.jpg";
    private static final String apkUrl = "https://dldir1.qq.com/weixin/android/weixin673android1360.apk";
//    private static final String apkUrl = "http://39.108.188.185:8080//stitp/download/app-release.apk";
    private ProgressDialog progressDialog;
    private static final  String jsonData="{\"code\": 302000,\"text\": \"亲，已帮您找到相关新闻\",\"list\": [{\"article\": \"工信部:今年将大幅提网速降手机流量费\",\"source\": \"网易新闻\",\"icon\": \"\",\"detailurl\":\"http://news.163.com/15/0416/03/AN9SORGH0001124J.html\"}]}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.actiity_post);
        domDeal = (Button) findViewById(R.id.Dom_deal);
        saxDeal = (Button) findViewById(R.id.Sax_deal);
        pullDeal = (Button) findViewById(R.id.Pull_deal);
        jsonDeal = (Button) findViewById(R.id.Json_deal);
        getPicture = (Button) findViewById(R.id.Get_picture);
        getApk = (Button) findViewById(R.id.Get_apk);
        myImageView = (MyImageView) findViewById(R.id.ImageGeted);

        progressDialog =new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);



        domDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DomXml().execute();

            }
        });

        saxDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaxXml().execute();
            }
        });

        pullDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PullXml().execute();

            }
        });

        jsonDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject jsonObject = new JSONObject(jsonData);
                    int code = Integer.parseInt(jsonObject.getString("code"));
                    Log.i(" zxjJson","code为:"+code);
                    String text = jsonObject.getString("text");
                    Log.i(" zxjJson","text为:"+text);

                    JSONArray list = jsonObject.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        String article = object.getString("article");
                        String source = object.getString("source");
                        String icon = object.getString("icon");
                        String detailurl = object.getString("detailurl");
                        Log.i("zxjJson","article为:"+article);
                        Log.i("zxjJson","source为:"+source);
                        Log.i("zxjJson","icon为:"+icon);
                        Log.i("zxjJson","detailurl为:"+detailurl);
                    }
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });

        getPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myImageView.setImageURL(pictureUrl);
            }
        });

        getApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DownloadTask downloadTask = new DownloadTask(PostActivity.this);
                downloadTask.execute(apkUrl);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        downloadTask.cancel(true);
                    }
                });
            }
        });
    }


    private class DownloadTask extends AsyncTask<String,Integer,String>{
        private Context context;
        private PowerManager.WakeLock mWakeLock;
        String path = getApplicationContext().getCacheDir()+"/weixin.apk";
        public DownloadTask(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... url) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL urll=new URL(url[0]);
                connection = (HttpURLConnection) urll.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(path);
                Log.i("zxj",path);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        //在调用这个方法后，执行onProgressUpdate(Progress... values)，
                        //运行在主线程，用来更新pregressbar
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        //onProgressUpdate(Progress... values),
        // 执行在UI线程，在调用publishProgress(Progress... values)时，此方法被执行。
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        //onPostExecute(Result result),
        // 执行在UI线程，当后台操作结束时，此方法将会被调用。
        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            progressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
            {Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();}
//这里主要是做下载后自动安装的处理
            File file=new File(path);
            if(file.exists()){
                Log.i("zxj","文件已经下载,大小为:"+file.length());
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if(Build.VERSION.SDK_INT>=24){
                Uri apkUri =
                        FileProvider.getUriForFile(context, "com.example.experiment5.fileprovider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
            else{
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            progressDialog.show();
        }
    }

}


