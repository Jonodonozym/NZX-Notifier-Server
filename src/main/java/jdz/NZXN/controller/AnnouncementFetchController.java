
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementRepository;
import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;
import jdz.NZXN.entity.user.User;
import jdz.NZXN.entity.user.UserConfig;
import jdz.NZXN.entity.user.UserRepository;

@RestController
@RequestMapping("/announcements")
public class AnnouncementFetchController {
	@Autowired private AnnouncementRepository repository;
	@Autowired private UserRepository userRepo;
	@Autowired private CompanyRepository companyRepo;

	@GetMapping("/new")
	public Collection<Announcement> newAnnouncements(Principal principal) {
		User user = userRepo.findByUsername(principal.getName());
		Long lastAnnouncement = user.getLastFetchedAnnouncement();
		List<Announcement> announcements = repository.findByIdGreaterThanOrderByIdDesc(lastAnnouncement);
		if (!announcements.isEmpty()) {
			user.setLastFetchedAnnouncement(announcements.get(0).getId());
			userRepo.save(user);
		}

		filter(principal, announcements);

		return announcements;
	}

	@GetMapping("/search")
	public Collection<Announcement> search(Principal principal, @RequestParam String query) {
		List<Announcement> announcements = search(query);
		filter(principal, announcements);
		return announcements;
	}
	
	private List<Announcement> search(String query) {
		List<Company> companies = companyRepo.findByIdStartingWith(query);
		if (companies.size() == 1)
			return repository.findFirst50ByCompany(companies.get(0));

		AnnouncementType type = AnnouncementType.of(query);
		if (type != null)
			return repository.findFirst50ByType(type);

		return repository.findFirst50ByTitleContaining(query);
	}

	private void filter(Principal principal, Collection<Announcement> announcements) {
		if (announcements.isEmpty())
			return;
		
		User user = userRepo.findByUsername(principal.getName());
		UserConfig config = user.getUserConfig();
		
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
