//170153K

package mail_client_package1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email_Client {
	public static void main(String[] args) {
		EmailClient emailClient = new EmailClient();
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nEnter option type: \n" + "1 - Adding a new recipient\n" + "2 - Sending an email\n"
				+ "3 - Printing out all the recipients who have birthdays\n"
				+ "4 - Printing out details of all the emails sent\n"
				+ "5 - Printing out the number of recipient objects in the application\n");

		int option = Integer.parseInt(scanner.nextLine().trim());
		switch (option) {
		case 1:
			System.out.println("\nEnter the details of the recipient:");
			String recipientDetails = scanner.nextLine().trim();
			emailClient.addRecipient(recipientDetails);
			System.out.println("\nRecipient was added successfully");

			break;
		case 2:
			System.out.println("\nEnter the details of the email to send:");
			String[] emailComponents = scanner.nextLine().trim().split(",");
			Email email = new Email(emailComponents[0], emailComponents[1], emailComponents[2]);
			MailTLS.sendMail(email);
			break;
		case 3:
			System.out.println("\nEnter the date to diaplay birthdays:");
			String[] dateComponents = scanner.nextLine().trim().split("/");
			Date givenDate = new Date(dateComponents[0], dateComponents[1], dateComponents[2]);
			emailClient.printGivenDateBirthdays(givenDate);
			// input format - yyyy/MM/dd (ex: 2018/09/17)
			// code to print recipients who have birthdays on the given date
			break;
		case 4:
			System.out.println("\nEnter the date to display sent emails:");
			String[] dateComponents2 = scanner.nextLine().trim().split("/");
			Date givenDate2 = new Date(dateComponents2[0], dateComponents2[1], dateComponents2[2]);
			emailClient.printGivenDateEmails(givenDate2);

			break;
		case 5:
			emailClient.printNoOfRecipients();
			break;

		}
		// emailClient.sendWishes();
		emailClient.serializeEmails();
		scanner.close();
		System.out.println("Closing email client...");

	}

}

class EmailClient {

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

class IO {

	public static void saveToFile(String recipientDetails) {

		PrintWriter outputStream = null;
		try {

			outputStream = new PrintWriter(new FileWriter("clientList.txt", true));

			outputStream.println(recipientDetails);
		} catch (IOException e) {
			System.out.println("Could not write Recipient Details to text file!");
			// e.printStackTrace();
		} finally {

			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public static void saveRecipient(Recipient recipient) {
		String recipientDetails = "";
		if (recipient instanceof PersonalRecipient) {
			PersonalRecipient personalRecipient = (PersonalRecipient) recipient;
			String name, nickname, email, birthday;
			name = personalRecipient.getName();
			nickname = personalRecipient.getNickname();
			email = personalRecipient.getEmail();
			birthday = personalRecipient.getBirthday().getYear() + "/" + personalRecipient.getBirthday().getMonth()
					+ "/" + personalRecipient.getBirthday().getDay();

			recipientDetails = "Personal: " + name + "," + nickname + "," + email + "," + birthday;

		} else if (recipient instanceof OfficialRecipientFriend) {
			OfficialRecipientFriend officialRecipientFriend = (OfficialRecipientFriend) recipient;
			String name, email, designation, birthday;
			name = officialRecipientFriend.getName();
			email = officialRecipientFriend.getEmail();
			designation = officialRecipientFriend.getDesignation();
			birthday = officialRecipientFriend.getBirthday().getYear() + "/"
					+ officialRecipientFriend.getBirthday().getMonth() + "/"
					+ officialRecipientFriend.getBirthday().getDay();

			recipientDetails = "Office_friend: " + name + "," + email + "," + designation + "," + birthday;

		} else if (recipient instanceof OfficialRecipient) {
			OfficialRecipient officialRecipient = (OfficialRecipient) recipient;
			String name, email, designation;
			name = officialRecipient.getName();
			email = officialRecipient.getEmail();
			designation = officialRecipient.getDesignation();

			recipientDetails = "Official: " + name + "," + email + "," + designation;

		}

		saveToFile(recipientDetails);
	}

	public static ArrayList<Email> loadEmails() {
		ArrayList<Object> loadedObjects = null;

		FileInputStream inputStream = null;

		ObjectInputStream objectinputStream = null;
		try {
			inputStream = new FileInputStream("objects.txt");
			objectinputStream = new ObjectInputStream(inputStream);

			loadedObjects = (ArrayList<Object>) objectinputStream.readObject();
			if (inputStream != null) {
				inputStream.close();
			}
			if (objectinputStream != null) {
				objectinputStream.close();
			}

		} catch (IOException e) {
			System.out.println("Could not load previous Emails!");
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			loadedObjects = new ArrayList<Object>();
		} finally {

		}
		if (loadedObjects == null) {
			return new ArrayList<Email>();
		}
		System.out.println("Previous sent emails loaded");
		return (ArrayList<Email>) (ArrayList<?>) loadedObjects;
	}

	public static void saveEmails(ArrayList<Email> emailList) {

		FileOutputStream outputStream = null;
		ObjectOutputStream objectoutputStream = null;
		try {
			outputStream = new FileOutputStream("objects.txt");
			objectoutputStream = new ObjectOutputStream(outputStream);
			objectoutputStream.writeObject(emailList);
			if (outputStream != null) {
				outputStream.close();
			}
			if (objectoutputStream != null) {
				objectoutputStream.close();
			}

		} catch (IOException e) {
			System.out.println("Could not save Emails!");
			// e.printStackTrace();
		} finally {

		}

	}

	public static ArrayList<Recipient> loadRecipients() {
		ArrayList<Recipient> recipients = new ArrayList<Recipient>();
		ArrayList<String> recipientDetails = readFromFile();

		if (recipientDetails.isEmpty()) {
			return recipients;
		} else {

			System.out.println("Recipient details loaded");
			String[] detailParts;
			Recipient recipient;
			for (int i = 0; i < recipientDetails.size(); i++) {
				detailParts = recipientDetails.get(i).split(":");
				recipient = createRecipient(detailParts);
				if (recipient != null) {
					recipients.add(recipient);
				} else {
					continue;
				}
			}

		}

		return recipients;

	}

	public static Recipient createRecipient(String[] detailParts) {
		String[] detailParts2;
		String[] birthdayParts;
		Date birthday;
		switch (detailParts[0].trim().toLowerCase()) {
		case "official": {
			detailParts2 = detailParts[1].trim().split(",");
			return new OfficialRecipient(detailParts2[0], detailParts2[1], detailParts2[2]);
		}

		case "office_friend": {
			detailParts2 = detailParts[1].trim().split(",");
			birthdayParts = detailParts2[3].split("/");
			birthday = new Date(birthdayParts[0], birthdayParts[1], birthdayParts[2]);
			return new OfficialRecipientFriend(detailParts2[0], detailParts2[1], detailParts2[2], birthday);
		}

		case "personal": {
			detailParts2 = detailParts[1].trim().split(",");
			birthdayParts = detailParts2[3].split("/");
			birthday = new Date(birthdayParts[0], birthdayParts[1], birthdayParts[2]);
			return new PersonalRecipient(detailParts2[0], detailParts2[1], detailParts2[2], birthday);
		}
		default:
			return null;

		}

	}

	public static ArrayList<String> readFromFile() {
		ArrayList<String> recipientDetails = new ArrayList<String>();
		FileReader fileIn = null;
		BufferedReader in = null;
		try {
			fileIn = new FileReader("clientList.txt");
			in = new BufferedReader(fileIn);
			String line = null;
			while ((line = in.readLine()) != null) {
				recipientDetails.add(line);

			}
			in.close();
			fileIn.close();

		} catch (Exception e) {
			System.out.println("Could not load Recipient Details");
		} finally {

		}

		return recipientDetails;

	}

}

class MailTLS {

	public static void sendMail(Email email) {
		final String username = "nimalpereracs@gmail.com";
		final String password = "cse@1234";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("nimalpereracs@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getRecipientAddress()));
			message.setSubject(email.getSubject());
			message.setText(email.getContent());

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}

abstract class Recipient {
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

class PersonalRecipient extends Recipient implements Greetable {
	private String nickname;
	Date birthday = new Date();

	public PersonalRecipient(String name, String nickname, String email, Date birthday) {
		super(name, email);
		this.nickname = nickname;
		this.birthday = birthday;

	}

	public Date getBirthday() {
		return this.birthday;
	}

	public String getNickname() {
		return this.nickname;
	}

}

class OfficialRecipient extends Recipient {
	private String designation;

	public OfficialRecipient(String name, String email, String designation) {
		super(name, email);
		this.designation = designation;
	}

	public String getDesignation() {
		return this.designation;
	}

}

class OfficialRecipientFriend extends OfficialRecipient implements Greetable {
	private Date birthday = new Date();

	public OfficialRecipientFriend(String name, String email, String designation, Date birthday) {
		super(name, email, designation);
		this.birthday = birthday;

	}

	public Date getBirthday() {
		return this.birthday;
	}

}

interface Greetable {
	public Date getBirthday();

	public String getName();

}

class Email implements Serializable {
	private String recipientAddress;
	private String content;
	private String subject;
	private Date sentDate;

	public Email(String recipientAddress, String subject, String content) {
		this.recipientAddress = recipientAddress;
		this.content = content;
		this.sentDate = new Date();
		this.subject = subject;
	}

	public String getRecipientAddress() {
		return this.recipientAddress;
	}

	public String getContent() {
		return this.content;

	}

	public Date getSentDate() {
		return this.sentDate;
	}

	public String getSubject() {
		return this.subject;
	}
}

class Date implements Serializable {
	String year, month, day;

	public Date(String year, String month, String day) {
		this.year = year;
		this.month = month;
		this.day = day;

	}

	public Date() {
		Calendar calendar = Calendar.getInstance();
		this.year = String.valueOf(calendar.get(Calendar.YEAR));
		this.month = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
		this.day = String.format("%02d", calendar.get(Calendar.DATE));

	}

	public String getYear() {
		return this.year;
	}

	public String getMonth() {
		return this.month;
	}

	public String getDay() {
		return this.day;
	}
}
