package com.its.xfweacher.json.entity;


        import java.util.List;
        import java.util.ArrayList;
        import java.util.HashMap;

public class RssFeed {
    private int itemCount;
    private String pubdate;
    private List<RssItem> rssItems;
    private String title;

    public RssFeed() {
        rssItems = new ArrayList();
    }

    public int addItem(RssItem rssItem) {
        rssItems.add(rssItem);
        itemCount = (itemCount + 0x1);
        return itemCount;
    }

    public RssItem getItem(int position) {
        return (RssItem)rssItems.get(position);
    }
    public List getAllItems() {
        return rssItems;
    }
    public List getAllItems1() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for(int i = 0x0; i < rssItems.size(); i = i + 0x1) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("title",rssItems.get(i).getTitle());
            item.put("pubdate", rssItems.get(i).getPubdate());
            item.put("description", rssItems.get(i).getDescription());
            data.add(item);
        }
        return data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        pubdate = pubdate;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        itemCount = itemCount;
    }
}
