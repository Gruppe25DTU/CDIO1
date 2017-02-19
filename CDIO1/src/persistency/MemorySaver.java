package persistency;

import dto.UserDTO;

import java.util.*;

/**
 * Created by ymuslu on 19-02-2017.
 */
public class MemorySaver implements IPersistency {

    private Map<Integer, UserDTO> userList;

    public MemorySaver() {
        init();
    }

    @Override
    public void init() {
        userList = new HashMap<>();
    }

    @Override
    public boolean save(UserDTO user) {
        if (userList.containsKey(user.getUserID())) {
            return false;
        }
        userList.put(user.getUserID(), user);
        return true;
    }

    @Override
    public void updateUser(UserDTO user, int origID) {
        if (userList.containsKey(origID)) {
            if (origID != user.getUserID()) {
                userList.remove(origID);
            }
            userList.put(user.getUserID(), user);
        }
    }

    @Override
    public UserDTO getUser(int id) {
        return userList.get(id);
    }

    @Override
    public ArrayList<UserDTO> getUserList() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public Set<Integer> getUserIDList() {
        return userList.keySet();
    }

    @Override
    public boolean deleteUser(int userID) {
        return (userList.remove(userID) != null);
    }

    @Override
    public void quit() {
        userList.clear();
    }
}
