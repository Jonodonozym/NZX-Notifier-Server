
package jdz.NZXN.entity.accountConfig;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountConfigRepository extends JpaRepository<AccountConfig, Long> {
	public AccountConfig findByAccountID(UUID uuid);
}
