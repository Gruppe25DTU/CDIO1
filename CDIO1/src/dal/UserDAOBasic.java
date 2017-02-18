package dal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import dto.UserDTO;
import dal.Password;
import persistency.IPersistency;

public class UserDAOBasic implements IUserDAO {


	private List<UserDTO> UserList = new ArrayList<UserDTO>();

	@Override
	public UserDTO getUser(int userId) throws DALException {
		// TODO Auto-generated method stub
		for (UserDTO User : UserList) {
			if(User.getUserID()==userId)
			{
				return User;
			}
		}
		return null;
	}

	@Override
	public List<UserDTO> getUserList() throws DALException {
		// TODO Auto-generated method stub
		return UserList;
	}

	@Override
	public UserDTO createUser(UserDTO user) throws DALException {
		// TODO Auto-generated method stub
		UserList.add(user);
		return user;
		
	}

	@Override
	public boolean setID(UserDTO user, int id) throws DALException {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean setName(UserDTO user, String name) throws DALException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setInitials(UserDTO user, String initials) throws DALException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setCpr(UserDTO user, String cpr) throws DALException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setPwd(UserDTO user, String pwd) throws DALException {
		// TODO Auto-generated method stub
		pwd = Password.makePassword(8);
		user.setPassword(pwd);
		return false;
	}

	@Override
	public void deleteUser(int userId) throws DALException {
		// TODO Auto-generated method stub
		UserList.remove(userId);

	}

}
