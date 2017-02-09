package dal;

import java.util.ArrayList;
import java.util.List;

import dto.UserDTO;
import persistency.IPersistency;

public class UserDAOBasic implements IUserDAO {
  
  private IPersistency persistencyManager; //TODO
  private ArrayList<UserDTO> userList; //TODO

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
  public void createUser(UserDTO user) throws DALException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateUser(UserDTO user) throws DALException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteUser(int userId) throws DALException {
    // TODO Auto-generated method stub
    
  }
  
  private String generatePassword() {
    // TODO
    return null;
  }

}
