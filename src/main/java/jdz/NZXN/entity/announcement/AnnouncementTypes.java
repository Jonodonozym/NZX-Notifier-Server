
package jdz.NZXN.entity.announcement;

public enum AnnouncementTypes {
	GENERAL, MEETING, SHINTR, MKTUPDTE, TRANSACT, ADMIN, SECISSUE, ANNREP, FLLYR, HLFYR, OTHER;
	
	public static AnnouncementTypes of(String string) {
		try {
			return AnnouncementTypes.valueOf(string.replaceAll("/", ""));
		}
		catch (Exception e) {
			return AnnouncementTypes.OTHER;
		}
	}
}
