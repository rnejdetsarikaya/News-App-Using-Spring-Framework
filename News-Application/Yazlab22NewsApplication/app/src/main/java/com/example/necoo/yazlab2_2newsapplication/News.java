package com.example.necoo.yazlab2_2newsapplication;

/**
 * Created by NECOO on 3.04.2019.
 */

public class News {

    private String imageUrl;
    private String newsTitle;
    private String text;
    private String type;
    private String date;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private int newsId;



    public News(String imageUrl, String newsTitle, String text, String type, String date) {

        this.imageUrl = imageUrl;
        this.newsTitle = newsTitle;
        this.text = text;
        this.type = type;
        this.date = date;
        this.likeCount = 0;
        this.dislikeCount = 0;
        this.viewCount = 0;
    }

    public News(){

    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    @Override
    public String toString(){
        String str = "newsId: " + newsId + "\n" +  "newsTitle: " + newsTitle+ "\n" +
                     "text: " + text + "\n"  + "type: " + type + "\n" +
                     "imageUrl: " + imageUrl +"\n" + "date: " +date + "\n" +
                     "likeCount: " + likeCount +"\n" + "dislikeCount: " + dislikeCount + "\n" +
                     "viewCount: " + viewCount;
        return  str;
    }


}
