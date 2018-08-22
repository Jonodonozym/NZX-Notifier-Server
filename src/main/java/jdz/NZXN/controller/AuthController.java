
package jdz.NZXN.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private DeviceRepository deviceRepo;
	@Autowired
	private AccountConfigRepository configRepo;
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping(path = "/signin")
	public boolean signin(HttpServletRequest req, @RequestBody String uuid) {
		try {
			uuid = JSON.extractFirst(uuid);
			Device device = deviceRepo.findByDeviceID(UUID.fromString(uuid));
			if (device == null)
				throw new BadCredentialsException("No device exists with the ID " + uuid);

			Authentication authentication = new UsernamePasswordAuthenticationToken(uuid, null);
			authentication = authenticationManager.authenticate(authentication);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(authentication);

			HttpSession session = req.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
			return true;
		}
		catch (BadCredentialsException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@PostMapping(path = "/register")
	public String registerNewDevice(@RequestBody String deviceName, HttpServletRequest request) {
		deviceName = JSON.extractFirst(deviceName);
		Device device = new Device(UUID.randomUUID(), UUID.randomUUID(), deviceName);
		while (deviceRepo.findByDeviceID(device.getDeviceID()) != null
				|| !deviceRepo.findByAccountIDOrderByDeviceIDDesc(device.getAccountID()).isEmpty()) {
			device = new Device(UUID.randomUUID(), UUID.randomUUID(), deviceName);
		}
		AccountConfig config = new AccountConfig(device);

		deviceRepo.save(device);
		configRepo.save(config);

		System.out.println("registered: " + device.getDeviceID().toString());
		return device.getDeviceID().toString();
	}
}
