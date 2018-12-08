package com.example.experiment5.Post_study;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DomXml extends AsyncTask {

    private final String xmlUrl = "http://119.23.190.57/person.xml";
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL(xmlUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream is = connection.getInputStream();
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(is);
                //获取根标签
                Element element = document.getDocumentElement();
                Log.i("zxjDom", "根标签：" + element.getNodeName());
                NodeList nodeList = element.getElementsByTagName("person");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    //获取单个
                    Element personElement = (Element) nodeList.item(i);
                    //获取<person>属性id的值
                    String id = personElement.getAttribute("id");
                    Log.i("zxjDom", id);
                    //获取<person>下面的子标签<name><age>的值
                    Element nameElement = (Element) personElement.getElementsByTagName("name").item(0);
                    String name = nameElement.getTextContent();
                    Element ageElement = (Element) personElement.getElementsByTagName("age").item(0);
                    String age = ageElement.getTextContent();
                    Log.i("zxjDom", name + "  " + age);
                }
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e ){
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
