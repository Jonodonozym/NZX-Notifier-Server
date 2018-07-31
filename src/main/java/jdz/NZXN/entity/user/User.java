
package jdz.NZXN.entity.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import jdz.NZXN.entity.userConfig.UserConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "users", indexes = @Index(columnList = "email"))
@Entity
@ToString(exclude = { "userConfig" })
@EqualsAndHashCode(of = { "username" })
public class User {
	@Id @Getter private String username;
	@Getter @Setter private String password;
	@Getter @Setter private String email;

	@Getter @Setter private boolean verified = true;
	@Getter @Setter private Long lastFetchedAnnouncement = 0L;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) @JoinColumn(name = "username", referencedColumnName = "username", nullable = false) @Getter @Setter private UserConfig userConfig = new UserConfig(
			this);

	protected User() {}

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
}
