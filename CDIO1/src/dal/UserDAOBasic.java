package dal;

import java.util.*;

import dto.UserDTO;
import persistency.IPersistency;

public class UserDAOBasic implements IUserDAO {

	private IPersistency persistencyManager;
	private Set<Integer> allowedIDs = new HashSet<>();

	public UserDAOBasic(IPersistency persistencyManager) {
		this.persistencyManager = persistencyManager;
		for (int i = 0; i < 99; i++) {
			allowedIDs.add(i);
		}
	}

	@Override
	public UserDTO getUser(int userId) throws DALException {
		UserDTO user = persistencyManager.getUser(userId);
		if (user == null) {
			//Todo: BAD NULL!!
			throw new DALException("Invalid ID");
		}
		return user;
	}

	@Override
	public List<UserDTO> getUserList() throws DALException {
		List<UserDTO> userList = persistencyManager.getUserList();
		if (userList.size() == 0) {
			//Todo: New exception
			throw new DALException("Userlist is empty");
		}
		return userList;
	}

	@Override
	public UserDTO createUser() throws DALException {
		UserDTO user = new UserDTO();
		//Set userID to smallest available ID
		//Does NOT validate ID - assumes getAvailableIDs returns valid IDs only
		getAvailableIDs().stream().min(Integer::compareTo).ifPresent(user::setUserId);
		//Initial password - likely to be changed during creation
		setPwd(user);
		return user;
	}

	@Override
	public boolean setID(UserDTO user, int id) throws DALException {
		//TODO: Validate
		user.setUserId(id);
		return false;
	}

	@Override
	public boolean setName(UserDTO user, String name) throws DALException {
		//TODO: Validate!
		user.setUserName(name);
		return false;
	}

	@Override
	public boolean setInitials(UserDTO user, String initials) throws DALException {
		//TODO: Validate!
		user.setIni(initials);
		return false;
	}

	@Override
	public boolean setCpr(UserDTO user, String cpr) throws DALException {
		//TODO: Validate!
		user.setCpr(cpr);
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user, String pwd) throws DALException {
		//TODO: Validate!
		user.setPassword(pwd);
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user) throws DALException {
		String pwd = Password.makePassword(8);
		user.setPassword(pwd);
		return true;
	}

	@Override
	public boolean deleteUser(int userId) throws DALException {
		if (!persistencyManager.deleteUser(userId)) {
			//TODO: New exception
			throw new DALException("Unable to delete user with ID " + userId);
		}
		return true;
	}

	@Override
	public Set<Integer> getAvailableIDs() throws DALException {
		Set<Integer> usedIDs = persistencyManager.getUserIDList();
		Set<Integer> availableIDs = new HashSet<>(allowedIDs);
		availableIDs.removeAll(usedIDs);
		if (availableIDs.size() == 0) {
			//TODO: New exception
			throw new DALException("All IDs are in use!");
		}
		return availableIDs;
	}

}
