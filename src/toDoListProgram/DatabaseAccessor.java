package toDoListProgram;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.serial.SerialBlob;


public class DatabaseAccessor {
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	// Login to Database Information
	final private String host = "localhost:3306";
	final private String user = "root";
	final private String passwd = "secret";
	final private String tableName = "todolistprogram.users";
	
	// Statements:
	final private String checkUsername = "SELECT username FROM " + tableName + " WHERE ? = username";
	final private String checkLoginInfo = "SELECT password, salt FROM " + tableName + " WHERE ? = username";
	final private String createNewUser = "INSERT INTO " + tableName + " values (default, ?, ?, ?, ?, ?, ?, ?, default)";
	final private String getSecurityQuestion = "SELECT securityquestion FROM " + tableName + " WHERE ? = username";
	final private String checkSecurityAnswer = "SELECT securityresponse, salt FROM " + tableName + " WHERE ? = username";
	final private String setNewPassword = "UPDATE " + tableName + " SET password = ? WHERE username = ?";
	final private String getSalt = "SELECT salt FROM " + tableName + " WHERE ? = username";
	final private String getUserObj = "SELECT userobject FROM " + tableName + " WHERE ? = username";
	final private String setUserObj = "UPDATE " + tableName + " SET userobject = ? WHERE username = ?";
	final private String getFirstName = "SELECT firstname FROM " + tableName + " WHERE ? = username";
	
	// DatabaseAccessor Constructor
	public DatabaseAccessor() {
		conn = null;
		ps = null;
		rs = null;	
	}
	
	// Returns true if @param username exists, false if it doesn't
	public boolean usernameExists(String username) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(checkUsername);
			ps.setString(1, username);
			if(username == null) {
				return false;
			}
			rs = ps.executeQuery();
			if(sizeOfResultSet(rs) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	
	// Returns true if successfully creates the new user, false if unsuccessful
	public boolean createNewUser(String firstName, String lastName, String username, char[] password, String question, String response) throws Exception {
		try {
			open();
			String salt = PasswordUtils.getSalt(30);
			ps = conn.prepareStatement(createNewUser);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, username);
			ps.setString(4, PasswordUtils.generateSecurePassword(password, salt));
			ps.setString(5, salt);
			ps.setString(6, question);
			ps.setString(7,  PasswordUtils.generateSecurePassword(response.toCharArray(), salt));
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}
	
	public boolean setNewPassword(char[] newPassword, String username) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(getSalt);
			ps.setString(1,  username);
			rs = ps.executeQuery();
			String salt = "";
			while(rs.next()) {
				salt = rs.getString("salt");
			}
			ps.close();
			ps = conn.prepareStatement(setNewPassword);
			ps.setString(1, PasswordUtils.generateSecurePassword(newPassword, salt));
			ps.setString(2,  username);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}
	
	// Returns true if @param password is the correct password for @param username, false if user doesn't exist or password is incorrect
	public boolean checkUsernamePassword(String username, char[] password) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(checkLoginInfo);
			ps.setString(1, username);
			rs = ps.executeQuery();
			String securePassword = "";
			String salt = "";
			while(rs.next()) {
				securePassword = rs.getString("password");
				salt = rs.getString("salt");
			}
			if(!securePassword.equals("") && PasswordUtils.verifyUserPassword(password, securePassword, salt)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	
	// Return the security question from the database
	public String getSecurityQuestion(String username) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(getSecurityQuestion);
			ps.setString(1, username);
			rs = ps.executeQuery();
			String securityQuestion = "";
			while(rs.next()) {
				securityQuestion = rs.getString("securityquestion");
			}
			return securityQuestion;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}

	public boolean checkSecurityAnswer(String securityResponse, String username) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(checkSecurityAnswer);
			ps.setString(1, username);
			rs = ps.executeQuery();
			String secureResponse = "";
			String salt = "";
			while(rs.next()) {
				secureResponse = rs.getString("securityresponse");
				salt = rs.getString("salt");
			}
			if(!secureResponse.equals("") && PasswordUtils.verifyUserPassword(securityResponse.toCharArray(), secureResponse, salt)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	
	public String getFirstName(String username) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(getFirstName);
			ps.setString(1, username);
			rs = ps.executeQuery();
			String firstname = "";
			while(rs.next()) {
				firstname = rs.getString("firstname");
			}
			return firstname;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	
	// DESERIALIZING AND GETTING THE OBJECT
	public User getUserObject(String username) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(getUserObj);
			ps.setString(1,  username);
			rs = ps.executeQuery();
			Blob userobj = null;
			while(rs.next()) {
				userobj = rs.getBlob("userobject");
			}
			if(userobj == null) {
				return null;
			} else {
				return User.deserialize(userobj.getBinaryStream());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	
	// SERIALIZING AND SETTING THE BLOB OBJECT
	public void setUserObj(String username, byte[] serializedObj) throws Exception {
		try {
			open();
			ps = conn.prepareStatement(setUserObj);
			ps.setBlob(1, (Blob)new SerialBlob(serializedObj));
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	
	
	// Example: Possibly useful method
	private int sizeOfResultSet(ResultSet _resultSet) throws SQLException {
		int count = 0;
		while(_resultSet.next()) {
			count++;
		}
		return count;
	}
	
	// HOW TO OPEN MYSQL CONNECTION
	private void open(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + host + "/todolistprogram", user, passwd);
		} catch (Exception e) {
			
		}
	}
	
	// HOW TO CLOSE MYSQL CONNECTION
	private void close() {
		try {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			
		}
	}
	
}

