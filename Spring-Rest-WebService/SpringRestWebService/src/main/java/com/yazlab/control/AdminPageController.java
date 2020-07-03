package com.yazlab.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yazlab.form.AdminPage;
import com.yazlab.model.News;
import com.yazlab.service.NewsService;

@Controller
@ResponseBody
@WebServlet("/admin")
public class AdminPageController extends HttpServlet {

	@Autowired
	NewsService newsService;
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			AdminPage frame = new AdminPage();
			frame.setFrame(frame);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("heyoooo");
	}
}
