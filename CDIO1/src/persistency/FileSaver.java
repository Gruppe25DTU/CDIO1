package persistency;

import dto.UserDTO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import dal.IUserDAO.DALException;
import dal.UserStore;

public class FileSaver implements IPersistency {

  private UserStore load() throws DALException {
    UserStore userStore = new UserStore();
    ObjectInputStream oIS = null;
    try {
      FileInputStream fIS = new FileInputStream(fileName);
      oIS = new ObjectInputStream(fIS);
      Object inObj = oIS.readObject();
      if (inObj instanceof UserStore){
        userStore = (UserStore) inObj;
      } else {
        throw new DALException("Wrong object in file");
      }
    } catch (FileNotFoundException e) {
      //No problem - just returning empty userstore
    } catch (IOException e) {
      throw new DALException("Error while reading disk!", e);
    } catch (ClassNotFoundException e) {
      throw new DALException("Error while reading file - Class not found!", e);
    } finally {
      if (oIS!=null){
        try {
          oIS.close();
        } catch (IOException e) {
          throw new DALException("Error closing pObjectStream!", e);
        }
      }
    }
    return userStore;
  }

  private void save(UserStore users) throws DALException {
    ObjectOutputStream oOS =null;
    try {
      FileOutputStream fOS = new FileOutputStream(fileName);
      oOS = new ObjectOutputStream(fOS);
      oOS.writeObject(users);
    } catch (FileNotFoundException e) {
      throw new DALException("Error locating file", e);
    } catch (IOException e) {
      throw new DALException("Error writing to disk", e);
    } finally {
      if (oOS!=null) {
        try {
          oOS.close();
        } catch (IOException e) {throw new DALException("Unable to close ObjectStream", e);
        }
      }
    } 
  }

}
