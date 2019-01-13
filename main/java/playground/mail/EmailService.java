package playground.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private JavaMailSender emailSender;

	@Autowired
	public void setEmailSender(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void sendMail(final Mail mail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(mail.getSubject());
		message.setText(mail.getContent());
		message.setTo(mail.getTo());

		emailSender.send(message);
	}

}