
package jdz.NZXN.entity.announcement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jdz.NZXN.entity.company.Company;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	public Optional<Announcement> findById(Long id);
	public List<Announcement> findByIdGreaterThanOrderByIdDesc(Long id);
	
	public List<Announcement> findFirst50ByCompany(Company company);
	public List<Announcement> findFirst50ByType(AnnouncementType type);
	public List<Announcement> findFirst50ByTitleContaining(String query);
	
	public Announcement findTopByOrderByTimeDesc();
}
