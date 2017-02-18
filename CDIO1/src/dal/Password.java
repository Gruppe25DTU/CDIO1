package dal;

public class Password {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String pwd = makePassword(8);
		System.out.println(pwd);
		
	}
	
	public static String makePassword(int length) {
		String password = "";
		
		for(int i = 0; i<length - 2; i++) {
			password = password + randomCharacter("abcdefghijklmnopqrstuvwxyz");
		}
		String randomDigit = randomCharacter("0123456789");
		password = insertAtRandom(password, randomDigit);
		
		String randomcapsCharacter = randomCharacter("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		password = insertAtRandom(password, randomcapsCharacter);
		return password;
	}
	
	public static String randomCharacter(String characters) {
		int n = characters.length();
		int r = (int) (n * Math.random());
		return characters.substring(r, r + 1);
	}
	
	public static String insertAtRandom(String str, String toInsert) {
		int n = str.length();
		int r = (int) ((n + 1) * Math.random());
		return str.substring(0, r) + toInsert + str.substring(r);
	}

}
