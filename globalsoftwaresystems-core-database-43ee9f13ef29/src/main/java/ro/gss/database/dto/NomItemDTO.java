package ro.gss.database.dto;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;

@Data
public class NomItemDTO {
	private Long id;
	private String code;
	private String value;
	private String typeCode;
	private String typeName;

	public NomItemDTO() {

	}

	@QueryProjection
	public NomItemDTO(Long id, String code, String value) {
		super();
		this.id = id;
		this.code = code;
		this.value = value;
	}

	@QueryProjection
	public NomItemDTO(Long id, String code, String value, String typeCode, String typeName) {
		super();
		this.id = id;
		this.code = code;
		this.value = value;
		this.typeCode = typeCode;
		this.typeName = typeName;
	}
}