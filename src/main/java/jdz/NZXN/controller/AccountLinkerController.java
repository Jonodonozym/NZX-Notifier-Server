
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.JSON;
import jdz.NZXN.entity.accountConfig.AccountConfig;
import jdz.NZXN.entity.accountConfig.AccountConfigRepository;
import jdz.NZXN.entity.device.Device;
import jdz.NZXN.entity.device.DeviceRepository;

@RestController
@RequestMapping("/devicelink")
public class AccountLinkerController {
	@Autowired
	private DeviceRepository deviceRepo;
	@Autowired
	private AccountConfigRepository configRepo;

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

	@GetMapping(path = "/linked")
	public List<Device> getAll(@AuthenticationPrincipal Principal principal) {
		Device device = getDevice(principal);
		List<Device> devices = deviceRepo.findByAccountIDOrderByDeviceIDDesc(device.getAccountID());
		devices.remove(device);
		return devices;
	}

	@PostMapping(path = "/join")
	public Device linkAccount(@AuthenticationPrincipal Principal principal, @RequestBody String json) {
		UUID deviceID = UUID.fromString(JSON.extractFirst(json));
		Device device = getDevice(principal);
		Device desired = deviceRepo.findByDeviceID(deviceID);

		AccountConfig config = getConfig(principal);
		configRepo.delete(config);

		device.setAccountID(desired.getAccountID());
		deviceRepo.save(device);
		return device;
	}

	@PostMapping(path = "/unlink")
	public Device unlinkAccount(@AuthenticationPrincipal Principal principal, @RequestBody String json) {
		UUID deviceID = UUID.fromString(JSON.extractFirst(json));
		Device device = getDevice(deviceID);

		while (!deviceRepo.findByAccountIDOrderByDeviceIDDesc(device.getAccountID()).isEmpty())
			device.setAccountID(UUID.randomUUID());

		AccountConfig config = new AccountConfig(device);

		configRepo.save(config);
		deviceRepo.save(device);
		return device;
	}

}
