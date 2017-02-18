package dal;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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

  HashMap<Runnable, Rule> ruleList = new HashMap<> ();
  
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

  class Rule {
    String text;
    Predicate<Integer> pred;

    public Rule(String text, Predicate<Integer> pred) {
      this.text = text;
      this.pred = pred;
    }
    
    public boolean test(Integer t) {
      return pred.test(t);
    }
    
    @Override
    public String toString() {
      return text;
    }
  }
	
	default void createRuleSet(IUserDAO userDAO) {
        int minID = 11;
        int maxID = 99;
        Predicate<Integer> pred = new Predicate<Integer>() {
          @Override
          public boolean test(Integer t) {
            return t > minID && t < maxID;
          };
        };
        Rule rule = new Rule("ID must be between" + minID + " and " + maxID, pred);
        try {
            //TODO
            //ruleList.put(userDAO::setID, rule);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
