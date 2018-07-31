
package jdz.NZXN.entity.announcement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jdz.NZXN.entity.company.Company;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	public Announcement findFirstByOrderByIdDesc();
	
	public Optional<Announcement> findById(long id);
	
	public List<Announcement> findByIdGreaterThan(Long id);
	public List<Announcement> findByIdBetween(long idStart, long idEnd);
	
	public List<Announcement> findFirst50ByCompany(Company company);
	public List<Announcement> findFirst50ByType(AnnouncementType type);
	public List<Announcement> findFirst50ByTitleContaining(String query);
	
	public Announcement findTopByOrderByTimeDesc();
}
