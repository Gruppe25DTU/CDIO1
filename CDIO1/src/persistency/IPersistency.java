package persistency;

import dto.UserDTO;

public interface IPersistency {

	UserDTO load(int usesrID);	
	boolean save(UserDTO user);	
	void updateUser(UserDTO user, int userID);
	
	
}
