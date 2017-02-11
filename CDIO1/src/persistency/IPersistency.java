package persistency;

import dto.UserDTO;

public interface IPersistency {

	UserDTO load(int userID);	
	boolean save(UserDTO user);
	void updateUser(UserDTO user, int i);


}
