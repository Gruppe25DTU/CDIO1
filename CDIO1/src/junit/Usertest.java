package junit;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dal.UserDAOBasic;
import dto.UserDTO;
import persistency.IPersistency;
import persistency.MemorySaver;
import view.IUIController;
import view.TUIController;

public class Usertest {
	IPersistency persistency = new MemorySaver();
    IUserDAO logic = new UserDAOBasic(persistency);
     IUIController ctrl = new TUIController(logic);

    @Before
    public void setup() throws Exception {
    	ctrl.createUser();
    }
    
    
	@Test
	public void CreateUsertest(){
		ArrayList<UserDTO> test;
		try {
			test = persistency.getUserList();
			assertTrue(test.size() != 0);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void UpdateUserTest(){
		UserDTO before;
		try {
			before = persistency.getUser(11);
			ctrl.listUsers();
			ctrl.initCommandList();
			ctrl.updateUser();
			assertNotEquals(before, persistency.getUser(11));
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void DeleteUser() {
		ArrayList<UserDTO> test;
		try {
			test = persistency.getUserList();
			ctrl.deleteUser();
			assertNotEquals(test, persistency.getUserList());
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
