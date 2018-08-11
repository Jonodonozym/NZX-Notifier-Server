
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.accountConfig.AccountConfig;
import jdz.NZXN.entity.accountConfig.AccountConfigRepository;
import jdz.NZXN.entity.device.Device;
import jdz.NZXN.entity.device.DeviceRepository;

@RestController
@RequestMapping("/devicelink")
public class AccountLinkerController {
	@Autowired private DeviceRepository deviceRepo;
	@Autowired private AccountConfigRepository configRepo;

	public AccountConfig getConfig(Principal principal) {
		return configRepo.findByAccountID(getDevice(principal).getAccountID());
	}

	private Device getDevice(Principal principal) {
		return getDevice(UUID.fromString(principal.getName()));
	}

	private Device getDevice(UUID uuid) {
		Device device = deviceRepo.findByDeviceID(uuid);
		if (device == null)
			throw new NullPointerException("No device exists with the ID " + uuid);
		return device;
	}
	
	@GetMapping(path = "/all")
	public List<Device> getAll(Principal principal){
		Device device = getDevice(principal);
		return deviceRepo.findByAccountIDOrderByDeviceIDDesc(device.getAccountID());
	}

	@PostMapping(path = "/join", consumes = "text/plain")
	public Device linkAccount(Principal principal, @RequestBody UUID accountID) {
		Device device = getDevice(principal);
		List<Device> devices = deviceRepo.findByAccountIDOrderByDeviceIDDesc(accountID);
		if (devices.isEmpty())
			return device;

		AccountConfig config = getConfig(principal);
		configRepo.delete(config);

		device.setAccountID(accountID);
		deviceRepo.save(device);
		return device;
	}

	@PostMapping(path = "/unlink", consumes = "text/plain")
	public Device unlinkAccount(Principal principal, @RequestBody UUID deviceID) {
		Device device = getDevice(deviceID);
		AccountConfig config = getConfig(principal);

		while (!deviceRepo.findByAccountIDOrderByDeviceIDDesc(device.getAccountID()).isEmpty())
			device.setAccountID(UUID.randomUUID());
		config.setAccountID(device.getAccountID());

		deviceRepo.save(device);
		configRepo.save(config);
		return device;
	}
	
}
