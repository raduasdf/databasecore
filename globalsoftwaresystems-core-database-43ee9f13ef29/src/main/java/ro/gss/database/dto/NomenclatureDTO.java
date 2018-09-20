package ro.gss.database.dto;

import java.util.Date;

import javax.annotation.concurrent.Immutable;

import com.mysema.query.annotations.QueryProjection;

@Immutable
public class NomenclatureDTO {

	private Long id;
	private Long typeId;
	private String typeCode;
	private String code;
	private String value;
	private Date startDate;
	private Date endDate;

	@QueryProjection
	public NomenclatureDTO() {
	}

	@QueryProjection
	public NomenclatureDTO(Long id, Long typeId, String typeCode, String code, String value, Date startDate,
			Date endDate) {
		super();
		this.id = id;
		this.typeId = typeId;
		this.typeCode = typeCode;
		this.code = code;
		this.value = value;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "NomenclatureDTO [id=" + id + ", typeId=" + typeId + ", typeCode=" + typeCode + ", code=" + code
				+ ", value=" + value + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	

}
