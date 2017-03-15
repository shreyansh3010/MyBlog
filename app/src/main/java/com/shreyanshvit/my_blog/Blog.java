package com.shreyanshvit.my_blog;

/**
 * Created by SJ on 15/03/2017.
 */

public class Blog {
    private String desc;
    private String image;
    private String title;

    public Blog(){

    }

    public Blog(String title, String desc, String image){

        this.title = title;
        this.desc = desc;
        this.image = image;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
