
package jdz.NZXN.authentication.registration.registerToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTokenRepository extends JpaRepository<RegisterToken, String> {
	RegisterToken findByToken(String token);
}
