package com.its.xfweacher.helper;


import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.SAXException;
import android.util.Log;

import com.its.xfweacher.json.entity.RssFeed;
import com.its.xfweacher.json.entity.RssItem;

import org.xml.sax.Attributes;

public class RssHandler extends DefaultHandler {
    RssFeed rssFeed;
    RssItem rssItem;
    String lastElementName = "";
    final int RSS_TITLE = 0x1;
    final int RSS_LINK = 0x2;
    final int RSS_DESCRIPTION = 0x3;
    final int RSS_CATEGORY = 0x4;
    final int RSS_PUBDATE = 0x5;
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

        switch(currentFlag) {
            case RSS_TITLE:
            {
                Log.i("i", "\u8981\u83b7\u53d6\u7684\u5185\u5bb9\uff1a" + text);
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
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
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
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if("item".equals(localName) && rssItem != null) {//只需要item标记的内容
            rssFeed.addItem(rssItem);
        }
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public RssFeed getRssFeed() {
        return rssFeed;
    }
}
