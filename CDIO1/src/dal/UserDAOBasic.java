package dal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import dto.UserDTO;
import persistency.IPersistency;

public class UserDAOBasic implements IUserDAO {

  @Override
  public UserDTO getUser(int userId) throws DALException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<UserDTO> getUserList() throws DALException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserDTO createUser(UserDTO user) throws DALException {
    // TODO Auto-generated method stub
    return null;
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
    return false;
  }

  @Override
  public void deleteUser(int userId) throws DALException {
    // TODO Auto-generated method stub
    
  }

}
