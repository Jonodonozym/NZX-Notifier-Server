
package jdz.NZXN.entity.announcement;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jdz.NZXN.entity.company.Company;
import lombok.Getter;
import lombok.ToString;

@Entity
@ToString(exclude="time")
@Table(indexes = {
		@Index(name = "index_time", columnList="time", unique = false),
		@Index(name = "index_company", columnList="companyId", unique = false),
		@Index(name = "index_type", columnList="type", unique = false)
})
public class Announcement {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private Long id;

	@ManyToOne
	@JoinColumn(name = "companyId", referencedColumnName = "Id")
	@Getter private Company company;

	@Getter private String title;
	@Getter private String url;
	@Getter private String pdfUrl;

	@Getter private AnnouncementType type;

	@Temporal(TemporalType.TIMESTAMP) @Getter private Calendar time;

	protected Announcement() {}

	public Announcement(Company company, String title, String url, String pdfUrl, AnnouncementType type,
			Calendar time) {
		this.company = company;
		this.title = title;
		this.url = url;
		this.pdfUrl = pdfUrl;
		this.type = type;
		this.time = time;
	}
}
