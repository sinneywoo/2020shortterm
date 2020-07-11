package com.promise.memo.DB.roomdb.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordBean {
    @PrimaryKey(autoGenerate = true)
    Long id;
    String typeName;//分类名字
    String typepin;//分类名字拼音 用来匹配寻找图片
    int money;
    String createDate;
    String type;//1 收入 0 支出
    String month;//月份
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypepin() {
        return typepin;
    }

    public void setTypepin(String typepin) {
        this.typepin = typepin;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
