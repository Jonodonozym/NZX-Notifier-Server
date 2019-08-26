
package jdz.NZXN.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementRepository;
import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;

@RestController
@RequestMapping("/announcements")
public class AnnouncementFetchController {
	@Autowired private AnnouncementRepository announcementRepo;
	@Autowired private CompanyRepository companyRepo;

	@GetMapping("/page")
	public Collection<Announcement> getPage(@RequestParam int page, @RequestParam(defaultValue = "100") int pageSize) {
		return announcementRepo.getLatest(PageRequest.of(page, pageSize)).getContent();
	}

	@GetMapping("/new")
	public Collection<Announcement> newAnnouncements(@AuthenticationPrincipal Principal principal,
			@RequestParam long latestAnnouncementTime) {
		Calendar latestTime = Calendar.getInstance();
		latestTime.setTimeInMillis(latestAnnouncementTime);
		List<Announcement> announcements = announcementRepo.getAllAfter(latestTime);

		if (announcements.isEmpty())
			return announcements;

		return announcements;
	}

	@GetMapping("/search")
	public Collection<Announcement> search(@AuthenticationPrincipal Principal principal, @RequestParam String query) {
		List<Announcement> announcements = search(query);
		return announcements;
	}

	private List<Announcement> search(String query) {
		List<Company> companies = companyRepo.findByIdStartingWith(query);
		if (companies.size() == 1)
			return announcementRepo.findFirst50ByIdCompanyOrderByIdTimeDesc(companies.get(0));

		try {
			AnnouncementType type = AnnouncementType.of(query);
			if (type != null)
				return announcementRepo.findFirst50ByTypeOrderByIdTimeDesc(type);
		}
		catch (Exception e) {}

		return announcementRepo.findFirst50ByIdTitleContainingOrderByIdTimeDesc(query);
	}
}
