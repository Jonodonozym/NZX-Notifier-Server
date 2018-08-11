
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.accountConfig.AccountConfig;
import jdz.NZXN.entity.accountConfig.PushNotificationType;
import jdz.NZXN.entity.accountConfig.AccountConfigRepository;
import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;
import jdz.NZXN.entity.device.Device;
import jdz.NZXN.entity.device.DeviceRepository;
import lombok.Data;

@RestController
@RequestMapping("/config")
public class AccountConfigController {
	@Autowired private DeviceRepository deviceRepo;
	@Autowired private AccountConfigRepository configRepo;
	@Autowired private CompanyRepository companyRepo;

	@RequestMapping
	public AccountConfig getConfig(Principal principal) {
		return configRepo.findByAccountID(getDevice(principal).getAccountID());
	}

	private Device getDevice(Principal principal) {
		Device device = deviceRepo.findByDeviceID(UUID.fromString(principal.getName()));
		if (device == null)
			throw new NullPointerException("No device exists with the ID " + principal.getName());
		return device;
	}

	@PostMapping(path = "/blacklist/company/add", consumes = "text/plain")
	public boolean blacklistAddCompany(Principal principal, @RequestBody String companyId) {
		Optional<Company> company = companyRepo.findById(companyId);
		if (!company.isPresent())
			return false;

		return getConfig(principal).getCompanyBlacklist().add(company.get());
	}

	@PostMapping(path = "/blacklist/company/remove", consumes = "text/plain")
	public boolean blacklistRemoveCompany(Principal principal, @RequestBody String companyId) {
		Optional<Company> company = companyRepo.findById(companyId);
		if (!company.isPresent())
			return false;

		return getConfig(principal).getCompanyBlacklist().remove(company.get());
	}

	@PostMapping(path = "/blacklist/type/add", consumes = "text/plain")
	public boolean blacklistAddType(Principal principal, @RequestBody AnnouncementType type) {
		if (type == null)
			return false;

		return getConfig(principal).getTypeBlacklist().add(type);
	}

	@PostMapping(path = "/blacklist/type/remove", consumes = "text/plain")
	public boolean blacklistRemoveType(Principal principal, @RequestBody AnnouncementType type) {
		if (type == null)
			return false;

		return getConfig(principal).getTypeBlacklist().remove(type);
	}

	@PostMapping(path = "/blacklist/keyword/add", consumes = "text/plain")
	public boolean blacklistAddKeyword(Principal principal, @RequestBody String keyword) {
		return getConfig(principal).getKeywordBlacklist().add(keyword);
	}

	@PostMapping(path = "/blacklist/keyword/remove", consumes = "text/plain")
	public boolean blacklistRemoveKeyword(Principal principal, @RequestBody String keyword) {
		return getConfig(principal).getKeywordBlacklist().remove(keyword);
	}

	@PostMapping(path = "/push")
	public void updatePushConfig(Principal principal, @RequestBody PushConfigDTO dto) {
		AccountConfig config = getConfig(principal);
		config.setPushEnabled(dto.isEnabled());
		config.setPushType(dto.getType());
		configRepo.save(config);
	}

	@Data
	public static class PushConfigDTO {
		private boolean enabled = true;
		private PushNotificationType type = PushNotificationType.VIBRATE;
	}

	@PostMapping(path = "/quietHours")
	public void updateQuietHoursConfig(Principal principal, @RequestBody QuietHoursConfigDTO dto) {
		AccountConfig config = getConfig(principal);
		config.setQuietHoursEnabled(dto.isEnabled());
		config.setQuietHoursStartMinutes(dto.getStartMinutes());
		config.setQuietHoursEndMinutes(dto.getEndMinutes());
		configRepo.save(config);
	}

	@Data
	public static class QuietHoursConfigDTO {
		private boolean enabled = false;
		private int startMinutes = 0;
		private int endMinutes = 0;
	}

	@PostMapping(path = "/alert")
	public void updateAlertConfig(Principal principal, @RequestBody AlertConfigDTO dto) {
		AccountConfig config = getConfig(principal);
		config.setAlertFrequencyMinutes(dto.getFrequencyMinutes());
		configRepo.save(config);
	}

	@Data
	public static class AlertConfigDTO {
		private int frequencyMinutes = 10;
	}
}
