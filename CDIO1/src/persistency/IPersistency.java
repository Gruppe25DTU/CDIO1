package persistency;

import dto.UserDTO;

import java.util.ArrayList;
import java.util.Set;

import dal.IUserDAO.DALException;

public interface IPersistency {

    void init() throws DALException;

    boolean save(UserDTO user);

    void updateUser(UserDTO user, int i);

    UserDTO getUser(int id) throws DALException;

    ArrayList<UserDTO> getUserList() throws DALException;

    Set<Integer> getUserIDList() throws DALException;

    boolean deleteUser(int userID);

    void quit();

}
