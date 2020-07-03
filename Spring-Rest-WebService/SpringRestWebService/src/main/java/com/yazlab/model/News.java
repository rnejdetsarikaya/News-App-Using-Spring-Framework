package com.yazlab.model;

import java.awt.Image;
import java.util.Date;

public class News {

	private String imageUrl;
	private String newsTitle;
	private String text;
	private String type;
	private Date date;
	private int likeCount;
	private int dislikeCount;
	private int viewCount;
	private int newsId;
	
	
	
	public News(String imageUrl, String newsTitle, String text, String type, Date date) {

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
	/**
	 * @return the image
	 */
	public String getImage() {
		return imageUrl;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the newsTitle
	 */
	public String getNewsTitle() {
		return newsTitle;
	}
	/**
	 * @param newsTitle the newsTitle to set
	 */
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	/**
	 * @return the news
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param news the news to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the likeCount
	 */
	public int getLikeCount() {
		return likeCount;
	}
	/**
	 * @param likeCount the likeCount to set
	 */
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	/**
	 * @return the dislikeCount
	 */
	public int getDislikeCount() {
		return dislikeCount;
	}
	/**
	 * @param dislikeCount the dislikeCount to set
	 */
	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}
	/**
	 * @return the viewCount
	 */
	public int getViewCount() {
		return viewCount;
	}
	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	/**
	 * @return the newsId
	 */
	public int getNewsId() {
		return newsId;
	}

	/**
	 * @param newsId the newsId to set
	 */
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}


	
}
