package com.its.xfweacher.api;


import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.SAXException;
import android.util.Log;

import com.its.xfweacher.json.entity.RssFeed;
import com.its.xfweacher.json.entity.RssItem;

import org.xml.sax.Attributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RssHandler extends DefaultHandler {
    RssFeed rssFeed;
    RssItem rssItem;
    StringBuilder sb;
    String lastElementName = "";
    final int RSS_TITLE = 0x1;
    final int RSS_LINK = 0x2;
    final int RSS_DESCRIPTION = 0x3;
    final int RSS_CATEGORY = 0x4;
    final int RSS_PUBDATE = 0x5;
    final int RSS_Image = 0x6;
    int currentFlag = 0x0;

    public RssHandler() {
    }

    public void startDocument() throws SAXException {
        super.startDocument();
        rssFeed = new RssFeed();
        rssItem = new RssItem();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String text = new String(ch, start, length);
        sb.append(new String(ch, start, length) );

        switch(currentFlag) {
            case RSS_TITLE:
            {
                rssItem.setTitle(text);
                currentFlag = 0;
                return;
            }
            case RSS_PUBDATE:
            {
                rssItem.setPubdate(text);
                currentFlag = 0;
                return;
            }
            case RSS_CATEGORY:
            {
                rssItem.setCategory(text);
                currentFlag = 0;
                return;
            }
            case RSS_LINK:
            {
                rssItem.setLink(text);
                currentFlag = 0;
                return;
            }
            case RSS_DESCRIPTION:
            {
                rssItem.setDescription(text);
                currentFlag = 0;
                break;
            }
            case RSS_Image:
            {
                rssItem.setImage(text);
                currentFlag = 0;
                break;
            }
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        sb = new StringBuilder();

        if("chanel".equals(localName)) {
            currentFlag = 0x0;
            return;
        }
        if("item".equals(localName)) {
            rssItem = new RssItem();//当遇到一个item节点时，就实例化一个RSSItem对象
            return;
        }
        if("title".equals(localName)) {
            currentFlag = RSS_TITLE;
            return;
        }
        if("description".equals(localName)) {
            currentFlag = RSS_DESCRIPTION;
            return;
        }
        if("link".equals(localName)) {
            currentFlag = RSS_LINK;
            return;
        }
        if("pubDate".equals(localName)) {
            currentFlag = RSS_PUBDATE;
            return;
        }
        if("category".equals(localName)) {
            currentFlag = RSS_CATEGORY;
        }
        if("img".equals(localName)) {
            currentFlag = RSS_Image;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if("item".equals(localName) && rssItem != null) {//只需要item标记的内容
            String s = sb.toString();
            s = Html2Text(s).replace("\t","").replace("\n","");
            s = s.length()>30?s.substring(0,30):s;
            try {
                String pubdate = rssItem.getPubdate();
                String t = pubdate.substring(0, rssItem.getPubdate().lastIndexOf(".")).replace("T", " ");
                rssItem.setDescription(s + "\t\t" + t);
            }catch (Exception e){
                rssItem.setDescription(s + "\t\t1970-01-01" );
                e.printStackTrace();
            }
            rssFeed.addItem(rssItem);

            //Log.i("i", "\u8981\u83b7\u53d6\u7684\u5185\u5bb9\uff1a" + s);
        }
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public RssFeed getRssFeed() {
        return rssFeed;
    }

    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }
}
