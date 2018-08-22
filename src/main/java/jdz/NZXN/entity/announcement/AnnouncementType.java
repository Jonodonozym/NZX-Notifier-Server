
package jdz.NZXN.entity.announcement;

public enum AnnouncementType {
	GENERAL, MEETING, SHINTR, MKTUPDTE, TRANSACT, ADMIN, SECISSUE, ANNREP, FLLYR, HALFYR, DAIRY, OTHER;

	public static AnnouncementType of(String string) {
		try {
			return AnnouncementType.valueOf(string.replaceAll("/", ""));
		}
		catch (Exception e) {
			return null;
		}
	}
}
