package com.revature.daos;

import java.util.List;

import com.revature.models.Reimbursement;

public interface ReimbursementDao {
	
	ReimbursementDao currentImplementation = ReimbursementDaoSQL.instance;

	//Create new reimbursement
	int saveReimbursement(Reimbursement r);
	
	int findUserId(String username);
	
	//LIST ALL
	List<Reimbursement> findAll();
	
	//Find by Status
	List<Reimbursement> findPending();
	
	//View Past Tickets
	List<Reimbursement> findByUser(String username);
	
	//Change Status
	int changeRequestStatus(Reimbursement r);
		
}
