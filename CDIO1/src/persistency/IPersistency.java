package persistency;

import java.util.ArrayList;
import java.util.List;

import dto.UserDTO;

public interface IPersistency {
	
	void load();	
	boolean save(UserDTO user);
	void updateUser(UserDTO user, int i);
	UserDTO getUser(int id);
	ArrayList<UserDTO> getUserList();
	ArrayList<String> getUserIDList();
	void deleteUser(int userID);
	
}
