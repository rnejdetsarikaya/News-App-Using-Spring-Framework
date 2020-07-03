package com.yazlab.dao;

import java.util.List;

import com.yazlab.model.News;

public interface NewsDao {

	public News create(News news);
	
	public News search(int id);

	public void update(int id, News book);
	
	public News delete(int id);

	public List<News> list();
	
	public int updateLikeCount(int id,int mode);
	
	public void updatedisLikeCount(int id,int mode);

	public void updateViewCount(int id);
	
	public List<String> getTypes();
	
	public List<Integer> getStates(int id,String deviceId);
	
	public void setStates(int id,int likeState,int viewState,String deviceId);
	
	public String getNotificationState();
	
}
