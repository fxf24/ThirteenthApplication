package com.example.qudqj_000.a2017_06_01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main3Activity extends AppCompatActivity {
    EditText et1;
    ListView lv1;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        et1 = (EditText)findViewById(R.id.url);
        lv1 = (ListView)findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        lv1.setAdapter(adapter);
    }

    public void onClick(View v){

        td.setDaemon(true);
        td.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"다음");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(Main3Activity.this, Main4Activity.class));
        return super.onOptionsItemSelected(item);
    }

    Handler handler = new Handler();
    Thread td = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new URL("https://news.google.com/news?cf=all&hl=ko&pz=1&ned=kr&output=rss");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int itemcount = readData(urlConnection.getInputStream());
                    for(int i = 0; i<itemcount; i++){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
//                                Toast.makeText(Main3Activity.this, "들어감", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    int readData(InputStream is) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(is);
            int datacount = parseDocument(document);
            return datacount;
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int parseDocument(Document doc) {
        Element docEle = doc.getDocumentElement();
        NodeList nodelist = docEle.getElementsByTagName("item");
        int count = 0;
        if ((nodelist != null) && (nodelist.getLength() > 0)) {
            for (int i = 0; i < nodelist.getLength(); i++) {
                String newsItem = getTagData(nodelist, i);
                if (newsItem != null) {
                    data.add(newsItem);
                    count++;
                }
            }
        }
        return count;
    }

    private String getTagData(NodeList nodelist, int index) {
        String newsItem = null;
        try {
            Element entry = (Element) nodelist.item(index);
            Element title = (Element) entry.getElementsByTagName("title").item(0);
            Element pubDate = (Element) entry.getElementsByTagName("pubDate").item(0);

            String titleValue = null;
            if (title != null) {
                Node firstChild = title.getFirstChild();
                if (firstChild != null) titleValue = firstChild.getNodeValue();
            }

            String pubDateValue = null;
            if(pubDate!=null){
                Node firstChild = pubDate.getFirstChild();
                if(firstChild!=null) pubDateValue = firstChild.getNodeValue();
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
            Date date = new Date();
            newsItem = titleValue + "-" + simpleDateFormat.format(date.parse(pubDateValue));
        } catch (DOMException e) {
            e.printStackTrace();
        }
        return newsItem;
    }
}
