
package jdz.NZXN.entity.device;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "devices", indexes = @Index(columnList = "accountID"))
@Entity
@EqualsAndHashCode(of = { "deviceID" })
public class Device {
	@Type(type="uuid-char")
	@Setter @Getter private UUID accountID;
	
	@Id
	@Type(type="uuid-char")
	@Getter private UUID deviceID;
	
	@Getter @Setter private String name;
	
	@Getter @Setter private Long lastFetchedAnnouncement = 0L;

	protected Device() {}

	public Device(UUID accountId, UUID deviceID, String name) {
		this.accountID = accountId;
		this.deviceID = deviceID;
		this.name = name;
	}
}
