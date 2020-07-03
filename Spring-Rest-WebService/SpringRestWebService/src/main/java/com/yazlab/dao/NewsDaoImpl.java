package com.yazlab.dao;

import java.lang.Thread.State;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.yazlab.config.AppConfig;
import com.yazlab.model.News;

@Repository
public class NewsDaoImpl implements NewsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public News create(News news) {
		String sql = "INSERT INTO news (newsId,newsTitle,newsImage,newsText,newsType,newsDate,liked,disliked,view)"
				+ " VALUES (?,?,?,?,?,?,?,?,?);";
		String sql1 = "UPDATE newsnotificationstate SET stateNumber = stateNumber+1 WHERE stateNumber >= 0";
		try {
			// jdbcTemplate = new JdbcTemplate(dataSource);
			if (jdbcTemplate == null)
				jdbcTemplate = new JdbcTemplate(new AppConfig().dataSource());
			
			jdbcTemplate.update(sql,
					new Object[] { news.getNewsId(), news.getNewsTitle(), news.getImage(), news.getText(),
							news.getType(), news.getDate(), news.getLikeCount(), news.getDislikeCount(),
							news.getViewCount() }); //news insert 
			jdbcTemplate.execute(sql1);//newsnotificationstate update
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("News created");
		return news;
	}

	@Override
	public News search(int id) {
		String sql = "SELECT * FROM news WHERE newsId='" + id + "'";
		try {
			// jdbcTemplate = new JdbcTemplate(dataSource);
			Map<String, Object> row = jdbcTemplate.queryForMap(sql);
			News n = new News();
			n.setNewsId(Integer.parseInt(String.valueOf(row.get("newsId"))));
			n.setNewsTitle((String) row.get("newsTitle"));
			n.setImage((String) row.get("newsImage"));
			n.setText((String) row.get("newsText"));
			n.setType((String) row.get("newsType"));

			SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			System.out.println(row.get("newsDate"));
			n.setDate(f.parse(f.format(row.get("newsDate"))));
			n.setLikeCount(Integer.parseInt(String.valueOf(row.get("liked"))));
			n.setDislikeCount(Integer.parseInt(String.valueOf(row.get("disliked"))));
			n.setViewCount(Integer.parseInt(String.valueOf(row.get("view"))));
			return n;
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		News n = new News("null", "null", "null", "null", new Date());
		return n;
	}

	@Override
	public void update(int id, News news) {
		this.delete(id);
		this.create(news);
	}

	@Override
	public News delete(int id) {

		String sql = "DELETE FROM news WHERE newsId= ?";
		try {
			jdbcTemplate.update(sql, new Object[] { id });
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}

	@Override
	public List<News> list() {
		String sql = "SELECT * FROM news";

		List<News> news = null;
		try {
//			context = new ClassPathXmlApplicationContext("spring.xml");
//			dataSource = (DataSource) context.getBean("dataSource");
			// jdbcTemplate = new JdbcTemplate(dataSource);
			if (jdbcTemplate == null)
				jdbcTemplate = new JdbcTemplate(new AppConfig().dataSource());

			news = new ArrayList<News>();
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				News n = new News();
				n.setNewsId(Integer.parseInt(String.valueOf(row.get("newsId"))));
				n.setNewsTitle((String) row.get("newsTitle"));
				n.setImage((String) row.get("newsImage"));
				n.setText((String) row.get("newsText"));
				n.setType((String) row.get("newsType"));

				SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				n.setDate(f.parse(f.format(row.get("newsDate"))));

				n.setLikeCount(Integer.parseInt(String.valueOf(row.get("liked"))));
				n.setDislikeCount(Integer.parseInt(String.valueOf(row.get("disliked"))));
				n.setViewCount(Integer.parseInt(String.valueOf(row.get("view"))));

				news.add(n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("All news here :)");
		return news;
	}

	@Override
	public int updateLikeCount(int id,int mod) {
		String sql = "SELECT * FROM news WHERE newsId='" + id + "'";
		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql);
			int like = Integer.parseInt(String.valueOf(row.get("liked")));
			System.out.println("like count"+like);
			String sql2 = "UPDATE news SET liked=? WHERE newsId= ?";
			
			if(mod == 0)
				jdbcTemplate.update(sql2, new Object[] {like+1, id });
			else if(mod == 1)
				jdbcTemplate.update(sql2, new Object[] {like-1, id });
			return like+1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	

	@Override
	public void updatedisLikeCount(int id,int mod) {
		String sql = "SELECT * FROM news WHERE newsId='" + id + "'";
		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql);
			int dislike = Integer.parseInt(String.valueOf(row.get("disliked")));
			System.out.println("dislike count"+dislike);
			String sql2 = "UPDATE news SET disliked=? WHERE newsId= ?";
			
			if(mod == 0)
				jdbcTemplate.update(sql2, new Object[] {dislike+1, id });
			else if(mod == 1)
				jdbcTemplate.update(sql2, new Object[] {dislike-1, id });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateViewCount(int id) {
		String sql = "SELECT * FROM news WHERE newsId='" + id + "'";
		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql);
			int view = Integer.parseInt(String.valueOf(row.get("view")));
			System.out.println("view count"+view);
			String sql2 = "UPDATE news SET view=? WHERE newsId= ?";
			
			jdbcTemplate.update(sql2, new Object[] {view+1, id });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getTypes() {
		List<String> list = new ArrayList<>();
		String sql = "SELECT newsType FROM news group by newsType";
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> map : rows) {
				list.add((String) map.get("newsType"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return list;
	}

	@Override
	public List<Integer> getStates(int id,String deviceId) {
		List<Integer> list = new ArrayList<>();
		String sql = "SELECT likeState,viewState FROM newsstateondevice where deviceId ='" + deviceId + "' and newsId ="+id+"";
		
		try {
			Map<String, Object> row = jdbcTemplate.queryForMap(sql);
			
			list.add(Integer.parseInt(String.valueOf(row.get("likeState"))));
			list.add(Integer.parseInt(String.valueOf(row.get("viewState"))));
			
		} catch (Exception e) {
			list.add(-1);
			list.add(-1);
		}
		return list;
	}

	@Override
	public void setStates(int id, int likeState, int viewState, String deviceId) {
		String sql = "INSERT INTO newsstateondevice (deviceId,newsId,likeState,viewState) "+
					 "VALUES (?,?,?,?)" + 
					 "ON DUPLICATE KEY UPDATE likeState=VALUES(likeState),viewState=VALUES(viewState)";
		try {
			jdbcTemplate.update(sql, new Object[] { deviceId,id,likeState,viewState});
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	@Override
	public String getNotificationState() {
		String sql ="SELECT stateNumber FROM newsnotificationstate";
		try {
			String stateNumber=jdbcTemplate.queryForObject(sql, String.class);
			return stateNumber;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "oops";
	}

}
