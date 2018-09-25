package package1;

public abstract class Recipient {
	private String name, email;

	public Recipient(String name, String email) {
		this.name = name;
		this.email = email;

	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}
}
