package com.example.experiment5.Post_study;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PullXml extends AsyncTask {
    private final String xmlUrl = "http://119.23.190.57/person.xml";

    @Override
    protected Object doInBackground(Object[] params) {
        //获取网络XML数据

        try {
            URL url = new URL(xmlUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            if (code == 200) {
                //获取服务器返回来额结果
                InputStream is = connection.getInputStream();
                //使用PULL解析
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(is, "UTF-8");
                //获取解析的标签的类型
                int type = xmlPullParser.getEventType();
                while (type != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            //获取开始标签的名字
                            String starttgname = xmlPullParser.getName();
                            if ("person".equals(starttgname)) {
                                //获取id的值
                                String id = xmlPullParser.getAttributeValue(0);
                                Log.i("zxjPull", id);
                            } else if ("name".equals(starttgname)) {
                                String name = xmlPullParser.nextText();
                                Log.i("zxjPull", name);
                            } else if ("age".equals(starttgname)) {
                                String age = xmlPullParser.nextText();
                                Log.i("zxjPull", age);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                    }//细节：
                    type = xmlPullParser.next();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
