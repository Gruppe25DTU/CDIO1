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
		UserDTO user = new UserDTO(12,"name","ini","cpr","password",role);		




		DatabaseSaver.init();
		System.out.println("init success");
		try {
			DatabaseSaver.createTable();
			System.out.println("create table success");
			DatabaseSaver.addToTable(user);
			System.out.println("addToTable success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserDTO loadedUser = i.load(12);
		System.out.println(loadedUser);
		
		UserDTO updateUser = new UserDTO(14,"name1","ini1","cpr1","password1",role);		
		i.updateUser(updateUser, 12);
		UserDTO updatedUser = i.load(14);
		System.out.println(updatedUser);






	}

}
