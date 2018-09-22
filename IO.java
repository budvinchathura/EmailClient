package package1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import emailclient.Sample;

class IO {

	public static void saveToFile(String recipientDetails) {

		PrintWriter outputStream = null;
		try {

			outputStream = new PrintWriter(new FileWriter("output_file.txt", true));

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

		String recipientDetails = name + "," + nickname + "," + email + "," + birthday;
		saveToFile(recipientDetails);
	}

	public static void saveRecipient(OfficialRecipient officialRecipient) {
		String name, email, designation;
		name = officialRecipient.getName();
		email = officialRecipient.getEmail();
		designation = officialRecipient.getDesignation();

		String recipientDetails = name + "," + email + "," + designation;
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

		String recipientDetails = name + "," + email + "," + designation + "," + birthday;
		saveToFile(recipientDetails);
	}

	public static ArrayList<Email> loadEmails() {
		ArrayList<Object> loadedObjects;

		FileInputStream inputStream = null;

		ObjectInputStream objectinputStream = null;
		try {
			inputStream = new FileInputStream("objects.txt");
			objectinputStream = new ObjectInputStream(inputStream);

			loadedObjects = (ArrayList<Object>) objectinputStream.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			loadedObjects= new ArrayList<Object>();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (objectinputStream != null) {
				objectinputStream.close();
			}

		}
		
		return (ArrayList<Email>)(ArrayList<?>)(loadedObjects));
	}
	
	public static void saveEmail(Email email,ArrayList<Email> emailList) {
		emailList.add(email);
		
		FileOutputStream outputStream = null;
        ObjectOutputStream objectoutputStream = null;
        try {
            outputStream = new FileOutputStream("objects.ser");
            objectoutputStream = new ObjectOutputStream(outputStream);
            objectoutputStream.writeObject(emailList);
            

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (objectoutputStream != null) {
                objectoutputStream.close();
            }

        }
		
	}

}
