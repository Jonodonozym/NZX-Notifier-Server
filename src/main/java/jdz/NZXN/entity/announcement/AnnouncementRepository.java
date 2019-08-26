
package jdz.NZXN.entity.announcement;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jdz.NZXN.entity.announcement.Announcement.AnnouncementID;
import jdz.NZXN.entity.company.Company;

public interface AnnouncementRepository extends JpaRepository<Announcement, AnnouncementID> {
	@Query("SELECT a FROM Announcement ORDER BY a.id.time DESC")
	public Page<Announcement> getLatest(Pageable pageable);
	
	@Query("select a from Announcement a where a.id.time > ?1 ORDER BY a.id.time DESC")
	public List<Announcement> getAllAfter(Calendar time);

	public List<Announcement> findFirst50ByIdCompanyOrderByIdTimeDesc(Company company);
	public List<Announcement> findFirst50ByTypeOrderByIdTimeDesc(AnnouncementType type);
	public List<Announcement> findFirst50ByIdTitleContainingOrderByIdTimeDesc(String query);

	public Announcement findTopByOrderByIdTimeDesc();
}
