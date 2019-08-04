
package jdz.NZXN.dataFetch.nzx;

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
import org.springframework.stereotype.Component;

import jdz.NZXN.dataFetch.AnnouncementFetcher;
import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementType;
import jdz.NZXN.entity.company.Company;

@Component
public class NZXAnnouncementFetcher implements AnnouncementFetcher {
	private static final String NZXrootURL = "https://nzx.com";
	private static final String NZXannouncementsURL = "https://nzx.com/markets/NZSX/announcements";
	private static final String announcementsTable = "table.table-to-list.announcements-table";
	private static DateFormat NZXTimeFormat = new SimpleDateFormat("d/M/yyyy, h:mm a");

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("NZ"));
	}

	public List<Announcement> fetchAllAfter(Calendar time) throws IOException {
		Document doc = Jsoup.connect(NZXannouncementsURL).get();
		Element table = doc.selectFirst(announcementsTable);

		List<Announcement> announcements = parseAnnouncementTable(table);
		announcements.removeIf((announcement) -> announcement.getTime().compareTo(time) <= 0);

		Collections.reverse(announcements);
		return announcements;
	}

	private List<Announcement> parseAnnouncementTable(Element table) {
		List<Announcement> announcements = new ArrayList<>();
		for (Element row : table.selectFirst("tbody").select("tr")) {
			try {
				announcements.add(parseAnnouncement(row));
			}
			catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return announcements;
	}

	private Announcement parseAnnouncement(Element row) throws IOException, ParseException {
		Elements tableRowElements = row.select("td");
		if (tableRowElements.isEmpty())
			throw new IOException("table row is empty");

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

	private Calendar parseTime(String timeText) throws IOException, ParseException {
		Date date = NZXTimeFormat.parse(timeText.replaceAll("pm", "PM").replaceAll("am", "AM"));
		Calendar announcementTime = Calendar.getInstance();
		announcementTime.setTime(date);
		return announcementTime;
	}

	private String getPDFURL(String announcementURL) throws IOException {
		Document doc = Jsoup.connect(announcementURL).get();
		Elements attatchments = doc.select("div.panel.module.documents").select("ul").select("li");
		return attatchments.first().select("a").attr("href");
	}

	public List<Announcement> fetchAll(Company company) throws IOException {
		List<Announcement> announcements = new ArrayList<>();
		for (String year : fetchAllYears(company)) {
			Document doc = Jsoup
					.connect("https://www.nzx.com/companies/" + company.getId() + "/announcements?year=" + year).get();
			Element table = doc.selectFirst("table[class='table-to-list announcements-table'");
			announcements.addAll(parseAnnouncementTable(table));
		}
		return announcements;
	}

	private List<String> fetchAllYears(Company company) throws IOException {
		Document doc = Jsoup.connect("https://www.nzx.com/companies/" + company.getId() + "/announcements").get();
		Elements selectorOptions = doc.selectFirst("select[id='year']").children();
		List<String> years = new ArrayList<>(selectorOptions.size());
		for (Element option : selectorOptions)
			years.add(option.text());
		return years;
	}
}
