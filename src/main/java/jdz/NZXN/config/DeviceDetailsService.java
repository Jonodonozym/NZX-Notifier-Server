
package jdz.NZXN.config;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jdz.NZXN.entity.device.Device;
import jdz.NZXN.entity.device.DeviceRepository;

@Service
public class DeviceDetailsService implements UserDetailsService {
	@Autowired
	private DeviceRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Device device = repo.findByDeviceID(UUID.fromString(username));
		if (device == null)
			throw new UsernameNotFoundException("Device id " + username + " not found");
		return new User(username, "", AuthorityUtils.createAuthorityList("ROLE_USER"));
	}
}