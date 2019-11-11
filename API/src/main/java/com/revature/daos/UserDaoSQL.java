package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDaoSQL implements UserDao {

	User extractUser(ResultSet rs) throws SQLException {
		int rsId = rs.getInt("ers_users_id");
		String rsUsername = rs.getString("ers_username");
		String rsPassword = rs.getString("ers_password");
		String rsFirstName = rs.getString("user_first_name");
		String rsLastName = rs.getString("user_last_name");
		String rsUserEmail = rs.getString("user_email");
		int rsRoleId = rs.getInt("user_role_id");
		return new User(rsId, rsUsername, rsPassword, rsFirstName, rsLastName, rsUserEmail, rsRoleId);
	}

	public int save(User u) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO ers_users (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) "
					+ " VALUES (ers_users_id_seq.nextval ,?,?,?.?,?,?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getUser_first_name());
			ps.setString(4, u.getUser_last_name());
			ps.setString(5, u.getUser_email());
			ps.setInt(6, u.getUser_role_id());
			return ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<User> findAll() {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_users";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<User>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}
			return users;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public User findById(int id) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_users WHERE ers_users_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return extractUser(rs);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User findByUsername(String username) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_users WHERE ers_username = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return extractUser(rs);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public User findByUsernameAndPassword(String username, String password) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ers_users WHERE ers_username = ? and ers_password = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return extractUser(rs);
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
