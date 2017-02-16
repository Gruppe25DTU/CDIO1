package test;
import java.util.ArrayList;

import dto.UserDTO;
import persistency.DatabaseSaver;
import persistency.IPersistency;


public class Test {
	public static void main(String[] args) {

		
		IPersistency i = new DatabaseSaver();
		
		
		ArrayList<String> role = new ArrayList<String>();
		role.add("foreman");
		role.add("admin");
		role.add("Pharmacist");
		role.add("Operator");
		UserDTO user = new UserDTO(90,"name","ini","cpr","password",role);		

		UserDTO user1 = new UserDTO(45,"Hej","med","Dig","!!!",role);		


		DatabaseSaver.init();
		System.out.println("init success");
		try {
			DatabaseSaver.createTable();
			System.out.println("create table success");
			DatabaseSaver.addToTable(user);
			DatabaseSaver.addToTable(user1);
			System.out.println("addToTable success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserDTO loadedUser = i.load(90);
		System.out.println(loadedUser);
		
		UserDTO updateUser = new UserDTO(17,"name1","ini1","cpr1","password1",role);		
		i.updateUser(updateUser, 90);
		UserDTO updatedUser = i.load(17);
		System.out.println(updatedUser);
		
		System.out.println("Delete user 17");
		DatabaseSaver.deleteUser(17);
		
		
		System.out.println("Print user list");
		ArrayList<UserDTO> list = DatabaseSaver.getUserList();
		
		for(int r = 0;r<list.size();r++) {
			System.out.println(list.get(r));
		}





	}

}
