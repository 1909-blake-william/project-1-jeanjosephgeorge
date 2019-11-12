package com.revature.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.ReimbursementDao;
import com.revature.models.Reimbursement;

public class Reimbursements extends HttpServlet {
	
	private ReimbursementDao reimbursementDao = ReimbursementDao.currentImplementation;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers",
				"Origin, Methods, Credentials, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.setContentType("application/json");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Request received with URI:" + req.getRequestURI());
		List<Reimbursement> reimbursements;
		
		reimbursements = reimbursementDao.findAll();
		
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(reimbursements);
		
		resp.addHeader("content-type", "application/json");
		resp.getWriter().write(json);
	}
}
