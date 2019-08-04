
package jdz.NZXN.dataFetch.tasks;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jdz.NZXN.dataFetch.nzx.NZXAnnouncementFetcher;
import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementRepository;

@Component
public class AnnouncementUpdateTask {
	private final Logger log = LoggerFactory.getLogger(NZXAnnouncementFetcher.class);

	@Autowired private AnnouncementRepository repository;
	@Autowired private NZXAnnouncementFetcher fetcher;

	@Scheduled(fixedRate = 60000, initialDelay = 5000)
	public void update() throws IOException {
		Announcement lastAnnouncement = repository.findTopByOrderByTimeDesc();
		Calendar lastAnnouncementTime = lastAnnouncement == null ? Calendar.getInstance() : lastAnnouncement.getTime();
		if (lastAnnouncement == null)
			lastAnnouncementTime.set(1990, 0, 0);

		List<Announcement> announcements = fetcher.fetchAllAfter(lastAnnouncementTime);
		repository.saveAll(announcements);
		if (!announcements.isEmpty())
			log.info("Fetched " + announcements.size() + " new announcement" + (announcements.size() > 1 ? "s" : ""));
	}
}
