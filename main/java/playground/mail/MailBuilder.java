package playground.mail;

public class MailBuilder {

	public static String getVerificationMailContent(String username, String verificationCode, String mail,
			String playground) {
		String body = 
				"Hi, " + username
				+ "\n"
				+ "Welcome to around the world in 80 questions.\n\n"
				+ "Enter the following verification code in order to verify your account.\n"
				+ "Code : "+ verificationCode + "\n"
				+ "Playground : " + playground
				+ "\nYours,\nEden Dupont, Eden Sharoni, Daniil Rolnik, Elia Ben Anat";

		return body;
	}
}
