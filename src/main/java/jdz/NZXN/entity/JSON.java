
package jdz.NZXN.entity;

public class JSON {
	public static String extractFirst(String json) {
		if (!json.contains(":"))
			return json;
		String s = json.substring(json.indexOf(":") + 1, json.lastIndexOf("}"));
		if (s.startsWith("\"") | s.startsWith("'"))
			s = s.substring(1, s.length() - 1);
		return s;
	}
}
