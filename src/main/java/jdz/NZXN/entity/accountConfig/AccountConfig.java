
package jdz.NZXN.entity.accountConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.device.Device;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = { "accountID" })
public class AccountConfig {
	@Id
	@Type(type="uuid-char")
	@Setter @Getter private UUID accountID;

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
	
	public AccountConfig(Device user) {
		this.accountID = user.getAccountID();
	}
}
