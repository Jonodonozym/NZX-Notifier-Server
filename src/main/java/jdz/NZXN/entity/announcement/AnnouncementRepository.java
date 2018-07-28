
package jdz.NZXN.entity.announcement;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	public Announcement findByAnnouncementId(Long announcement_id);
	public List<Announcement> findByTimeGreaterThanOrderByTime(Calendar time);
	
	public List<Announcement> findByCompanyCompanyIdContainingOrderByTimeDesc(String companyId);
	public Announcement findTopByOrderByTimeDesc();
}
