
package jdz.NZXN.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {
    public UserConfig findByUserId(Long userId);
}
