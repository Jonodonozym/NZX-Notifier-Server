
package jdz.NZXN.entity.device;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "devices", indexes = @Index(columnList = "accountID"))
@Entity
@EqualsAndHashCode(of = { "deviceID" })
public class Device {
	@Setter @Getter private UUID accountID;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Getter private UUID deviceID;
	@Getter @Setter private Long lastFetchedAnnouncement = 0L;

	protected Device() {}

	public Device(UUID accountId, UUID deviceId) {
		this.accountID = accountId;
	}
}
