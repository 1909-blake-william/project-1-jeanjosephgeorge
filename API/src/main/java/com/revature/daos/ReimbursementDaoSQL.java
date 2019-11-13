package com.revature.daos;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.util.ConnectionUtil;

public class ReimbursementDaoSQL implements ReimbursementDao {

	// Singleton instance of the ReimburseDaoSQL
	public static final ReimbursementDaoSQL instance = new ReimbursementDaoSQL();

	// Private Constructor
	private ReimbursementDaoSQL() {
	};

	//EXTRACT REIMBURSEMENT
	Reimbursement extractReimbursement(ResultSet rs) throws SQLException {
		int rsReimbId = rs.getInt("reimb_id");
		double rsReimbAmount = rs.getDouble("reimb_amount");
		long rsReimbSubmitted = rs.getLong("reimb_submitted");
		long rsReimbResolved = rs.getLong("reimb_resolved");
		String rsReimbDescription = rs.getString("reimb_description");
		//String rsReimbReceipt = rs.getString("reimb_receipt"); 
		String rsReimbAuthor = rs.getString("ers_username");
		String rsReimbResolver = rs.getString("boss");
		String rsReimbStatus = rs.getString("reimb_status");
		String rsReimbType = rs.getString("reimb_type");
		return new Reimbursement(rsReimbId, rsReimbAmount, rsReimbSubmitted, rsReimbResolved, rsReimbDescription, null, rsReimbAuthor, rsReimbResolver, rsReimbStatus, rsReimbType);
	}

	//EXTRACT USERID BASED ON USERNAME
	public int findUserId(String username) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT ers_users_id FROM ers_users WHERE ers_username = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("ers_users_id");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
	}

	//GET STATUSID BASED ON STATUS
	public int findStatusId(String status) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT reimb_status_id FROM ers_reimbursement_status WHERE reimb_status = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("reimb_status_id");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	//GET TYPEID BASED ON TYPE
	public int findTypeId(String type) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT reimb_type_id FROM ers_reimbursement_type WHERE reimb_type = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("reimb_type_id");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	@Override
	public int saveReimbursement(Reimbursement r) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = ("INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)"
					+ "VALUES (reimb_id_seq.nextval, ?, ?, ?, ?, null, ?, ?, ?, ?)");

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, r.getReimb_amount());
			ps.setLong(2, r.getReimb_submitted());
			ps.setLong(3, r.getReimb_resolved());
			ps.setString(4, r.getReimb_description());
			//ps.setString(5, r.getReimb_receipt());
			ps.setInt(5, findUserId(r.getReimb_author()));
			ps.setInt(6, findUserId(r.getReimb_resolver()));
			ps.setInt(7, findStatusId(r.getReimb_status()));
			ps.setInt(8, findTypeId(r.getReimb_type()));
			return ps.executeUpdate();

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Reimbursement> findAll() {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT er.reimb_id, er.reimb_amount,er.reimb_submitted,er.reimb_resolved,er.reimb_description,eu.ers_username,eur.ers_username AS boss ,ers.reimb_status, ert.reimb_type FROM ers_reimbursement er LEFT JOIN ers_users eu ON eu.ers_users_id = er.reimb_author LEFT JOIN ers_users eur ON eur.ers_users_id = er.reimb_resolver LEFT JOIN ers_reimbursement_status ers ON ers.reimb_status_id = er.reimb_status_id LEFT JOIN ers_reimbursement_type ert ON ert.reimb_type_id = er.reimb_type_id";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
			
//			ResultSetMetaData rsmd = rs.getMetaData();
//			String name = rsmd.getColumnName(1);
//			System.out.println(name);
//
//			String name2 = rsmd.getColumnName(2);
//			System.out.println(name2);
//
//			String name3 = rsmd.getColumnName(3);
//			System.out.println(name3);
//
//			String name4 = rsmd.getColumnName(4);
//			System.out.println(name4);
//
//			String name5 = rsmd.getColumnName(5);
//			System.out.println(name5);
//
//			String name6 = rsmd.getColumnName(6);
//			System.out.println(name6);
//
//			String name7 = rsmd.getColumnName(7);
//			System.out.println(name7);
//
//			String name8 = rsmd.getColumnName(8);
//			System.out.println(name8);
//
//			String name9 = rsmd.getColumnName(9);
//			System.out.println(name9);			

			while (rs.next()) {
				reimbursements.add(extractReimbursement(rs));
			}
			return reimbursements;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Reimbursement> findPending() {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_reimbursement where reimb_status_id = 1;";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> pendingReimbursements = new ArrayList<Reimbursement>();
			while (rs.next()) {
				pendingReimbursements.add(extractReimbursement(rs));
			}
			return pendingReimbursements;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Reimbursement> findByUser(String username) {
		try(Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?"; 
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, findUserId(username));
			ResultSet rs = ps.executeQuery();
			
			List<Reimbursement> userReimbursements = new ArrayList<Reimbursement>();
			while(rs.next()) {
				userReimbursements.add(extractReimbursement(rs));
			}
			return userReimbursements;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void changeRequestStatus(int statusId, int reimbId) {
		try(Connection c = ConnectionUtil.getConnection()) {
			String sql = "UPDATE ers_reimbursement SET reimb_status_id = ? WHERE reimb_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, statusId);
			ps.setInt(2, reimbId);
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
