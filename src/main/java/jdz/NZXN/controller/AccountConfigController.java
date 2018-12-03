
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.JSON;
import jdz.NZXN.entity.accountConfig.AccountConfig;
import jdz.NZXN.entity.accountConfig.AccountConfigRepository;
import jdz.NZXN.entity.accountConfig.PushNotificationType;
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
	public AccountConfig getConfig(@AuthenticationPrincipal Principal principal) {
		return configRepo.findByAccountID(getDevice(principal).getAccountID());
	}

	private Device getDevice(Principal principal) {
		Device device = deviceRepo.findByDeviceID(UUID.fromString(principal.getName()));
		if (device == null)
			throw new NullPointerException("No device exists with the ID " + principal.getName());
		return device;
	}

	@PostMapping(path = "/blacklist/company/add")
	public void blacklistAddCompany(@AuthenticationPrincipal Principal principal, @RequestBody String companyId) {
		companyId = JSON.extractFirst(companyId);
		Optional<Company> company = companyRepo.findById(companyId);
		if (!company.isPresent())
			return;

		AccountConfig config = getConfig(principal);
		config.getCompanyBlacklist().add(company.get());
		configRepo.save(config);
	}

	@PostMapping(path = "/blacklist/company/remove")
	public void blacklistRemoveCompany(@AuthenticationPrincipal Principal principal, @RequestBody String companyId) {
		companyId = JSON.extractFirst(companyId);
		Optional<Company> company = companyRepo.findById(companyId);
		if (!company.isPresent())
			return;

		AccountConfig config = getConfig(principal);
		config.getCompanyBlacklist().remove(company.get());
		configRepo.save(config);
	}

	@PostMapping(path = "/blacklist/type/add")
	public void blacklistAddType(@AuthenticationPrincipal Principal principal, @RequestBody String json) {
		AnnouncementType type = AnnouncementType.valueOf(JSON.extractFirst(json));
		if (type == null)
			return;

		AccountConfig config = getConfig(principal);
		config.getTypeBlacklist().add(type);
		configRepo.save(config);
	}

	@PostMapping(path = "/blacklist/type/remove")
	public void blacklistRemoveType(@AuthenticationPrincipal Principal principal, @RequestBody String json) {
		AnnouncementType type = AnnouncementType.valueOf(JSON.extractFirst(json));
		if (type == null)
			return;

		AccountConfig config = getConfig(principal);
		config.getTypeBlacklist().remove(type);
		configRepo.save(config);
	}

	@PostMapping(path = "/blacklist/keyword/add")
	public void blacklistAddKeyword(@AuthenticationPrincipal Principal principal, @RequestBody String keyword) {
		AccountConfig config = getConfig(principal);
		config.getKeywordBlacklist().add(JSON.extractFirst(keyword));
		configRepo.save(config);
	}

	@PostMapping(path = "/blacklist/keyword/remove")
	public void blacklistRemoveKeyword(@AuthenticationPrincipal Principal principal, @RequestBody String keyword) {
		AccountConfig config = getConfig(principal);
		config.getKeywordBlacklist().remove(JSON.extractFirst(keyword));
		configRepo.save(config);
	}

	@PostMapping(path = "/push")
	public void updatePushConfig(@AuthenticationPrincipal Principal principal, @RequestBody PushConfigDTO dto) {
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
	public void updateQuietHoursConfig(@AuthenticationPrincipal Principal principal,
			@RequestBody QuietHoursConfigDTO dto) {
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
	public void updateAlertConfig(@AuthenticationPrincipal Principal principal, @RequestBody AlertConfigDTO dto) {
		AccountConfig config = getConfig(principal);
		config.setAlertFrequencyMinutes(dto.getFrequencyMinutes());
		configRepo.save(config);
	}

	@Data
	public static class AlertConfigDTO {
		private int frequencyMinutes = 10;
	}
}
