package com.example.memorizewords.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String english_mean;
    @ColumnInfo
    private String chinese_mean;
    @ColumnInfo
    private boolean is_visitable;

    public boolean isIs_visitable() {
        return is_visitable;
    }

    public void setIs_visitable(boolean is_visitable) {
        this.is_visitable = is_visitable;
    }

    public Word(String english_mean, String chinese_mean) {
        this.english_mean = english_mean;
        this.chinese_mean = chinese_mean;
    }

    public int getId() {
        return id;
    }

    public String getEnglish_mean() {
        return english_mean;
    }

    public String getChinese_mean() {
        return chinese_mean;
    }

    public void setEnglish_mean(String english_mean) {
        this.english_mean = english_mean;
    }

    public void setChinese_mean(String chinese_mean) {
        this.chinese_mean = chinese_mean;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", english_mean='" + english_mean + '\'' +
                ", chinese_mean='" + chinese_mean + '\'' +
                ", is_visitable=" + is_visitable +
                '}';
    }

}
