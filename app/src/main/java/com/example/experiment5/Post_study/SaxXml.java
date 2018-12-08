package com.example.experiment5.Post_study;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxXml extends AsyncTask {
    private final String xmlUrl = "http://119.23.190.57/person.xml";
    private String cuurentTag=null;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL(xmlUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);


            int code=connection.getResponseCode();

            if(code==200){
                //获取服务器返回来额结果
                InputStream is=connection.getInputStream();
                //解析XMLSax解析=====================================
                //(边读边解析)
                SAXParserFactory saxParseFactory=SAXParserFactory.newInstance();
                SAXParser saxParse= saxParseFactory.newSAXParser();
                saxParse.parse(is,new DefaultHandler(){
                    @Override
                    public void startDocument() throws SAXException {
                        super.startDocument();
                        Log.i("zxjSam","开始文档");
                    }
                    @Override
                    public void endDocument() throws SAXException {
                        super.endDocument();
                        Log.i("zxjSam","结束文档");
                    }
                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        super.startElement(uri, localName, qName, attributes);
                        cuurentTag=localName;
                        //获取开始标签的名字
                        if("person".equals(localName)){
                            //取属性的值
                            String id=attributes.getValue(0);
                            Log.i("zxjSam",id);
                        }
                    }
                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        super.endElement(uri, localName, qName);
                        cuurentTag=null;
                    }
                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        super.characters(ch, start, length);
                        if("name".equals(cuurentTag)){
                            //获取<name>的值
                            String name=new String(ch,start,length);
                            Log.i("zxjSam", "   "+name);
                        }else if("age".equals(cuurentTag)){
                            //获取<name>的值
                            String age=new String(ch,start,length);
                            Log.i("zxjSam", "   "+age);
                        }
                    }
                });
            }
        }
        catch (MalformedURLException e ){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;

    }
}
