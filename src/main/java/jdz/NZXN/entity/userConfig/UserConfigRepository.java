
package jdz.NZXN.entity.userConfig;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {
    public UserConfig findByUsername(String username);
}
