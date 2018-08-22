
package jdz.NZXN.NZXDataFetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;

@Component
public class NZXCompanyDataFetcher {
	private static final String NZXrootURL = "https://nzx.com";
	private static final String companiesListURL = "https://www.nzx.com/markets/NZSX";
	private static final String companiesTable = "table.instruments-table";

	private final Logger log = LoggerFactory.getLogger(NZXCompanyDataFetcher.class);

	@Autowired
	private CompanyRepository repository;

	@Scheduled(cron = "0 30 9 * * MON")
	public void update() {
		Set<Company> repoCompanies = new HashSet<Company>(repository.findAll());

		List<Company> nzxCompanies = fetchAll();
		nzxCompanies.removeIf((c) -> {
			return repoCompanies.contains(c);
		});

		for (Company company : nzxCompanies)
			repository.save(company);

		if (!nzxCompanies.isEmpty())
			log.info("Fetched " + nzxCompanies.size() + " new compan" + (nzxCompanies.size() > 1 ? "ies" : "y"));
	}

	private List<Company> fetchAll() {
		List<Company> companies = new ArrayList<Company>();
		Document doc;
		try {
			doc = Jsoup.connect(companiesListURL).get();
		}
		catch (IOException e) {
			return companies;
		}

		Element table = doc.select(companiesTable).select("tbody").get(0);

		for (Element row : table.select("tr")) {
			Elements tableRowElements = row.select("td");

			if (tableRowElements.isEmpty())
				continue;

			Company company = parseCompany(tableRowElements);
			if (company != null)
				companies.add(company);
		}

		companies.add(new Company("NZXR", "NZX Regulations", "https://www.nzx.com/regulation/NZXR/announcements"));
		companies.add(new Company("NZXO", "NZX Operations", "https://www.nzx.com/regulation/NZXO/announcements"));
		return companies;
	}

	private Company parseCompany(Elements tableRowElements) {
		Element companyCell = tableRowElements.select("td[data-title=Company]").select("span").first();

		String companyName = companyCell.text();
		if (companyName.contains("Limited"))
			companyName = companyName.substring(0, companyName.indexOf("Limited")) + "Ltd.";
		companyName = companyName.replaceAll("\\(NS\\)", "").trim();

		String companyURL = NZXrootURL + companyCell.select("a").attr("href");
		String companyID = companyURL.substring(companyURL.lastIndexOf("/") + 1);
		return new Company(companyID, companyName, companyURL);
	}
}
