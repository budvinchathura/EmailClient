package package1;



class PersonalRecipient extends Recipient implements Greeting {
	private String nickname;
	Date birthday = new Date();

	public PersonalRecipient(String name, String email,Date birthday) {
		super(name, email);
		this.birthday=birthday;
		
	}
	
	public Date getBirthday() {
		return this.birthday;
	}
	
	public String getNickname() {
		return this.nickname;
	}

}
