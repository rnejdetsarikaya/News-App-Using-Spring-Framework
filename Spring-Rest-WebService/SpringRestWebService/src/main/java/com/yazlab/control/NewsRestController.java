package com.yazlab.control;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yazlab.model.News;
import com.yazlab.service.NewsService;

@Controller
@ResponseBody
@RequestMapping("/news")
public class NewsRestController {

	@Autowired NewsService newsService;
	
	@GetMapping
	public List<News> getNews(){
		
		return newsService.list();
	}
	
	@GetMapping("/{id}")
	public News getNew(@PathVariable int id) {
		return newsService.search(id);
	}
	
	@GetMapping("/like/{id}/{mode}")
	public void setLikeCount(@PathVariable int id,@PathVariable int mode) {
		newsService.updateLikeCount(id,mode);
	}
	@GetMapping("/dislike/{id}/{mode}")
	public void setdisLikeCount(@PathVariable int id,@PathVariable int mode) {
		newsService.updatedisLikeCount(id,mode);
	}
	@GetMapping("/view/{id}")
	public void setViewCount(@PathVariable int id) {
		newsService.updateViewCount(id);
	}
	
	@GetMapping("/states/{id}/{deviceId}")
	public List<Integer> getStates(@PathVariable int id,@PathVariable String deviceId) {
		System.out.println("State çekme isteði");
		return newsService.getStates(id, deviceId);
	}
	
	@GetMapping("/states/{id}/{deviceId}/{viewState}/{likeState}")
	//int id,int likeState,int viewState,String deviceId
	public void setStates(@PathVariable int id,@PathVariable String deviceId,@PathVariable int viewState, @PathVariable int likeState) {
		System.out.println("State ekleme isteði");
		newsService.setStates(id, likeState, viewState, deviceId);
	}
	
	@GetMapping("/types")
	public List<String> getTypes() {
		return newsService.getTypes();
	}
	@GetMapping("/notificationstate")
	public String getNotificationState() {
		System.out.println("notification request");
		return newsService.getNotificationState();
	}
	@PostMapping
	public News saveNews(@RequestBody News news) {
		
		return newsService.create(news);
	}
//	@PostMapping("/{id}")
//	public Image saveNews(@PathVariable int id) throws IOException {
//		
//		File sourceimage = new File("C:\\Users\\NECOO\\Desktop\\Icons\\006-pencil.png");
//	Image image = ImageIO.read(sourceimage);
//	return image;
//	}
//	
	@PutMapping("/{id}")
	public News updateNews(@PathVariable int id, @RequestBody News news) {
	
		newsService.update(id, news);
		return news;
	}

	@DeleteMapping("/{id}")
	public void deleteNews(@PathVariable int id) {
		System.out.println("DELETEEEEE");
		newsService.delete(id);
	}
}
