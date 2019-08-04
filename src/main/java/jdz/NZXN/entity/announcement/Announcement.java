
package jdz.NZXN.entity.announcement;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jdz.NZXN.entity.company.Company;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@EqualsAndHashCode(of = { "id" })
@ToString
@Table(indexes = { @Index(name = "index_time", columnList = "id.time", unique = false),
		@Index(name = "index_company", columnList = "id.companyId", unique = false),
		@Index(name = "index_type", columnList = "type", unique = false) })
public class Announcement {
	@Embeddable
	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	@ToString(exclude = { "time" })
	public static class AnnouncementID implements Serializable {
		private static final long serialVersionUID = 1954776663036659457L;
		private String title;
		@ManyToOne(cascade = CascadeType.MERGE) @JoinColumn(name = "companyId", referencedColumnName = "Id") private Company company;
		@Temporal(TemporalType.TIMESTAMP) private Calendar time;
	}

	@EmbeddedId private AnnouncementID id;

	@Getter private String url;
	@Getter private String pdfUrl;

	@Getter private AnnouncementType type;

	protected Announcement() {}

	public Announcement(Company company, String title, String url, String pdfUrl, AnnouncementType type,
			Calendar time) {
		this.id = new AnnouncementID(title, company, time);
		this.url = url;
		this.pdfUrl = pdfUrl;
		this.type = type;
	}

	public String getTitle() {
		return id.title;
	}

	public Company getCompany() {
		return id.company;
	}

	public Calendar getTime() {
		return id.time;
	}
}
