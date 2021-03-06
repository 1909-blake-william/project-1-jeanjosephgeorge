package com.revature.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.UserDao;
import com.revature.models.User;

public class AuthServlet extends HttpServlet {

	ObjectMapper om = new ObjectMapper();
	UserDao userDao = UserDao.currentImplementation;

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		System.out.println(req.getRequestURL());
		resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers",
				"Origin, Methods, Credentials, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.setContentType("application/json");
		// TODO Auto-generated method stub
		super.service(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("uri = " + req.getRequestURI());
		if ("/ExpenseReimbursement/login".equals(req.getRequestURI())) {

			User credentials = (User) om.readValue(req.getReader(), User.class);
			String username = credentials.getUsername();
			String password = credentials.getPassword();
			User loggedInUser = userDao.findByUsernameAndPassword(username, password);
			
			if (loggedInUser == null) {
				resp.setStatus(401); // Unauthorized status code
				return;
			} else {
				resp.setStatus(201);
				req.getSession(true).setAttribute("user", loggedInUser);
				resp.getWriter().write(om.writeValueAsString(loggedInUser));
				System.out.println(om.writeValueAsString(loggedInUser));
				resp.setStatus(201);
				return;
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ("/ExpenseReimbursement/login".equals(req.getRequestURI())) {
			String json = om.writeValueAsString(req.getSession(false).getAttribute("user"));
			resp.addHeader("content-type", "application/json");
			resp.getWriter().write(json);
		}
	}
}
