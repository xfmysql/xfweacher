package com.its.xfweacher.json.entity;


import android.text.TextUtils;

public class RssItem {
    private String _category;
    private String _description;
    private String _link;
    private String _pubdate;
    private String _title;

    public RssItem() {
    }

    public String getTitle() {
        if(!TextUtils.isEmpty(_title))
            if(_title.length() > 0x14) {
                return _title.substring(0x0, 0x13) + "...";
            }
        else _title="";
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getLink() {
        return _link;
    }

    public void setLink(String link) {
        _link = link;
    }

    public String getCategory() {
        return _category;
    }

    public void setCategory(String category) {
        _category = category;
    }

    public String getPubdate() {
        return _pubdate;
    }

    public void setPubdate(String pubdate) {
        _pubdate = pubdate;
    }

    public String toString() {
        return "RssItem [title=" + _title + ", description=" + _description + ", link=" + _link + ", category=" + _category + ", pubdate=" + _pubdate + "]";
    }
}
