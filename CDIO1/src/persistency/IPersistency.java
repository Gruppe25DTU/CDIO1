package persistency;

import java.util.List;

import dto.UserDTO;

public interface IPersistency {
	
	UserDTO load(int userID);	
	boolean save(UserDTO user);
	void updateUser(UserDTO user, int i);
	UserDTO getUser(int id);
	List<UserDTO> getUserList();
	List<Integer> getUsedIds();
	
}
