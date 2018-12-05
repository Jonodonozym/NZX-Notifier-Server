
package jdz.NZXN.entity.device;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Table(name = "devices", indexes = @Index(columnList = "accountID"))
@Entity
@EqualsAndHashCode(of = { "deviceID" })
@NoArgsConstructor
public class Device {
	@Type(type = "uuid-char") private UUID accountID;

	@Id @Type(type = "uuid-char") private UUID deviceID;

	private String name;

	private Long lastFetchedAnnouncement = 0L;

	public Device(UUID accountId, UUID deviceID, String name) {
		accountID = accountId;
		this.deviceID = deviceID;
		this.name = name;
	}
}
