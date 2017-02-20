package persistency;

import dto.UserDTO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileSaver implements IPersistency {

    private static final String FILE_NAME = System.getProperty("user.dir") + "\\dtopersist.txt";
    private Map<Integer, UserDTO> userList;

    public FileSaver() {
        userList = new HashMap<>();
        init();
    }

    public void init() {
        ObjectInputStream oIS = null;
        try {
            FileInputStream fIS = new FileInputStream(FILE_NAME);
            oIS = new ObjectInputStream(fIS);
            Object inObj = oIS.readObject();
            if (inObj instanceof Map) {
                userList = (Map<Integer, UserDTO>) inObj;
            } else {
                //throw new DALException("Wrong object in file");
                return;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //No problem - just returning empty userstore
        } catch (IOException e) {
            e.printStackTrace();
            //throw new DALException("Error while reading disk!", e);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //throw new DALException("Error while reading file - Class not found!", e);
            return;
        } finally {
            if (oIS != null) {
                try {
                    oIS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //throw new DALException("Error closing pObjectStream!", e);
                }
            }
        }
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
        userList.remove(origID);
        if (!userList.containsKey(user.getUserID())) {
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
        ObjectOutputStream oOS = null;
        try {
            FileOutputStream fOS = new FileOutputStream(FILE_NAME);
            oOS = new ObjectOutputStream(fOS);
            oOS.writeObject(userList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //throw new DALException("Error locating file", e);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            //throw new DALException("Error writing to disk", e);
            return;
        } finally {
            if (oOS != null) {
                try {
                    oOS.close();
                } catch (IOException e) {//throw new DALException("Unable to close ObjectStream", e);
                    e.printStackTrace();
                }
            }
        }

    }

}