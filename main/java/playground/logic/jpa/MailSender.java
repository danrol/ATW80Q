package playground.logic.jpa;


import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import playground.constants.Playground;


public class MailSender {

	private static Properties props = setProperty();
	private static Session session = Session.getInstance(props, 
			new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(Playground.PLAYGROUND_MAIL,Playground.PLAYGROUND_MAIL_PASSWORD);
		}
	});

	
	private static Properties setProperty() {
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		return props;
	}
	public MailSender() {

	}
	public void sendMail(Message message)
	{

		try {
			System.err.println("Trying to send mail..." + message.getSubject() + " " + message.getContent() +" " +message.getRecipients(Message.RecipientType.TO));
			Transport.send(message);

			System.err.println("mail sent.");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Properties getProperties() {
		return props;
	}
	public void setPropertiess(Properties props) {
		this.props = props;
	}
	
	
}