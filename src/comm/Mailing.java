package comm;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import util.ConfigManager;
import util.Constant;

public class Mailing {
	static ConfigManager cm= ConfigManager.getInstance();
	
	/**
	 * This method will return Recipient list
	 * 
	 * @param prjName
	 * 
	 * @return toList
	 */
	private String getToListByProjectName(String prjName){
		String toList=null;
		if(prjName.equalsIgnoreCase("ESD")){
			toList=cm.getProperty(Constant.ESD_LEAD_MAILID);
		}else{
			toList=cm.getProperty(Constant.BH_LEAD_MAILID);
		}
		return toList;
	}
	
	/**
	 * This method will send mail to Recipient
	 * 
	 * @param fileName
	 * @param projectName
	 * 
	 */
	public void sendMail(String fileName, String projectName) {

		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", cm.getProperty(Constant.SMTP_HOST));
		properties.setProperty("port", cm.getProperty(Constant.SMTP_PORT));
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(cm.getProperty(Constant.MAIL_FROM)));

			
			// Set To: header field of the header.
			List<String> lstRecipient = Arrays.asList(getToListByProjectName(projectName).split(","));
			for (String to : lstRecipient) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			}
			lstRecipient = Arrays.asList(cm.getProperty(Constant.PM_MAILID).split(","));
			for (String cc : lstRecipient) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}

			// Set Subject: header field
			message.setSubject(cm.getProperty(Constant.MAIL_SUBJECT) + projectName);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(cm.getProperty(Constant.MAIL_TEXT));

			// Create a multipart message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(fileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully.");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Mailing().sendMail("", "'");
	}
}
