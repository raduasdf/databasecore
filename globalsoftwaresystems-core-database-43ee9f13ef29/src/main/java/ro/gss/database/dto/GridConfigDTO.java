package ro.gss.database.dto;

public class GridConfigDTO {
	private String title;
	private String field;
	private Boolean visible;
	private Boolean isEditable;
	private String columnType;
	private String op;
	private Boolean searchShow;
	private String searchType;
	private String searchSource;
	private Boolean isEligibleForAlter;
	private String validation;
	private Boolean detailsVisible;
	private String filterValidation;
	private Boolean showFilter;
	private String columnLabel;
	private Boolean isVisibleCreateOnly;
	private String width;
	private String nomCode;

	public GridConfigDTO(Boolean searchShow, String title, String field, Boolean visible, String op, String searchType,
			String searchSource, Boolean isEditable, String columnType, Boolean isEligibleForAlter, String validation,
			Boolean detailsVisible, String filterValidation, Boolean showFilter, String columnLabel,
			Boolean isVisibleCreateOnly) {
		this.searchShow = searchShow;
		this.title = title;
		this.field = field;
		this.visible = visible;
		this.op = op;
		this.searchType = searchType;
		this.searchSource = searchSource;
		this.isEditable = isEditable;
		this.columnType = columnType;
		this.isEligibleForAlter = isEligibleForAlter;
		this.validation = validation;
		this.detailsVisible = detailsVisible;
		this.filterValidation = filterValidation;
		this.showFilter = showFilter;
		this.columnLabel = columnLabel;
		this.isVisibleCreateOnly = isVisibleCreateOnly;

	}

	public GridConfigDTO(Boolean searchShow, String title, String field, Boolean visible, String op, String searchType,
			String searchSource, Boolean isEditable, String columnType, Boolean isEligibleForAlter) {
		this.searchShow = searchShow;
		this.title = title;
		this.field = field;
		this.visible = visible;
		this.op = op;
		this.searchType = searchType;
		this.searchSource = searchSource;
		this.isEditable = isEditable;
		this.columnType = columnType;
		this.isEligibleForAlter = isEligibleForAlter;
		this.columnLabel = title;
	}

	public GridConfigDTO(String field, Boolean visible, String columnType, String op, Boolean searchShow,
			String searchSource) {
		this.field = field;
		this.visible = visible;
		this.columnType = columnType;
		this.op = op;
		this.searchShow = searchShow;
		this.searchSource = searchSource;
	}

	public GridConfigDTO(String field, Boolean visible, String columnType, String op, Boolean searchShow,
			String searchSource, String nomCode) {
		super();
		this.field = field;
		this.visible = visible;
		this.columnType = columnType;
		this.op = op;
		this.searchShow = searchShow;
		this.searchSource = searchSource;
		this.nomCode = nomCode;
	}

	public String getNomCode() {
		return nomCode;
	}

	public void setNomCode(String nomCode) {
		this.nomCode = nomCode;
	}

	public String getFilterValidation() {
		return filterValidation;
	}

	public void setFilterValidation(String filterValidation) {
		this.filterValidation = filterValidation;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getSearchSource() {
		return searchSource;
	}

	public void setSearchSource(String searchSource) {
		this.searchSource = searchSource;
	}

	public String getSearchType() {

		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Boolean getVisible() {

		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getField() {

		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {

		return title;
	}

	public Boolean getSearchShow() {
		return searchShow;
	}

	public void setSearchShow(Boolean searchShow) {
		this.searchShow = searchShow;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public Boolean getIsEligibleForAlter() {
		return isEligibleForAlter;
	}

	public void setIsEligibleForAlter(Boolean isEligibleForAlter) {
		this.isEligibleForAlter = isEligibleForAlter;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public Boolean getDetailsVisible() {
		return detailsVisible;
	}

	public void setDetailsVisible(Boolean detailsVisible) {
		this.detailsVisible = detailsVisible;
	}

	public Boolean getShowFilter() {
		return showFilter;
	}

	public void setShowFilter(Boolean showFilter) {
		this.showFilter = showFilter;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public Boolean getIsVisibleCreateOnly() {
		return isVisibleCreateOnly;
	}

	public void setIsVisibleCreateOnly(Boolean isVisibleCreateOnly) {
		this.isVisibleCreateOnly = isVisibleCreateOnly;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}
