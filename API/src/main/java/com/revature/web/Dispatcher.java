package com.revature.web;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.ReimbursementDao;
import com.revature.models.Reimbursement;

public class Dispatcher {

	private static ReimbursementDao reimbursementDao = ReimbursementDao.currentImplementation;

	private static ObjectMapper mapper = new ObjectMapper();

	private static final String REIMBURSEMENTS_URI = "/ExpenseReimbursement/api";

	private Dispatcher() {

	}

	public static Object dispatch(HttpServletRequest request, HttpServletResponse response) {
		if (isGet(request)) {
			if (request.getRequestURI().startsWith(REIMBURSEMENTS_URI)) {
				String[] path = request.getRequestURI().split("/");
				System.out.println(Arrays.deepToString(path));
				if (path.length == 3) {
					return reimbursementDao.findAll();
				} else if (path.length == 4) {
					String username = String.valueOf(path[3]);
					System.out.println("Username Selected is: " + username);
					return reimbursementDao.findByUser(username);
				}
			}
		} else if (isPost(request)) {
			if (request.getRequestURI().equals(REIMBURSEMENTS_URI)) {
				try {
					Reimbursement reimbursement = mapper.readValue(request.getInputStream(), Reimbursement.class);
					return reimbursementDao.saveReimbursement(reimbursement);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (isPut(request)) {
			if (request.getRequestURI().equals(REIMBURSEMENTS_URI)) {
				try {
					Reimbursement reimbursement = mapper.readValue(request.getInputStream(), Reimbursement.class);
					return reimbursementDao.changeRequestStatus(reimbursement);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static boolean isGet(HttpServletRequest request) {
		return request.getMethod().equals("GET");
	}

	private static boolean isPost(HttpServletRequest request) {
		return request.getMethod().equals("POST");
	}

	private static boolean isPut(HttpServletRequest request) {
		return request.getMethod().equals("PUT");
	}

}
