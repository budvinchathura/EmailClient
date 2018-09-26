package package1;

import java.io.Serializable;

public class Email implements Serializable {
	private String recipientAddress;
	private String content;
	private String subject;
	private Date sentDate;

	public Email(String recipientAddress, String subject, String content) {
		this.recipientAddress = recipientAddress;
		this.content = content;
		this.sentDate = new Date();
		this.subject = subject;
		// TODO Auto-generated constructor stub
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
