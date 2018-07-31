
package jdz.NZXN.auth.accountLocker;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class AuthFailureMessage extends SimpleUrlAuthenticationFailureHandler {

	@Autowired private MessageSource messages;
	@Autowired private LocaleResolver localeResolver;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		setDefaultFailureUrl("/login?error=true");

		super.onAuthenticationFailure(request, response, exception);

		final Locale locale = localeResolver.resolveLocale(request);

		String errorMessage = messages.getMessage("auth.message.badPassword", null, locale);
		if (exception instanceof UsernameNotFoundException)
			errorMessage = messages.getMessage("auth.message.badUsername", null, locale);
			
		else if (exception.getMessage().equalsIgnoreCase("User account has expired"))
			errorMessage = messages.getMessage("auth.message.expired", null, locale);
			
		else if (exception.getMessage().equalsIgnoreCase("blocked"))
			errorMessage = messages.getMessage("auth.message.blocked", null, locale);
			
		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
	}
}