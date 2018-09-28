package package1;

public class OfficialRecipientFriend extends OfficialRecipient implements Greetable {
	private Date birthday = new Date();

	public OfficialRecipientFriend(String name, String email, String designation, Date birthday) {
		super(name, email, designation);
		this.birthday = birthday;

	}

	public Date getBirthday() {
		return this.birthday;
	}

}
