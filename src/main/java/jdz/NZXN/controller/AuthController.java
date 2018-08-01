
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/auth")
public class AuthController {
	@Autowired private DeviceRepository deviceRepo;
	@Autowired private AccountConfigRepository configRepo;

	@PostMapping(path = "/signin", consumes = "text/plain")
	public boolean signin(@RequestBody String uuid) {
		try {
			authenticate(uuid);
			return true;
		}
		catch (BadCredentialsException e) {
			return false;
		}
	}

	@GetMapping("/register")
	public Device registerNewDevice(HttpServletRequest request) {
		Device device = new Device(UUID.randomUUID(), UUID.randomUUID());
		while (deviceRepo.findByDeviceID(device.getDeviceID()) != null
				|| !deviceRepo.findByAccountIDOrderByDeviceIDDesc(device.getAccountID()).isEmpty()) {
			device = new Device(UUID.randomUUID(), UUID.randomUUID());
		}
		AccountConfig config = new AccountConfig(device);
		
		deviceRepo.save(device);
		configRepo.save(config);
		
		return device;
	}

	private Principal authenticate(String deviceId) {
		Device device = deviceRepo.findByDeviceID(UUID.fromString(deviceId));
		if (device == null)
			throw new BadCredentialsException("No device exists with the ID " + deviceId);

		Authentication authentication = new UsernamePasswordAuthenticationToken(deviceId, null,
				AuthorityUtils.createAuthorityList("ROLE_USER"));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}
}
