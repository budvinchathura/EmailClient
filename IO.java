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

			e.printStackTrace();
		} finally {

			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public static void saveRecipient(PersonalRecipient personalRecipient) {
		String name, nickname, email, birthday;
		name = personalRecipient.getName();
		nickname = personalRecipient.getNickname();
		email = personalRecipient.getEmail();
		birthday = personalRecipient.getBirthday().getYear() + "/" + personalRecipient.getBirthday().getMonth() + "/"
				+ personalRecipient.getBirthday().getDay();

		String recipientDetails = "Personal: " + name + "," + nickname + "," + email + "," + birthday;
		saveToFile(recipientDetails);
	}

	public static void saveRecipient(OfficialRecipient officialRecipient) {
		String name, email, designation;
		name = officialRecipient.getName();
		email = officialRecipient.getEmail();
		designation = officialRecipient.getDesignation();

		String recipientDetails = "Official: " + name + "," + email + "," + designation;
		saveToFile(recipientDetails);
	}

	public static void saveRecipient(OfficialRecipientFriend officialRecipientFriend) {
		String name, email, designation, birthday;
		name = officialRecipientFriend.getName();
		email = officialRecipientFriend.getEmail();
		designation = officialRecipientFriend.getDesignation();
		birthday = officialRecipientFriend.getBirthday().getYear() + "/"
				+ officialRecipientFriend.getBirthday().getMonth() + "/"
				+ officialRecipientFriend.getBirthday().getDay();

		String recipientDetails = "Office_friend: " + name + "," + email + "," + designation + "," + birthday;
		saveToFile(recipientDetails);
	}

	public static ArrayList<Email> loadEmails() {
		ArrayList<Object> loadedObjects = new ArrayList<Object>();

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
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			loadedObjects = new ArrayList<Object>();
		} finally {

		}

		return (ArrayList<Email>) (ArrayList<?>) loadedObjects;
	}

	public static void saveEmail(Email email, ArrayList<Email> emailList) {
		emailList.add(email);

		FileOutputStream outputStream = null;
		ObjectOutputStream objectoutputStream = null;
		try {
			outputStream = new FileOutputStream("objects.ser");
			objectoutputStream = new ObjectOutputStream(outputStream);
			objectoutputStream.writeObject(emailList);
			if (outputStream != null) {
				outputStream.close();
			}
			if (objectoutputStream != null) {
				objectoutputStream.close();
			}

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

		}

	}

	public static ArrayList<Recipient> loadRecipients() {
		ArrayList<Recipient> recipients = new ArrayList<Recipient>();
		ArrayList<String> recipientDetails = readFromFile();
		if (recipientDetails.isEmpty()) {
			return recipients;
		} else {

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

		return null;

	}

	public static Recipient createRecipient(String[] detailParts) {
		String[] detailParts2;
		String[] birthdayParts;
		Date birthday;
		switch (detailParts[0]) {
		case "Official": {
			detailParts2 = detailParts[0].trim().split(",");
			return new OfficialRecipient(detailParts2[0], detailParts2[1], detailParts2[2]);
		}

		case "Office_friend": {
			detailParts2 = detailParts[0].trim().split(",");
			birthdayParts = detailParts2[3].split("/");
			birthday = new Date(birthdayParts[0], birthdayParts[1], birthdayParts[2]);
			return new OfficialRecipientFriend(detailParts2[0], detailParts2[1], detailParts2[2], birthday);
		}

		case "Personal": {
			detailParts2 = detailParts[0].trim().split(",");
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
			e.printStackTrace();
		} finally {

		}

		return recipientDetails;

	}

}
