
package jdz.NZXN.dataFetch.tasks;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jdz.NZXN.dataFetch.nzx.NZXAnnouncementFetcher;
import jdz.NZXN.entity.announcement.AnnouncementRepository;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;

@Component
public class CompleteHistoryFetchTask {
	@Autowired private CompaniesUpdateTask companyUpdater;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private AnnouncementRepository announcementRepository;
	@Autowired private NZXAnnouncementFetcher announcementFetcher;

	public void update() throws IOException {
		companyUpdater.update();
		ExecutorService executor = Executors.newFixedThreadPool((int) companyRepository.count());
		for (Company company : companyRepository.findAll())
			executor.execute(() -> {
				try {
					announcementRepository.saveAll(announcementFetcher.fetchAll(company));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			});
	}
}
