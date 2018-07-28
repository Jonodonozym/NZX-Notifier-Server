
package jdz.NZXN;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import jdz.NZXN.entity.company.CompanyRepository;
import jdz.NZXN.service.NZXAnnouncementDataFetcher;
import jdz.NZXN.service.NZXCompanyDataFetcher;

@SpringBootApplication
@EnableScheduling
public class NZXNServer {	
    public static void main(String[] args) {
        SpringApplication.run(NZXNServer.class, args);
    }
    
    @Bean
	public CommandLineRunner demo(CompanyRepository companyRepo, NZXCompanyDataFetcher companyDataFetcher, NZXAnnouncementDataFetcher announcementDataFetcher) {
    	return (args) -> {
    		companyDataFetcher.update();
		};
	}
}