package ro.gss.database.dto;

public class FilterDTO {

	private String key;

	private String type;

	private String value;

	private String op;

	private String builder;

	public FilterDTO() {
		super();
	}

	public FilterDTO(String key, String type, String value, String op) {
		super();
		this.key = key;
		this.type = type;
		this.value = value;
		this.op = op;
	}

	public FilterDTO(String key, String type, String value, String op, String builder) {
		super();
		this.key = key;
		this.type = type;
		this.value = value;
		this.op = op;
		this.builder = builder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getBuilder() {
		return builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
	}

	@Override
	public String toString() {
		return "FilterDTO [key=" + key + ", type=" + type + ", value=" + value + ", op=" + op + ", builder=" + builder
				+ "]";
	}
	
}
