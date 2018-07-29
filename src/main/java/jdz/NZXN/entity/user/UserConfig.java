
package jdz.NZXN.entity.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "userId"})
@EqualsAndHashCode(of = { "user" })
public class UserConfig {
	@Id @GeneratedValue
	@Getter private Long userId;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@ElementCollection
	@Getter private Set<Company> companyBlacklist = new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.ORDINAL)
	@Getter private Set<AnnouncementType> typeBlacklist = new HashSet<>();
    
    @ElementCollection
	@Getter private Set<String> keywordBlacklist = new HashSet<>();

	@Getter @Setter private boolean pushEnabled = true;

    @Enumerated(EnumType.ORDINAL)
	@Getter @Setter private PushNotificationType pushType = PushNotificationType.VIBRATE;

	@Getter @Setter private int alertFrequencyMinutes = 10;

	@Getter @Setter private boolean quietHoursEnabled = false;
	@Getter @Setter private int quietHoursStartMinutes = 0;
	@Getter @Setter private int quietHoursEndMinutes = 0;
	
	public UserConfig(User user) {
		this.user = user;
		this.userId = user.getUserId();
	}
}
