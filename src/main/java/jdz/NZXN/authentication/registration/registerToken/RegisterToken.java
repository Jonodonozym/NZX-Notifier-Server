
package jdz.NZXN.authentication.registration.registerToken;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

import jdz.NZXN.entity.user.User;
import jdz.NZXN.entity.user.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RegisterToken {
	@Autowired
	private static UserRepository userRepo;
	
	private String username;
	@Id
	private String token;
	
	public User getUser() {
		return userRepo.findByUsername(username);
	}
}
