package com.revature.web;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
		resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers",
				"Origin, Methods, Credentials, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.setContentType("application/json");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
				resp.setStatus(201);
			} catch (IOException e) {
				resp.getOutputStream().write(mapper
						.writeValueAsBytes(Collections.singletonMap("error", "Failed to write List of Todos as JSON")));
			}
		} else {
				resp.setStatus(400);
		}
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
				resp.setStatus(201);
			} catch (IOException e) {
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write Reimbursement as JSON")));
			}
		} else {
			resp.setStatus(400);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
				resp.setStatus(201);
			} catch (IOException e) {
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write Reimbursement as JSON")));
			}
		} else {
				resp.setStatus(400);
		}
	}

}
