package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable{

	private static final long serialVersionUID = 4545864587995944260L;
	private int	userID;                     
	private String userName;                
	private String ini;                 
	private List<String> roles;
	private String cpr;
	private String password;
	//TODO Add relevant fields
	
	public UserDTO() {
		this.roles = new ArrayList<>();
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserId(int userId) {
		this.userID = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIni() {
		return ini;
	}
	public void setIni(String ini) {
		this.ini = ini;
	}

	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void addRole(String role){
		this.roles.add(role);
	}
	/**
	 * 
	 * @param role
	 * @return true if role existed, false if not
	 */
	public boolean removeRole(String role){
		return this.roles.remove(role);
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userID + ", userName=" + userName + ", ini=" + ini + ", roles=" + roles + "]";
	}
	
}	
	
