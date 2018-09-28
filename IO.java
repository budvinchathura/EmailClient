package package1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class IO {

	public static void saveToFile(String recipientDetails) {

		PrintWriter outputStream = null;
		try {

			outputStream = new PrintWriter(new FileWriter("clientList.txt", true));

			outputStream.println(recipientDetails);
		} catch (IOException e) {
			System.out.println("Could not write Recipient Details to text file!");
			//e.printStackTrace();
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
			//e.printStackTrace();
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
