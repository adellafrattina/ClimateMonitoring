/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

/**
 * The Email class creates and sends an email through the use of a SMTP server
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Email {

	/**
	 * Initializes the email sender
	 * @param sender_email The sender's email
	 * @param sender_name The sender's name
	 */
	public Email(String sender_email, String sender_name){
		m_senderEmail = sender_email;
		m_senderName = sender_name;
	}

	/**
	 * To send the email to the receiver. This function is non-blocking
	 * @return True if the email was sent, otherwise false
	 */
	public Result <Boolean> send() {

		return new Result<Boolean>() {

			@Override
			public Boolean exec() throws Exception {
		
				try {
		
					org.simplejavamail.api.email.Email email = EmailBuilder.startingBlank()
						.from(m_senderName, m_senderEmail)
						.to(m_receiverName, m_receiverEmail)
						.withSubject(m_subject)
						.withPlainText(m_message)
						.buildEmail();
		
					Mailer mailer = MailerBuilder
						.withSMTPServer("smtp-relay.brevo.com", 587, "7a8c92002@smtp-brevo.com", "AawCOMxz95hJWcyT")
						.withTransportStrategy(TransportStrategy.SMTP)
						.buildMailer();
		
					mailer.sendMail(email);
					return true;
				}
		
				catch (RuntimeException e) {
		
					throw new Exception(e);
				}
			}
		};
	}
	
	/**
	 * To set the receiver's email
	 * @param receiver_email The receiver's email
	 */
	public void setReceiverEmail(String receiver_email){

		this.m_receiverEmail = receiver_email;
	}

	/**
	 * To set the receiver's name
	 * @param receiver_email The receiver's name
	 */
	public void setReceiverName(String receiver_name) {

		this.m_receiverName = receiver_name;
	}

	/**
	 * To set the email's subject
	 * @param subject The email's subject
	 */
	public void setSubject(String subject){

		this.m_subject = subject;
	}

	/**
	 * To set the email's message
	 * @param message The email's message
	 */
	public void setMessage(String message){

		this.m_message = message;
	}

	private String m_senderEmail;
	private String m_senderName;
	private String m_receiverEmail;
	private String m_receiverName;
	private String m_subject;
	private String m_message;
}
