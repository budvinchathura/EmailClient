package package1;

import java.io.Serializable;

class Email implements Serializable{
	private String recipientAddress;
	private String content;
	private Date sentDate;
	public Email(String recipientAddress,String content) {
		this.recipientAddress=recipientAddress;
		this.content=content;
		this.sentDate=new Date();
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
}
