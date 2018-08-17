
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.accountConfig.AccountConfig;
import jdz.NZXN.entity.accountConfig.AccountConfigRepository;
import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementRepository;
import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;
import jdz.NZXN.entity.device.Device;
import jdz.NZXN.entity.device.DeviceRepository;

@RestController
@RequestMapping("/announcements")
public class AnnouncementFetchController {
	@Autowired private AnnouncementRepository announcementRepo;
	@Autowired private AccountConfigRepository configRepo;
	@Autowired private DeviceRepository deviceRepo;
	@Autowired private CompanyRepository companyRepo;

	private Device getDevice(@AuthenticationPrincipal Principal principal) {
		Device device = deviceRepo.findByDeviceID(UUID.fromString(principal.getName()));
		if (device == null)
			throw new NullPointerException("No device exists with the ID " + principal.getName());
		return device;
	}

	@GetMapping("/new")
	public Collection<Announcement> newAnnouncementsFiltered(@AuthenticationPrincipal Principal principal) {
		Device user = getDevice(principal);
		Long lastAnnouncement = user.getLastFetchedAnnouncement();
		List<Announcement> announcements = announcementRepo.findByIdGreaterThanOrderByIdDesc(lastAnnouncement);
		if (!announcements.isEmpty()) {
			user.setLastFetchedAnnouncement(announcements.get(0).getId());
			deviceRepo.save(user);
		}

		filter(principal, announcements);

		return announcements;
	}

	@GetMapping("/recent")
	public Collection<Announcement> recentAnnouncementsFiltered(@AuthenticationPrincipal Principal principal,
			@RequestParam(value = "offset", defaultValue = "0", required = false) long offset) {
		long topId = announcementRepo.findFirstByOrderByIdDesc().getId() - offset;
		long endId = topId - 100;

		List<Announcement> announcements = announcementRepo.findByIdBetweenOrderByIdDesc(endId, topId);

		Device user = getDevice(principal);
		if (!announcements.isEmpty() && user.getLastFetchedAnnouncement() < topId) {
			user.setLastFetchedAnnouncement(topId);
			deviceRepo.save(user);
		}

		filter(principal, announcements);
		return announcements;
	}

	@GetMapping("/search")
	public Collection<Announcement> search(@AuthenticationPrincipal Principal principal, @RequestParam String query) {
		List<Announcement> announcements = search(query);
		filter(principal, announcements);
		return announcements;
	}

	private List<Announcement> search(String query) {
		List<Company> companies = companyRepo.findByIdStartingWith(query);
		if (companies.size() == 1)
			return announcementRepo.findFirst50ByCompanyOrderByIdDesc(companies.get(0));

		try {
			AnnouncementType type = AnnouncementType.of(query);
			if (type != null)
				return announcementRepo.findFirst50ByTypeOrderByIdDesc(type);
		}
		catch (Exception e) {}

		return announcementRepo.findFirst50ByTitleContainingOrderByIdDesc(query);
	}

	private void filter(Principal principal, Collection<Announcement> announcements) {
		if (announcements.isEmpty())
			return;

		Device user = getDevice(principal);
		AccountConfig config = configRepo.findByAccountID(user.getAccountID());

		Set<Company> cBlacklist = config.getCompanyBlacklist();
		Set<String> kwBlacklist = config.getKeywordBlacklist();
		Set<AnnouncementType> tBlacklist = config.getTypeBlacklist();

		announcements.removeIf((a) -> {
			if (cBlacklist.contains(a.getCompany()) || tBlacklist.contains(a.getType()))
				return true;
			for (String keyword : kwBlacklist)
				if (a.getTitle().toLowerCase().contains(keyword.toLowerCase()))
					return true;
			return false;
		});
	}
}
