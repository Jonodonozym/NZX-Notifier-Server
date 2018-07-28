
package jdz.NZXN.entity.announcement;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(indexes = @Index(name = "index_time", columnList="time", unique = false))
public class Announcement {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private Long announcementId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	@Getter private Company company;

	@Getter private String title;
	@Getter private String url;
	@Getter private String pdfUrl;

	@Getter private AnnouncementTypes type;

	@Temporal(TemporalType.TIMESTAMP) @Getter private Calendar time;

	protected Announcement() {}

	public Announcement(Company company, String title, String url, String pdfUrl, AnnouncementTypes type,
			Calendar time) {
		this.company = company;
		this.title = title;
		this.url = url;
		this.pdfUrl = pdfUrl;
		this.type = type;
		this.time = time;
	}
}
