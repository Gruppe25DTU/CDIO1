package sql;

import java.util.ArrayList;

import dto.UserDTO;

public interface IPersistency {

	boolean save(ArrayList<UserDTO> list);
	ArrayList<UserDTO> load();
	void opdateUser(UserDTO user, );
	
	
}
