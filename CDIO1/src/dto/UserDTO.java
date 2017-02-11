package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable{

	private static final long serialVersionUID = 4545864587995944260L;
	private int	userID;                     
	private String userName;                
	private String ini;                 
	private String cpr;
	private String password;
	private ArrayList<String> roles;

	//TODO Add relevant fields
	
	public UserDTO(int userID, String userName, String ini, String cpr, String password, ArrayList<String> roles) 
	{	
		this.userID = userID;
		this.userName = userName;
		this.ini = ini;
		this.cpr = cpr;
		this.password = password;
		this.roles = new ArrayList<String>(roles);
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
	public void setRoles(ArrayList<String> roles) {
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
	
	public String getCpr() {
		return cpr;
	}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "UserDTO [userID=" + userID + ", userName=" + userName + ", ini=" + ini + ", cpr=" + cpr + ", password="
				+ password + ", roles=" + roles + "]";
	}

	

	

	
	
}	
	
