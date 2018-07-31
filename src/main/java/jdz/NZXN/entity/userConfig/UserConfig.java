
package jdz.NZXN.entity.userConfig;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = { "username" })
public class UserConfig {
	@Id @GeneratedValue
	@Getter private String username;

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
		this.username = user.getUsername();
	}
}
