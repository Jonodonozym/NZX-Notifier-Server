
package jdz.NZXN.dataFetch.nzx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import jdz.NZXN.dataFetch.CompanyFetcher;
import jdz.NZXN.entity.company.Company;

@Component
public class NZXCompanyFetcher implements CompanyFetcher {
	private static final String NZXrootURL = "https://nzx.com";
	private static final String companiesListURL = "https://www.nzx.com/markets/NZSX";
	private static final String companiesTable = "table.instruments-table";

	public List<Company> fetchAll() {
		List<Company> companies = new ArrayList<>();
		Document doc;
		try {
			doc = Jsoup.connect(companiesListURL).get();
		}
		catch (IOException e) {
			return companies;
		}

		Element table = doc.selectFirst(companiesTable).selectFirst("tbody");

		for (Element row : table.select("tr")) {
			Elements tableRowElements = row.select("td");

			if (tableRowElements.isEmpty())
				continue;

			companies.add(parseCompany(tableRowElements));
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
