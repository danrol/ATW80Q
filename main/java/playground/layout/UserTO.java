package playground.layout;

import java.util.regex.Pattern;
import playground.constants.User;
import playground.logic.UserEntity;

public class UserTO {

	private String email = "";
	private String avatar = "";
	private String username;
	private String playground;
	private String role = User.GUEST_ROLE;
	private long points = 0;

	public UserTO() {
	}

	public UserTO(String username, String email, String avatar, String role, String playground) {
		super();
		setUsername(username);
		setEmail(email);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
		setPoints(0);
	}

	public UserTO(UserEntity u) {
		this.setEmail(u.getEmail());
		this.setAvatar(u.getAvatar());
		this.setUsername(u.getUsername());
		this.setPlayground(u.getPlayground());
		this.setRole(u.getRole());
		this.setPoints(u.getPoints());
	}

	public static boolean emailIsValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	public UserTO(String username, String email, String avatar, String role, String playground, String code) {
		this(username, email, avatar, role, playground);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		role = role.toLowerCase();
		if (role.equals(User.MANAGER_ROLE.toLowerCase())) {
			this.role = User.MANAGER_ROLE;
		} else if (role.equals(User.PLAYER_ROLE.toLowerCase())) {
			this.role = User.PLAYER_ROLE;
		} else {
			this.role = User.GUEST_ROLE;
			throw new RuntimeException("Undefined role");
		}
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "UserTO [email=" + email + ", avatar=" + avatar + ", username=" + username + ", playground=" + playground
				+ ", role=" + role + ", verified_user=" + ", points=" + points + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public UserEntity toEntity() {
		UserEntity rv = new UserEntity();
		rv.setEmail(email);
		rv.setAvatar(avatar);
		rv.setUsername(username);
		rv.setPlayground(playground);
		rv.setRole(role);
		rv.setPoints(points);
		return rv;
	}

}