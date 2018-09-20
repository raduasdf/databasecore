package ro.gss.database.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.concurrent.Immutable;

import com.mysema.query.annotations.QueryProjection;

@Immutable
public class GenericDTO {

	private final Long id;
	private String code;
	private final String value;
	private final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	@QueryProjection
	public GenericDTO(Long id, String value) {
		this.id = id;
		this.value = value;
	}

	@QueryProjection
	public GenericDTO(Long id, Long value) {
		this.id = id;
		this.value = "" + value;
	}
	
	@QueryProjection
	public GenericDTO(Long id, String code, String value) {
		this.id = id;
		this.code = code;
		this.value = value;
	}
	
	@QueryProjection
	public GenericDTO(Long id, String code, Long value) {
		this.id = id;
		this.code = code;
		this.value = "" + value;
	}

	@QueryProjection
	public GenericDTO(Long id, Date date) {
		this.id = id;
		this.value = date != null ? defaultDateFormat.format(date) : "";
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "GenericDTO [id=" + id + ", value=" + value + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GenericDTO that = (GenericDTO) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		return !(value != null ? !value.equals(that.value) : that.value != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}
}
