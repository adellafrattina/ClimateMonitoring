/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import java.util.Random;

import climatemonitoring.core.Application;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Result;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Email;
import imgui.ImGui;

/**
 * To execute the verification code
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Verification {

	public void onHeadlessRender(Operator operator) {

		Email email = new Email("climatemonitoringappservice@mail.com", "Climate Monitoring");
		email.setReceiverEmail(operator.getEmail());
		email.setReceiverName(operator.getName() + " " + operator.getSurname());
		email.setSubject("Climate Monitoring verification code");

		Random r = new Random();
		int codeGiven = r.nextInt(10000, 100000);
		email.setMessage("Your verification code is: " + codeGiven + "\nIt will expire in 2 minutes");

		Result<Boolean> result = email.send();
		result.join();

		start = System.nanoTime();
		Console.write("An email with the verification code has been sent to " + operator.getEmail() + "\nIt will expire in 2 minutes");
		int codeReceived = Integer.parseInt(Console.read("Your verification code > "));

		if (System.nanoTime() - start >= 120000000000L)
			errorMsg = "Time to enter verification code expired";

		if (codeReceived != codeGiven)
			errorMsg = "The verification code entered is incorrect";
	}

	public void onGUIRender(Operator operator) {

		clicked = false;

		if (setUpEmail) {

			Email email = new Email("climatemonitoringappservice@mail.com", "Climate Monitoring");
			email.setReceiverEmail(operator.getEmail());
			email.setReceiverName(operator.getName() + " " + operator.getSurname());
			email.setSubject("Climate Monitoring verification code");
	
			Random r = new Random();
			code = r.nextInt(10000, 100000);
			email.setMessage("Your verification code is: " + code + "\nIt will expire in 2 minutes");
	
			start = System.nanoTime();
			emailSentResult = email.send();
			setUpEmail = false;
		}

		if (emailSentResult != null && emailSentResult.ready()) {

			try {

				emailSentResult.get();
				emailSentResult = null;
			}

			catch (Exception e) {

				e.printStackTrace();
				Application.close();
			}
		}

		else if (emailSentResult != null) {

			text.setOrigin(text.getWidth() / 2.0f, text.getHeight() / 2.0f);
			text.setPosition(ImGui.getWindowWidth() / 2.0f, ImGui.getWindowHeight() / 2.0f);
			text.render();
			return;
		}

		verificationPanel.setSize(Application.getWidth() / 2.0f, Application.getHeight() / 2.0f);
		verificationPanel.setOrigin(verificationPanel.getWidth() / 2.0f, verificationPanel.getHeight() / 2.0f);
		verificationPanel.setPosition(Application.getWidth() / 2.0f, Application.getHeight() / 2.0f);
		verificationPanel.begin("Verification");

		verificationText.setOriginX(verificationText.getWidth() / 2.0f);
		verificationText.setPositionX(verificationPanel.getWidth() / 2.0f);
		verificationText.render();

		verificationEmailText.setOriginX(verificationEmailText.getWidth() / 2.0f);
		verificationEmailText.setPositionX(verificationPanel.getWidth() / 2.0f);
		verificationEmailText.setString(operator.getEmail());
		verificationEmailText.render();

		verificationExpirationText.setOriginX(verificationExpirationText.getWidth() / 2.0f);
		verificationExpirationText.setPositionX(verificationPanel.getWidth() / 2.0f);
		verificationExpirationText.render();

		verificationInputText.setWidth(verificationPanel.getWidth() / 2.0f);
		verificationInputText.setOriginX(verificationInputText.getWidth() / 2.0f);
		verificationInputText.setPositionX(verificationPanel.getWidth() / 2.0f);
		verificationInputText.setNumbersOnly(true);
		verificationInputText.setNoSpaces(true);
		verificationInputText.setEnterReturnsTrue(true);
		if (tried)
			verificationInputText.setReadOnly(true);
		if (verificationInputText.render() || verificationInputText.isButtonPressed()) {

			if (!verificationInputText.getString().isEmpty()) {

				if (System.nanoTime() - start >= 120000000000L)
					errorMsg = "Time to enter verification code expired";

				if (Integer.parseInt(verificationInputText.getString()) != code)
					errorMsg = "The verification code entered is incorrect";

				clicked = true;
				tried = true;
			}
		}

		if (tried) {

			if (errorMsg != null) {

				text.setString(errorMsg);
				text.setOriginX(text.getWidth() / 2.0f);
				text.setPositionX(ImGui.getWindowWidth() / 2.0f);
				text.setColor(255, 0, 0, 255);
				text.render();
	
				cancel.setOriginX(cancel.getWidth() / 2.0f);
				cancel.setPositionX(ImGui.getWindowWidth() / 2.0f);
				if (cancel.render()) {
	
					Handler.getView().resetStateData(new Registration());
					Handler.getView().resetStateData(new CenterCreation());
					Handler.getView().returnToPreviousState();
				}
			}
		}

		verificationPanel.end();
	}

	public boolean clicked() {

		return clicked;
	}

	public String getErrorMsg() {

		return errorMsg;
	}

	private boolean setUpEmail = true;
	private Result<Boolean> emailSentResult;
	private String errorMsg;
	private Text verificationText = new Text("An email with the verification code has been sent to");
	private Text verificationEmailText = new Text("");
	private Text verificationExpirationText = new Text("It will expire in 2 minutes");
	private InputTextButton verificationInputText = new InputTextButton("Code");
	private Panel verificationPanel = new Panel();
	private int code = 0;
	private long start = 0;
	private Text text = new Text("Loading...");
	private boolean clicked = false;
	private Button cancel = new Button("Cancel");
	private boolean tried = false;
}
