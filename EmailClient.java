package package1;

import java.util.ArrayList;

public class EmailClient {
	private ArrayList<Greeting> todayBirthdayPeople = new ArrayList<Greeting>();
	private ArrayList<Greeting> givenDateBirthdayPeople = new ArrayList<Greeting>();
	private ArrayList<Greeting> greetablePeople = new ArrayList<Greeting>();
	private ArrayList<Recipient> allRecipients = new ArrayList<Recipient>();
	private ArrayList<Email> prevEmails = new ArrayList<Email>();
	private ArrayList<Email> todayEmails = new ArrayList<Email>();
	private ArrayList<Email> givenDateEmails = new ArrayList<Email>();

	public EmailClient() {
		this.allRecipients = IO.loadRecipients();
		this.prevEmails = IO.loadEmails();
		this.greetablePeople = loadGreetable();

	}

	public void sendWishes() {
		Date today = new Date();
		this.todayBirthdayPeople=getBirthdayPeople(greetablePeople, today);

		if (this.todayBirthdayPeople.isEmpty()) {
			return;
		} else {
			for (int i = 0; i < this.todayBirthdayPeople.size(); i++) {
				if (this.todayBirthdayPeople.get(i) instanceof PersonalRecipient) {
					sendPersonalWish((PersonalRecipient) this.todayBirthdayPeople.get(i));
				} else if (this.todayBirthdayPeople.get(i) instanceof OfficialRecipientFriend) {
					sendOfficialWish((OfficialRecipientFriend) this.todayBirthdayPeople.get(i));
				}
			}

		}

	}

	private void sendPersonalWish(PersonalRecipient person) {
		Email wish = new Email(person.getEmail(), "Happy Birthday!", "Hugs and love on your birthday\nEmail User");
		MailTLS.sendMail(wish);
		this.todayEmails.add(wish);
	}

	private void sendOfficialWish(OfficialRecipientFriend person) {
		Email wish = new Email(person.getEmail(), "Happy Birthday!", "Wish you a Happy Birthday!\nEmailUser");
		MailTLS.sendMail(wish);
		this.todayEmails.add(wish);
	}

	private ArrayList<Greeting> loadGreetable() {
		ArrayList<Greeting> greetablePeople = new ArrayList<Greeting>();
		if (this.allRecipients.isEmpty()) {
			return greetablePeople;
		} else {
			for (int i = 0; i < this.allRecipients.size(); i++) {
				if (this.allRecipients.get(i) instanceof Greeting) {
					greetablePeople.add((Greeting) this.allRecipients.get(i));
				}
			}

			return greetablePeople;

		}
	}

	private ArrayList<Greeting> getBirthdayPeople(ArrayList<Greeting> greetable, Date date) {
		ArrayList<Greeting> birthdayPeople = new ArrayList<Greeting>();
		if (greetable.isEmpty()) {
			return birthdayPeople;
		} else {
			for (int i = 0; i < greetable.size(); i++) {
				if (greetable.get(i).getBirthday().getDay().equals(date.getDay())
						&& greetable.get(i).getBirthday().getMonth().equals(date.getMonth())) {
					birthdayPeople.add(greetable.get(i));
				}
			}
		}

		return birthdayPeople;
	}
	
	private ArrayList<Email> getGivenDateEmails(ArrayList<Email> allEmails,Date date){
		ArrayList<Email> givenDateEmails = new ArrayList<Email>();
		if (allEmails.isEmpty()) {
			return givenDateEmails;
		}
		else {
			for(int i=0;i<allEmails.size();i++) {
				if (allEmails.get(i).getSentDate().getYear().equals(date.getYear()) &&
						allEmails.get(i).getSentDate().getMonth().equals(date.getMonth()) &&
						allEmails.get(i).getSentDate().getDay().equals(date.getDay())) {
					givenDateEmails.add(allEmails.get(i));
				}
			}
			return givenDateEmails;
		}
	}
	
	public void sendEmail(String emailDetails) {
		String[] emailParts=emailDetails.trim().split(",");
		Email sendingEmail=new Email(emailParts[0], emailParts[0], emailParts[0]);
		MailTLS.sendMail(sendingEmail);
		this.todayEmails.add(sendingEmail);
	}

}
