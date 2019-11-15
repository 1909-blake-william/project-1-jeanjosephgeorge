package com.revature.web;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DispatcherServlet extends HttpServlet {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
		System.out.println("To context param: " + req.getServletContext().getInitParameter("To"));

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
		System.out.println("Inside DispatcherServlet");
		if (response != null) {
			// Write the JSON Response and return
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
			} catch (IOException e) {
				// The Collections.singletonMap method I use here is a quick way to marshal a
				// JSON object
				// will return something like { "error": "Failed to write Todo as JSON" }
				resp.getOutputStream().write(mapper
						.writeValueAsBytes(Collections.singletonMap("error", "Failed to write List of Todos as JSON")));
			}
		} else {
			// Return some 4XX error
		}
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");

		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			// Write the JSON Response and return
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
			} catch (IOException e) {
				// The Collections.singletonMap method I use here is a quick way to marshal a JSON object
				// will return something like { "error": "Failed to write Todo as JSON" }
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write Todo as JSON")));
			}
		} else {
			// Return some 4XX error
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");

		Object response = Dispatcher.dispatch(req, resp);
		if (response != null) {
			// Write the JSON Response and return
			try {
				resp.getOutputStream().write(mapper.writeValueAsBytes(response));
			} catch (IOException e) {
				// The Collections.singletonMap method I use here is a quick way to marshal a JSON object
				// will return something like { "error": "Failed to write Todo as JSON" }
				resp.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("error", "Failed to write Todo as JSON")));
			}
		} else {
			// Return some 4XX error
		}
	}

}
