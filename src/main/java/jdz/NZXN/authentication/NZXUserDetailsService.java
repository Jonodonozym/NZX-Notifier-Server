
package jdz.NZXN.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import jdz.NZXN.auth.accountLocker.LoginAttemptService;
import jdz.NZXN.entity.user.User;
import jdz.NZXN.entity.user.UserRepository;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired private UserRepository userRepository;
	@Autowired private LoginAttemptService loginAttemptService;

	@Autowired private PasswordEncoder passEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String ip = getRemoteAddress();
		boolean blocked = ip == null ? false : loginAttemptService.isBlocked(ip);

		User user = userRepository.findByEmail(email);
		if (user == null)
			user = userRepository.findByUsername(email);
		if (user == null)
			throw new UsernameNotFoundException("Username Not Found");

		return org.springframework.security.core.userdetails.User.builder().passwordEncoder(passEncoder::encode)
				.username(user.getUsername()).password(user.getPassword()).accountLocked(blocked).roles("USER").build();
	}

	private String getRemoteAddress() {
		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		if (attribs instanceof NativeWebRequest) {
			HttpServletRequest request = (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
			return request.getRemoteAddr();
		}
		return null;
	}
}
