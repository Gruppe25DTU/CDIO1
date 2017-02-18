package persistency;

import java.util.ArrayList;
import java.util.Set;

import dto.UserDTO;

public interface IPersistency {
	
	void load();	
	boolean save(UserDTO user);
	void updateUser(UserDTO user, int i);
	UserDTO getUser(int id);
	ArrayList<UserDTO> getUserList();
	Set<Integer> getUserIDList();
	boolean deleteUser(int userID);
	void quit();
	
}
