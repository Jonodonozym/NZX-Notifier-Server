
package jdz.NZXN.dataFetch.tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jdz.NZXN.dataFetch.nzx.NZXCompanyFetcher;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;

@Component
public class CompaniesUpdateTask {
	private final Logger log = LoggerFactory.getLogger(NZXCompanyFetcher.class);
	
	@Autowired private CompanyRepository repository;
	@Autowired private NZXCompanyFetcher fetcher;

	@Scheduled(cron = "0 30 9 * * MON")
	public void update() {
		Set<Company> repoCompanies = new HashSet<>(repository.findAll());

		List<Company> nzxCompanies = fetcher.fetchAll();
		nzxCompanies.removeIf((c) -> {
			return repoCompanies.contains(c);
		});

		for (Company company : nzxCompanies)
			repository.save(company);

		if (!nzxCompanies.isEmpty())
			log.info("Fetched " + nzxCompanies.size() + " new compan" + (nzxCompanies.size() > 1 ? "ies" : "y"));
	}

}
