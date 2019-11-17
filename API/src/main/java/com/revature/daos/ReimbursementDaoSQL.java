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

	// EXTRACT REIMBURSEMENT
	Reimbursement extractReimbursement(ResultSet rs) throws SQLException {
		int rsReimbId = rs.getInt("reimb_id");
		double rsReimbAmount = rs.getDouble("reimb_amount");
		long rsReimbSubmitted = rs.getLong("reimb_submitted");
		long rsReimbResolved = rs.getLong("reimb_resolved");
		String rsReimbDescription = rs.getString("reimb_description");
		System.out.println("Inside Extract Reimbursemnt");
		String rsReimbAuthor = findUsername(rs.getInt("reimb_author"));
		System.out.println("reimb author is: " + rsReimbAuthor);
		String rsReimbResolver = findUsername(rs.getInt("reimb_resolver"));
		System.out.println("reimb resolver is: " + rsReimbResolver);
		String rsReimbStatus = findStatus(rs.getInt("reimb_status_id"));
		String rsReimbType = findType(rs.getInt("reimb_type_id"));
		return new Reimbursement(rsReimbId, rsReimbAmount, rsReimbSubmitted, rsReimbResolved, rsReimbDescription, null,
				rsReimbAuthor, rsReimbResolver, rsReimbStatus, rsReimbType);
	}

	// EXTRACT USERID BASED ON USERNAME
	public int findUserId(String username) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT ers_users_id as userId FROM ers_users WHERE ers_username = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			System.out.println("Inside findUserId");
			rs.next();
			int uId = rs.getInt("userId");
			System.out.println(uId);
			return uId;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
	}

	// INVERSE
	// EXTRACT USERNAME BASED ON ID
	public String findUsername(int id) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT ers_username as username FROM ers_users WHERE ers_users_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			System.out.println("Inside findUsername X");
			rs.next();
			String username = rs.getString("username");
			System.out.println("Username X:" + username);
			return username;
		} catch (SQLException e) {
			System.out.println("EMPTY Name");
			// e.printStackTrace();
			// System.out.println(e.getMessage());
			return null;
		}
	}

	// GET STATUSID BASED ON STATUS
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

	// INVERSE
	// GET STATUS BASED ON STATUSID
	public String findStatus(int statusId) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT reimb_status FROM ers_reimbursement_status WHERE reimb_status_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, statusId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString("reimb_status");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}

	// GET TYPEID BASED ON TYPE
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

	// INVERSE
	// GET TYPE BASED ON TYPEID
	public String findType(int typeId) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT reimb_type FROM ers_reimbursement_type WHERE reimb_type_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, typeId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString("reimb_type");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public int saveReimbursement(Reimbursement r) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = ("INSERT INTO ERS_REIMBURSEMENT (reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id)"
					+ "VALUES (reimb_id_seq.nextval, ?, ?, ?, ?, null, ?, null, ?, ?)");

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, r.getReimb_amount());
			ps.setLong(2, (System.currentTimeMillis()));
			ps.setLong(3, 0);
			ps.setString(4, r.getReimb_description());
			ps.setInt(5, findUserId(r.getReimb_author()));
			ps.setInt(6, 1);
			ps.setInt(7, findTypeId(r.getReimb_type()));
			return ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Reimbursement> findAll() {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT er.reimb_id, er.reimb_amount,er.reimb_submitted,er.reimb_resolved,er.reimb_description,er.reimb_author, er.reimb_resolver, er.reimb_status_id, er.reimb_type_id FROM ers_reimbursement er LEFT JOIN ers_users eu ON eu.ers_users_id = er.reimb_author LEFT JOIN ers_users eur ON eur.ers_users_id = er.reimb_resolver LEFT JOIN ers_reimbursement_status ers ON ers.reimb_status_id = er.reimb_status_id LEFT JOIN ers_reimbursement_type ert ON ert.reimb_type_id = er.reimb_type_id";
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
	public List<Reimbursement> findByUser(String username) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			int authorId = findUserId(username);
			System.out.println(authorId);
			ps.setInt(1, authorId);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> userReimbursements = new ArrayList<Reimbursement>();
			System.out.println("Inside Find By User");
			while (rs.next()) {
				userReimbursements.add(extractReimbursement(rs));
			}
			return userReimbursements;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int changeRequestStatus(Reimbursement r) {
		System.out.println("\nChange Request Status Initiated :: \n");
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "UPDATE ers_reimbursement SET reimb_status_id = ?, reimb_resolver = ?, reimb_resolved = ? WHERE reimb_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, findStatusId(r.getReimb_status()));
			ps.setInt(2, findUserId(r.getReimb_resolver()));
			ps.setLong(3, System.currentTimeMillis());
			ps.setInt(4, r.getReimb_id());
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}

//BONUS:
//ResultSetMetaData rsmd = rs.getMetaData();
//String name = rsmd.getColumnName(1);
//System.out.println(name);
