package persistency;

import dto.UserDTO;

import java.util.ArrayList;
import java.util.Set;

public interface IPersistency {

    void init();

    boolean save(UserDTO user);

    void updateUser(UserDTO user, int i);

    UserDTO getUser(int id);

    ArrayList<UserDTO> getUserList();

    Set<Integer> getUserIDList();

    boolean deleteUser(int userID);

    void quit();

}
