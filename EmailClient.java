package package1;

import java.util.ArrayList;

public class EmailClient {

	private ArrayList<Recipient> allRecipients = new ArrayList<Recipient>();
	private ArrayList<Greetable> greetableRecipients = new ArrayList<Greetable>();
	private ArrayList<Email> prevEmails = new ArrayList<Email>();
	private ArrayList<Email> todaySentEmails = new ArrayList<Email>();

	public EmailClient() {
		this.allRecipients = IO.loadRecipients();
		this.greetableRecipients = loadGreetableRecipients();
		this.prevEmails = IO.loadEmails();

	}

	private ArrayList<Greetable> loadGreetableRecipients() {
		ArrayList<Greetable> greetableRecipients = new ArrayList<Greetable>();
		if (this.allRecipients.isEmpty()) {
			return greetableRecipients;
		} else {
			for (int i = 0; i < this.allRecipients.size(); i++) {
				if (this.allRecipients.get(i) instanceof Greetable) {
					greetableRecipients.add((Greetable) this.allRecipients.get(i));
				}
			}

			return greetableRecipients;

		}
	}

	public void addRecipient(String recipientDetails) {
		Recipient recipient = IO.createRecipient(recipientDetails.split(":"));
		IO.saveRecipient(recipient);
		this.allRecipients.add(recipient);
		if (recipient instanceof Greetable) {
			this.greetableRecipients.add((Greetable) recipient);
		}

	}

	public void sendEmail(String emailDetails) {
		String[] emailParts = emailDetails.trim().split(",");
		Email sendingEmail = new Email(emailParts[0], emailParts[0], emailParts[0]);
		MailTLS.sendMail(sendingEmail);
		this.todaySentEmails.add(sendingEmail);
	}

	public void printGivenDateBirthdays(Date date) {
		ArrayList<Greetable> givenDateBirthdayRecipients = new ArrayList<Greetable>();
		givenDateBirthdayRecipients = getGivenBirthdayRecipients(this.greetableRecipients, date);
		if (givenDateBirthdayRecipients.isEmpty()) {
			System.out.println("\nNo Birthdays on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
		} else {
			System.out.println("\nBirthdays on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
			for (int i = 0; i < givenDateBirthdayRecipients.size(); i++) {
				System.out.println(givenDateBirthdayRecipients.get(i).getName());
			}
		}

	}

	private ArrayList<Greetable> getGivenBirthdayRecipients(ArrayList<Greetable> allGreetableRecipients, Date date) {
		ArrayList<Greetable> givenBirthdayRecipients = new ArrayList<Greetable>();
		if (allGreetableRecipients.isEmpty()) {
			return givenBirthdayRecipients;
		} else {
			for (int i = 0; i < allGreetableRecipients.size(); i++) {
				if (allGreetableRecipients.get(i).getBirthday().getDay().equals(date.getDay())
						&& allGreetableRecipients.get(i).getBirthday().getMonth().equals(date.getMonth())) {
					givenBirthdayRecipients.add(allGreetableRecipients.get(i));
				}
			}
		}

		return givenBirthdayRecipients;
	}

	public void printGivenDateEmails(Date date) {
		ArrayList<Email> givenDateEmails = getGivenDateEmails(this.prevEmails, date);
		if (givenDateEmails.isEmpty()) {
			System.out.println(
					"\nNo emails were sent on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
		} else {
			System.out
					.println("\nEmails sent on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay() + "\n");
			for (int i = 0; i < givenDateEmails.size(); i++) {
				System.out.println("To: " + givenDateEmails.get(i).getRecipientAddress() + "\n" + "Subject: "
						+ givenDateEmails.get(i).getSubject() + "\n" + "Content: " + givenDateEmails.get(i).getContent()
						+ "\n");
			}
		}
	}

	private ArrayList<Email> getGivenDateEmails(ArrayList<Email> allEmails, Date date) {
		ArrayList<Email> givenDateEmails = new ArrayList<Email>();
		if (allEmails.isEmpty()) {
			return givenDateEmails;
		} else {
			for (int i = 0; i < allEmails.size(); i++) {
				if (allEmails.get(i).getSentDate().getYear().equals(date.getYear())
						&& allEmails.get(i).getSentDate().getMonth().equals(date.getMonth())
						&& allEmails.get(i).getSentDate().getDay().equals(date.getDay())) {
					givenDateEmails.add(allEmails.get(i));
				}
			}
			return givenDateEmails;
		}
	}

	public void printNoOfRecipients() {
		System.out.println("\nNo of recipients: " + this.allRecipients.size());
	}

	public void sendWishes() {
		Date today = new Date();
		System.out.println(today.getDay() + today.getMonth() + today.getYear());
		ArrayList<Greetable> todayBirthdayRecipients;
		todayBirthdayRecipients = getGivenBirthdayRecipients(this.greetableRecipients, today);

		if (todayBirthdayRecipients.isEmpty()) {
			System.out.println("No wishes sent today!");
			return;
		} else {
			for (int i = 0; i < todayBirthdayRecipients.size(); i++) {
				if (todayBirthdayRecipients.get(i) instanceof PersonalRecipient) {
					sendPersonalWish((PersonalRecipient) todayBirthdayRecipients.get(i));
				} else if (todayBirthdayRecipients.get(i) instanceof OfficialRecipientFriend) {
					sendOfficialWish((OfficialRecipientFriend) todayBirthdayRecipients.get(i));
				}
				
			}
			System.out.println("Today wishes sent!");

		}

	}

	private void sendPersonalWish(PersonalRecipient person) {
		Email wish = new Email(person.getEmail(), "Happy Birthday!",
				"Dear " + person.getName() + ",\nHugs and love on your birthday!\nEmail User");
		MailTLS.sendMail(wish);
		this.todaySentEmails.add(wish);
	}

	private void sendOfficialWish(OfficialRecipientFriend person) {
		Email wish = new Email(person.getEmail(), "Happy Birthday!",
				"Dear " + person.getName() + ",\nWish you a Happy Birthday!\nEmailUser");
		MailTLS.sendMail(wish);
		this.todaySentEmails.add(wish);
	}

	public void serializeEmails() {
		ArrayList<Email> emailsToSerialize = new ArrayList<Email>();
		emailsToSerialize.addAll(this.prevEmails);
		emailsToSerialize.addAll(this.todaySentEmails);
		IO.saveEmails(emailsToSerialize);
		System.out.println("\nAll emails saved to disk");
	}

}
