package junit;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import dal.IUserDAO;
import dal.UserDAOBasic;
import dto.UserDTO;
import persistency.IPersistency;
import persistency.MemorySaver;
import view.IUserInterface;
import view.TUIBasic;
import view.TUIController;

public class Usertest {
	IPersistency persistency = new MemorySaver();
    IUserDAO logic = new UserDAOBasic(persistency);
    IUserInterface ui = new TUIBasic();
    TUIController ctrl = new TUIController(ui, logic);

    @Before
    public void setup() throws Exception {
    	ctrl.createUser();
    }
    
    
	@Test
	public void CreateUsertest(){
		ArrayList<UserDTO> test = persistency.getUserList();
		assertTrue(test.size() != 0);
	}
	
	@Test
	public void UpdateUserTest(){
		UserDTO before = persistency.getUser(11);
		ctrl.listUsers();
		ctrl.initCommandList();
		ctrl.updateUser();
		assertNotEquals(before, persistency.getUser(11));
		
	}
	
	@Test
	public void DeleteUser() {
		ArrayList<UserDTO> test = persistency.getUserList();
		ctrl.deleteUser();
		assertNotEquals(test, persistency.getUserList());
	}

}
