package package1;

import java.util.Scanner;

public class EmailClientDemo {

	public static void main(String[] args) {
		EmailClient emailClient = new EmailClient();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter option type: \n" + "1 - Adding a new recipient\n" + "2 - Sending an email\n"
				+ "3 - Printing out all the recipients who have birthdays\n"
				+ "4 - Printing out details of all the emails sent\n"
				+ "5 - Printing out the number of recipient objects in the application");

		int option = Integer.parseInt(scanner.nextLine().trim());
		switch (option) {
		case 1:
			String recipientDetails = scanner.nextLine().trim();
			emailClient.addRecipient(recipientDetails);
			// input format - Official: nimal,nimal@gmail.com,ceo
			// Use a single input to get all the details of a recipient
			// code to add a new recipient
			// store details in clientList.txt file
			// Hint: use methods for reading and writing files
			break;
		case 2:
			String[] emailComponents = scanner.nextLine().trim().split(",");
			Email email = new Email(emailComponents[0], emailComponents[1], emailComponents[2]);
			MailTLS.sendMail(email);
			break;
		case 3:
			String[] dateComponents = scanner.nextLine().trim().split("/");
			Date givenDate = new Date(dateComponents[0], dateComponents[1], dateComponents[2]);
			emailClient.printGivenDateBirthdays(givenDate);
			// input format - yyyy/MM/dd (ex: 2018/09/17)
			// code to print recipients who have birthdays on the given date
			break;
		case 4:
			String[] dateComponents2 = scanner.nextLine().trim().split("/");
			Date givenDate2 = new Date(dateComponents2[0], dateComponents2[1], dateComponents2[2]);
			emailClient.printGivenDateEmails(givenDate2);
			// input format - yyyy/MM/dd (ex: 2018/09/17)
			// code to print the details of all the emails sent on the input date
			break;
		case 5:
			emailClient.printNoOfRecipients();
			break;

		}
		// emailClient.sendWishes();
		emailClient.serializeEmails();
		scanner.close();

	}

}
