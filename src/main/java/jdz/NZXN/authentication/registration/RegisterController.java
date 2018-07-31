
package jdz.NZXN.authentication.registration;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jdz.NZXN.authentication.PasswordStrengthChecker;
import jdz.NZXN.authentication.registration.registerToken.RegisterToken;
import jdz.NZXN.authentication.registration.registerToken.RegisterTokenRepository;
import jdz.NZXN.entity.user.User;
import jdz.NZXN.entity.user.UserRepository;
import lombok.Data;

@Controller
@Transactional
@RequestMapping("/register")
public class RegisterController {
	@Autowired private UserRepository userRepo;
	@Autowired private RegisterTokenRepository tokenRepo;
	@Autowired private JavaMailSender mailer;
	@Autowired private PasswordEncoder pwEncrypter;

	// Process form input data
	@PostMapping
	public RegisterResponse processRegistrationForm(@RequestBody RegisterDTO userInfo, HttpServletRequest request) {
		User userExists = userRepo.findByEmail(userInfo.getEmail());
		if (userExists != null)
			return RegisterResponse.EMAIL_EXISTS;

		userExists = userRepo.findByUsername(userInfo.getUsername());
		if (userExists != null)
			return RegisterResponse.USERNAME_EXISTS;

		if (!userInfo.getPassword().equals(userInfo.getPassword_2()))
			return RegisterResponse.PASSWORD_WRONG;

		if (!PasswordStrengthChecker.isStrong(userInfo.getPassword()))
			return RegisterResponse.PASSWORD_WEAK;

		String encryptedPassword = pwEncrypter.encode(userInfo.getPassword());

		User user = new User(userInfo.getUsername(), encryptedPassword, userInfo.getEmail());

		user.setVerified(false);
		userRepo.save(user);

		String token = UUID.randomUUID().toString();
		tokenRepo.save(new RegisterToken(user.getUsername(), token));

		String appUrl = request.getScheme() + "://" + request.getServerName();

		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText(
				"To confirm your e-mail address, please click the link below:\n" + appUrl + "/confirm?token=" + token);
		registrationEmail.setFrom("noreply@NZXNotifier.co.nz");

		mailer.send(registrationEmail);

		return RegisterResponse.SUCCESS;
	}

	@Data
	public static class RegisterDTO {
		private final String username;
		private final String email;
		private final String password;
		private final String password_2;
	}

	public static enum RegisterResponse {
		SUCCESS, USERNAME_EXISTS, EMAIL_EXISTS, PASSWORD_WEAK, PASSWORD_WRONG;
	}

	// Process confirmation link
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public void processConfirmationForm(@RequestParam String token, HttpServletResponse response) {
		RegisterToken cToken = tokenRepo.findByToken(token);
		if (cToken == null)
			return;

		User user = cToken.getUser();
		user.setVerified(true);

		tokenRepo.delete(cToken);
		userRepo.save(user);

		try {
			response.sendRedirect("/register/success");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("jaidencolebaker@gmail.com");
		mailSender.setPassword("Jouel383841");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
