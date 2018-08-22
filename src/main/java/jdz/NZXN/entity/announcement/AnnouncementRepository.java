
package jdz.NZXN.entity.announcement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jdz.NZXN.entity.company.Company;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	public Announcement findFirstByOrderByIdDesc();

	public Optional<Announcement> findById(long id);

	public List<Announcement> findByIdGreaterThanOrderByIdDesc(Long id);

	public List<Announcement> findByIdBetweenOrderByIdDesc(long idStart, long idEnd);

	public List<Announcement> findFirst50ByCompanyOrderByIdDesc(Company company);

	public List<Announcement> findFirst50ByTypeOrderByIdDesc(AnnouncementType type);

	public List<Announcement> findFirst50ByTitleContainingOrderByIdDesc(String query);

	public Announcement findTopByOrderByTimeDesc();
}
