package com.itouchstudio.itsbook.entity;

/**
 * Created by its on 2016-06-08.
 */
public class Segment {
    /**
     * 一章里面的位置
     */
    public int Position;
    /**
     * 每一段
     */
    public String SegmentText;
    public Segment(){}
    public Segment(int p,String s){
        Position = p;
        SegmentText = s ;
    }
}
