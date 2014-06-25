import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyDbConnecton {
	private Connection link = null;
	private String url;
	private String user;
	private String pass;

	public MyDbConnecton(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	 public void connect() {
		 try{
			link = DriverManager.getConnection(url, user, pass);
		    if(link == null){
		    	System.out.println("MySQL is not started!!!");
		    }
		    else{
		    	System.out.println("You are connected to MySQL!!!");
		    }
		 }catch(SQLException e){
		      e.printStackTrace();
		    }
	 return;
	 }
	 
	  public void closeConnection() {
		try {
			if(link != null) { 
				link.close();
				System.out.println("Connection to MySQL is closed!!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
	  
	  public int checkUsername(String username) {
		  int usernameExist = 0;
		  try {
			  CallableStatement doesExist = 
				         link.prepareCall("{CALL check_if_username_exist(?)}");
			  doesExist.setString(1, username);
		      ResultSet rs = doesExist.executeQuery();
		      while (rs.next())
		    	  usernameExist = rs.getInt(1);
		  }catch(SQLException e) {
			  e.printStackTrace();
	      }
		  return usernameExist;
	  }
	  
	  public String getPassword(String username) {
		  String password = null;
		  try {
			  CallableStatement passwordSt = 
					  link.prepareCall("{CALL get_password(?)}");
			  passwordSt.setString(1, username);
			  ResultSet rs = passwordSt.executeQuery();
			  while(rs.next())
				  password = rs.getString(1);
		  } catch(SQLException e) {
			  e.printStackTrace();
	      }
		  return password;
	  }
	  
	  
	  public String getMessages(String username) {
		  String messages = "";
		  try {
			  CallableStatement messagesSt = 
					  link.prepareCall("{CALL get_message(?)}");
			  messagesSt.setString(1, username);
			  ResultSet rs = messagesSt.executeQuery();
			  while(rs.next()) {
				  messages += rs.getString(1) + ": " + rs.getString(2) + "\n";
			}
		  } catch(SQLException e) {
			  e.printStackTrace();
	      }
		  return messages;
	  }
	  
	  public int getUserId(String username) {
		  ResultSet rs = null;
		  int id = 0;
		  try {
			  CallableStatement messagesSt = 
					  link.prepareCall("{CALL get_id(?)}");
			  messagesSt.setString(1, username);
			  rs = messagesSt.executeQuery();
			  int i = 0;
			  while(rs.next()) {
				 id = rs.getInt(1);
			  }
		  } catch(SQLException e) {
			  e.printStackTrace();
	      }
		  return id;
	  }
	  
	  public void setMessage(String username,String message) {
		  try {
			  CallableStatement messagesSt = 
					  link.prepareCall("{CALL set_message(?,?)}");
			  messagesSt.setInt(1, getUserId(username));
			  messagesSt.setString(2, message);
			  messagesSt.executeQuery();
		  } catch(SQLException e) {
			  e.printStackTrace();
	      }
	  }
	  
	  public void setAccount(String username, String password) {
		  try {
			  CallableStatement messagesSt = 
					  link.prepareCall("{CALL set_account(?,?)}");
			  messagesSt.setString(1, username);
			  messagesSt.setString(2, password);
			  messagesSt.executeQuery();
		  } catch(SQLException e) {
			  e.printStackTrace();
	      }
	  }
	  
	  public void setFollowings(String username, String followings_id) {
		  try {
			  CallableStatement messagesSt = 
					  link.prepareCall("{CALL set_followings(?,?)}");
			  messagesSt.setInt(1, getUserId(username));
			  messagesSt.setInt(2, getUserId(followings_id));
			  messagesSt.executeQuery();
		  } catch(SQLException e) {
			 System.out.println("followings alredy added!!!");
	      }
	  }
	  
	  public ArrayList getUsernames() {
		  ArrayList usernames = new ArrayList();
		  try {
			  CallableStatement messagesSt = 
					  link.prepareCall("{CALL get_usernames()}");
			  ResultSet rs = messagesSt.executeQuery();
			  while(rs.next()) {
				  usernames.add(rs.getString(1));
			  }
		  } catch(SQLException e) {
			  e.printStackTrace();
	      }
		  return usernames;
	  }
	  
}
