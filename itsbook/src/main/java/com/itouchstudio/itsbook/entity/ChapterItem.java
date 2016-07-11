package com.itouchstudio.itsbook.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by its on 2016-06-22.
 */
public class ChapterItem {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String title;
    @DatabaseField
    public String fileName;

}
