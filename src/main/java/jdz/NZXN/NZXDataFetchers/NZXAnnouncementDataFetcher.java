
package jdz.NZXN.NZXDataFetchers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementRepository;
import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;

@Component
public class NZXAnnouncementDataFetcher {
	private static final String NZXrootURL = "https://nzx.com";
	private static final String NZXannouncementsURL = "https://nzx.com/markets/NZSX/announcements";
	private static final String announcementsTable = "table.table-to-list.announcements-table";
	private static DateFormat NZXTimeFormat = new SimpleDateFormat("d/M/yyyy, h:mm a");
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("NZ"));
	}

	private final Logger log = LoggerFactory.getLogger(NZXAnnouncementDataFetcher.class);

	@Autowired private AnnouncementRepository repository;

	@Scheduled(fixedRate = 60000, initialDelay = 5000)
	public void update() {
		Announcement lastAnnouncement = repository.findTopByOrderByTimeDesc();
		Calendar lastAnnouncementTime = lastAnnouncement == null ? Calendar.getInstance() : lastAnnouncement.getTime();
		if (lastAnnouncement == null)
			lastAnnouncementTime.set(1990, 0, 0);
		List<Announcement> announcements = fetchAllAfter(lastAnnouncementTime);
		for (Announcement announcement : announcements)
			repository.save(announcement);

		if (!announcements.isEmpty())
			log.info("Fetched " + announcements.size() + " new announcement" + (announcements.size() > 1 ? "s" : ""));
	}

	private List<Announcement> fetchAllAfter(Calendar time) {
		List<Announcement> announcements = new ArrayList<>();
		Document doc;
		try {
			doc = Jsoup.connect(NZXannouncementsURL).get();
		}
		catch (IOException e) {
			return announcements;
		}

		Element table = doc.select(announcementsTable).select("tbody").get(0);

		for (Element row : table.select("tr")) {
			Elements tableRowElements = row.select("td");

			if (tableRowElements.isEmpty())
				continue;

			Announcement announcement = parseAnnouncement(tableRowElements);
			if (announcement == null)
				continue;

			if (announcement.getTime().compareTo(time) <= 0)
				break;

			announcements.add(announcement);

			System.out
					.println("New announcement: " + announcement.getCompany().getId() + ", " + announcement.getTitle());
		}

		Collections.reverse(announcements);

		return announcements;
	}

	private Announcement parseAnnouncement(Elements tableRowElements) {
		String timeText = tableRowElements.select("td[data-title=Date]").select("span").first().ownText();
		Calendar announcementTime = parseTime(timeText);

		String companyURL = tableRowElements.select("td[data-title=Company]").select("span").select("a").attr("href");
		String companyId = companyURL.substring(companyURL.lastIndexOf("/") + 1);

		Company company = new Company(companyId);

		String typeText = tableRowElements.select("td[data-title=Type]").select("span").text();
		AnnouncementType type = AnnouncementType.of(typeText);
		if (type == null)
			type = AnnouncementType.OTHER;

		String title = tableRowElements.select("td[data-title=Title]").select("span").select("a").text()
				.replaceAll("\\\\|/|:", "-").replaceAll("\"", "");

		String url = NZXrootURL
				+ tableRowElements.select("td[data-title=Title]").select("span").select("a").attr("href");
		String pdfURL = getPDFURL(url);

		Announcement a = new Announcement(company, title, url, pdfURL, type, announcementTime);
		return a;
	}

	private Calendar parseTime(String timeText) {
		Calendar announcementTime = Calendar.getInstance();

		try {
			Date date = NZXTimeFormat.parse(timeText.replaceAll("pm", "PM").replaceAll("am", "AM"));
			announcementTime.setTime(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
			announcementTime.set(1990, 0, 0);
		}
		return announcementTime;
	}

	private String getPDFURL(String announcementURL) {
		Document doc;
		try {
			doc = Jsoup.connect(announcementURL).get();
		}
		catch (IOException e) {
			return announcementURL;
		}

		Elements attatchments = doc.select("div.panel.module.documents").select("ul").select("li");
		return attatchments.first().select("a").attr("href");
	}
}
