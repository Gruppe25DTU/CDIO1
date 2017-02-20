package dal;

import java.util.*;

import dto.UserDTO;
import persistency.IPersistency;

public class UserDAOBasic implements IUserDAO {

	private IRuleSet ruleSet;
	private IPersistency persistencyManager;
	private Set<Integer> allowedIDs = new HashSet<>();

	public UserDAOBasic(IPersistency persistencyManager) {
		ruleSet = new RuleSetBasic();
		this.persistencyManager = persistencyManager;
		//TODO: Fix hardcoded values!
		for (int i = 0; i < 99; i++) {
			allowedIDs.add(i);
		}
	}

	@Override
	public void quit() {
		persistencyManager.quit();
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
    public boolean saveUser(UserDTO user) throws DALException {
	    //TODO: Validate?
        persistencyManager.save(user);
        return false;
    }

    @Override
	public boolean setID(UserDTO user, int id) throws DALException {
		if (ruleSet.getIdReq().test(id)) {
			user.setUserId(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean setName(UserDTO user, String name) throws DALException {
		if (ruleSet.getNameReq().test(name)) {
			user.setUserName(name);
			return true;
		}
		return false;
	}

	@Override
	public boolean setInitials(UserDTO user, String initials) throws DALException {
		if (ruleSet.getIniReq().test(initials)) {
			user.setIni(initials);
			return true;
		}
		return false;
	}

	@Override
	public boolean setCpr(UserDTO user, String cpr) throws DALException {
		if (ruleSet.getCprReq().test(cpr)) {
			user.setCpr(cpr);
			return true;
		}
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user, String pwd) throws DALException {
		if (ruleSet.getPwdReq().test(pwd)) {
			user.setPassword(pwd);
			return true;
		}
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user) throws DALException {
		String pwd = "";
		while (!ruleSet.getPwdReq().test(pwd)) {
			pwd = Password.makePassword(6);
		}
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

	@Override
	public boolean addRole(UserDTO user, String role) {
	    Set<String> tmp = new HashSet<>(user.getRoles());
	    tmp.add(role);
	    if (ruleSet.getRoleReq().test(tmp)) {
	        user.setRoles(tmp);
	        return true;
        }
		return false;
	}

	@Override
    public HashSet<String> getAvailableRoles(UserDTO user) {
        HashSet<String> available = new HashSet<>();
        Set<String> used = new HashSet<>(user.getRoles());
        for (String role : roles) {
            if (used.add(role)) {
                if (ruleSet.getRoleReq().test(used)) {
                    available.add(role);
                }
                used.remove(role);
            }
        }
        return available;
    }

	@Override
	public void updateUser(UserDTO user, int originalID) {
		persistencyManager.updateUser(user, originalID);
	}

	@Override
	public IRuleSet.Rule getIdReq() {
		return ruleSet.getIdReq();
	}

	@Override
	public IRuleSet.Rule getNameReq() {
		return ruleSet.getNameReq();
	}

	@Override
	public IRuleSet.Rule getIniReq() {
		return ruleSet.getIniReq();
	}

	@Override
	public IRuleSet.Rule getCprReq() {
		return ruleSet.getCprReq();
	}

	@Override
	public IRuleSet.Rule getRoleReq() {
		return ruleSet.getRoleReq();
	}

	@Override
	public IRuleSet.Rule getPwdReq() {
		return ruleSet.getPwdReq();
	}


}
