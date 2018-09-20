package ro.gss.database.utils;

import org.springframework.util.StringUtils;

public enum SearchTypeEnum {
	NUMBER, DATE, DATE_TIME, STRING, SELECT, CLOB, HOUR, LIST, SELECT2; // default: STRING

	public static SearchTypeEnum getByName(String name) {
		if (StringUtils.hasLength(name)) {
			for (SearchTypeEnum en : values()) {
				if (en.name().equalsIgnoreCase(name)) {
					return en;
				}
			}
			throw new IllegalArgumentException("No search type found for [" + name + "]");
		}
		return STRING;
	}

}
