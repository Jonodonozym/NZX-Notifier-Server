
package jdz.NZXN.entity.device;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    public Device findByDeviceID(UUID deviceID);
    public List<Device> findByAccountIDOrderByDeviceIDDesc(UUID accountID);
}