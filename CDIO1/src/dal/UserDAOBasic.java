package dal;

import java.util.*;
import java.util.regex.Pattern;

import dto.UserDTO;
import persistency.IPersistency;

public class UserDAOBasic implements IUserDAO {

	private IPersistency persistencyManager;
	private Set<Integer> allowedIDs = new HashSet<>();

	public UserDAOBasic(IPersistency persistencyManager) {
		createRuleSet();
		this.persistencyManager = persistencyManager;
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
		if (ruleList.get("id").test(id)) {
			user.setUserId(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean setName(UserDTO user, String name) throws DALException {
		if (ruleList.get("name").test(name)) {
			user.setUserName(name);
			return true;
		}
		return false;
	}

	@Override
	public boolean setInitials(UserDTO user, String initials) throws DALException {
		if (ruleList.get("init").test(initials)) {
			user.setIni(initials);
			return true;
		}
		return false;
	}

	@Override
	public boolean setCpr(UserDTO user, String cpr) throws DALException {
		if (ruleList.get("cpr").test(cpr)) {
			user.setCpr(cpr);
			return true;
		}
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user, String pwd) throws DALException {
		if (ruleList.get("pwd").test(pwd)) {
			user.setPassword(pwd);
			return true;
		}
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user) throws DALException {
		String pwd = "";
		while (!ruleList.get("pwd").test(pwd)) {
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
	    if (ruleList.get("role").test(tmp)) {
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
                if (ruleList.get("role").test(used)) {
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

	public String getRequirement(String method) {
		return ruleList.get(method).toString();
    }


    private void createRuleSet() {
		int minID = 11, maxID = 99;
		int minName = 2, maxName = 20;
		int minIni = 2, maxIni = 3;
		int minPwd = 6, minPwdReq = 3;
		String[][] exclusiveRoles = new String[][] {{
		   "Pharmacist", "Foreman",
        }};
		Rule idRule = new Rule<Integer>
				("ID must be between" + minID + " and " + maxID
						, t -> t > minID && t < maxID);
		Rule nameRule = new Rule<String>
				("Name must be between " + minName + " and " + maxName + " characters"
						, t -> t.length() >= minName && t.length() <= maxName);
		Rule iniRule = new Rule<String>
				("Initials must be between " + minIni + " and " + maxIni + " characters"
						, t -> t.length() >= minIni && t.length() <= maxIni);
		Rule cprRule = new Rule<String>
				("CPR must be entered as 'xxxxxx-yyyy' or 'xxxxxxyyyy'"
						, t -> Pattern.matches("[0-9]{6}-?[0-9]{4}", t));
		Rule pwdRule = new Rule<String>
				("Password must be at least " + minPwd + " long and contain at least "
						+ minPwdReq + " of these categories:\n" +
						"* Lowercase letters\n" +
						"* Uppercase letters\n" +
						"* Numbers\n" +
						"* Special characters (Use only\". - _ + ! ? =\")"
						, t -> {
					int hasSize = t.length() >= minPwd ? 1 : 0;
					int hasLowerCase = t.matches(".*[a-zæøå]+.*") ? 1 : 0;
					int hasUpper = t.matches(".*[A-ZÆØÅ]+.*") ? 1 : 0;
					int hasNumber = t.matches(".*[0-9]+.*") ? 1 : 0;
					int hasSpecial = t.matches(".*[.-_+!?=]+.*") ? 1 : 0;
					boolean hasIllegal = !t.matches("[a-zæøåA-ZÆØÅ0-9.-_+!?=]*");
					if (hasIllegal) {
						return false;
					}
					return ((hasSize + hasLowerCase + hasUpper + hasNumber + hasSpecial) >= minPwdReq);
				}
				);
		String roleRuleString = "Following roles cannot be assigned at the same time:";
		for (int i = 0; i < exclusiveRoles.length; i++) {
		    roleRuleString += "Exclusive Group " + i + "\n";
		    for (int j = 0; j < exclusiveRoles[i].length; j++) {
		        roleRuleString += "\t\"" + exclusiveRoles[i][j] + "\"\n";
            }
        }
		Rule roleRule = new Rule<Set<String>>(
                roleRuleString
                , t -> {
		            for (int i = 0; i < exclusiveRoles.length; i++) {
		                int count = 0;
		                for (int j = 0; j < exclusiveRoles[i].length; j++) {
		                    if (t.contains(exclusiveRoles[i][j])) {
		                        count++;
                            }
                        }
                        if (count > 1) {
		                    return false;
                        }
                    }
                    return true;
        });
		ruleList.put("id", idRule);
		ruleList.put("name", nameRule);
		ruleList.put("init", iniRule);
		ruleList.put("cpr", cprRule);
		ruleList.put("pwd", pwdRule);
		ruleList.put("role", roleRule);
	}


}
