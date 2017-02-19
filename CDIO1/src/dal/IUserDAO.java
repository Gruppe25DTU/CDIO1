package dal;


import java.util.*;
import java.util.function.Predicate;

import dto.UserDTO;

public interface IUserDAO {

	UserDTO getUser(int userId) throws DALException;
	List<UserDTO> getUserList() throws DALException;
	UserDTO createUser() throws DALException;
	boolean saveUser(UserDTO user) throws DALException;
	boolean setID(UserDTO user, int id) throws DALException;
	boolean setName(UserDTO user, String name) throws DALException;
	boolean setInitials(UserDTO user, String initials) throws DALException;
	boolean setCpr(UserDTO user, String cpr) throws DALException;
    boolean setPwd(UserDTO user, String pwd) throws DALException;
    boolean setPwd(UserDTO user) throws DALException;
	boolean deleteUser(int userId) throws DALException;
	Set<Integer> getAvailableIDs() throws DALException;
	String getRequirement(String method);
    Map<String, Rule> ruleList = new HashMap<> ();
    boolean addRole(UserDTO user, String role);
    HashSet<String> getAvailableRoles(UserDTO user);
    void updateUser(UserDTO selected, int selectedOriginalID);
    ArrayList<String> roles = new ArrayList<String>() {{
       add("Admin");
       add("Pharmacist");
       add("Foreman");
       add("Operator");
    }};

    class DALException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7355418246336739229L;

		public DALException(String msg, Throwable e) {
			super(msg,e);
		}

		public DALException(String msg) {
			super(msg);
		}

    }

    class Rule<T> {
        String text;
        Predicate<T> pred;

        public Rule(String text, Predicate<T> pred) {
            this.text = text;
            this.pred = pred;
    }

    public boolean test(T t) {
      return pred.test(t);
    }

    @Override
    public String toString() {
      return text;
    }
    }
}
