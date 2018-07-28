
package jdz.NZXN;

import java.util.Calendar;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.announcement.AnnouncementRepository;
import jdz.NZXN.entity.announcement.AnnouncementTypes;
import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;
import jdz.NZXN.entity.user.User;
import jdz.NZXN.entity.user.UserConfig;
import jdz.NZXN.entity.user.UserRepository;

@SpringBootApplication
@EnableScheduling
public class NZXNServer {	
    public static void main(String[] args) {
        SpringApplication.run(NZXNServer.class, args);
    }
    
    @Bean
	public CommandLineRunner demo(UserRepository userRepo, AnnouncementRepository annRepo, CompanyRepository compRepo) {
    	return (args) -> {
			if (userRepo.findByUsername("Jaiden Baker") == null)
			userRepo.save(new User("Jaiden Baker", "Jouel383841", "jaidencolebaker@gmail.com"));

			if (compRepo.findByCompanyId("ATM") == null)
				compRepo.save(new Company("ATM", "atm", "null"));
			
			Company comp = compRepo.findByCompanyId("ATM");
			
			if (!annRepo.findAll().iterator().hasNext())
				annRepo.save(new Announcement(comp, "milk thing happened", "", "", AnnouncementTypes.ADMIN, Calendar.getInstance()));
		
			UserConfig config = userRepo.findByUsername("Jaiden Baker").getUserConfig();
			System.out.println(config);
			
			List<Announcement> announcements = annRepo.findByCompanyCompanyIdContainingOrderByTimeDesc("AT");
			System.out.println(announcements.size());
		};
	}
}