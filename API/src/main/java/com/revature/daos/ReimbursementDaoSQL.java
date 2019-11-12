package com.revature.daos;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	Reimbursement extractReimbursement(ResultSet rs) throws SQLException {
		int rsReimbId = rs.getInt("reimb_id");
		double rsReimbAmount = rs.getDouble("reimb_amount");
		long rsReimbSubmitted = rs.getLong("reimb_submitted");
		long rsReimbResolved = rs.getLong("reimb_resolved");
		String rsReimbDescription = rs.getString("reimb_description");
		//Blob rsReimbReceipt = rs.Blob("reimb_receipt"); -- CHANGED IT TO NULL
		int rsReimbAuthor = rs.getInt("reimb_author");
		int rsReimbResolver = rs.getInt("reimb_resolver");
		int rsReimbStatusId = rs.getInt("reimb_status_id");
		int rsReimbTypeId = rs.getInt("reimb_type_id");

		return new Reimbursement(rsReimbId, rsReimbAmount, rsReimbSubmitted, rsReimbResolved, rsReimbDescription,
				null, rsReimbAuthor, rsReimbResolver, rsReimbStatusId, rsReimbTypeId);
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
			ps.setInt(5, r.getReimb_author());
			ps.setInt(6, r.getReimb_resolver());
			ps.setInt(7, r.getReimb_status_id());
			ps.setInt(8, r.getReimb_type_id());
			return ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Reimbursement> findAll() {
		try (Connection c = ConnectionUtil.getConnection()) {
			System.out.println("Before SQL");
			String sql = "SELECT * FROM ers_reimbursement";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
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
	public List<Reimbursement> findByUser() {
		try(Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?"; 
			PreparedStatement ps = c.prepareStatement(sql);
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
