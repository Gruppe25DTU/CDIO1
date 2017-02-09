package persistency;

import java.util.ArrayList;

import dto.UserDTO;

public interface IPersistency {
  
  ArrayList<UserDTO> load();
  boolean save(ArrayList<UserDTO> users);

}
