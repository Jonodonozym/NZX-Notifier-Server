
package jdz.NZXN.dataFetch;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import jdz.NZXN.entity.announcement.Announcement;
import jdz.NZXN.entity.company.Company;

public interface AnnouncementFetcher {
	public List<Announcement> fetchAllAfter(Calendar time) throws IOException;
	public List<Announcement> fetchAll(Company company) throws IOException;
}
