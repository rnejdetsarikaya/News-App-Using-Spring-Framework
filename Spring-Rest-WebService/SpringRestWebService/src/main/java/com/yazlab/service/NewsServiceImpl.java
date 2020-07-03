package com.yazlab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yazlab.dao.NewsDao;
import com.yazlab.model.News;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsDao newsdao;
	
	@Override
	public News create(News news) {
	
		return newsdao.create(news);
	}

	@Override
	public News search(int id) {

		return newsdao.search(id);
	}

	@Override
	public void update(int id, News book) {
		
		newsdao.update(id, book);
	}

	@Override
	public News delete(int id) {

		return newsdao.delete(id);
	}

	@Override
	public List<News> list() {

		return newsdao.list();
	}

	@Override
	public int updateLikeCount(int id,int mode) {
		return newsdao.updateLikeCount(id,mode);
	}

	@Override
	public void updatedisLikeCount(int id,int mode) {
		newsdao.updatedisLikeCount(id,mode);
	}

	@Override
	public void updateViewCount(int id) {
		newsdao.updateViewCount(id);
	}

	@Override
	public List<String> getTypes() {
		return newsdao.getTypes();
	}

	@Override
	public List<Integer> getStates(int id, String deviceId) {
		return newsdao.getStates(id, deviceId);
	}

	@Override
	public void setStates(int id, int likeState, int viewState, String deviceId) {
		newsdao.setStates(id, likeState, viewState, deviceId);
	}

	@Override
	public String getNotificationState() {
		return newsdao.getNotificationState();
	}

}
