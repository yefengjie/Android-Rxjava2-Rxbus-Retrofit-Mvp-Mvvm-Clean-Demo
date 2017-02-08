package com.yefeng.androidarchitecturedemo.data.model.book;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yefeng on 06/02/2017.
 */
@Entity
public class Book {
    @Id
    private Long id;
    private String title;
    private String pic;
    @Generated(hash = 1387555794)
    public Book(Long id, String title, String pic) {
        this.id = id;
        this.title = title;
        this.pic = pic;
    }
    @Generated(hash = 1839243756)
    public Book() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPic() {
        return this.pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
