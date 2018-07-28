
package jdz.NZXN.entity.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "users")
@Entity
@ToString(exclude = {"userConfig"})
@EqualsAndHashCode(of = { "userId" })
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private Long userId;
	@Getter private String username;
	@Getter @Setter private String password;
	@Getter @Setter private String email;
	@Getter @Setter private Long lastUpdate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	@Getter @Setter private UserConfig userConfig = new UserConfig();

	protected User() {}

	public User(String username, String password, String email) {
		this(username, password, email, 0L);
	}

	public User(String username, String password, String email, Long lastUpdate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.lastUpdate = lastUpdate;
	}
}
