package ro.gss.database.dto;

public class SorterDTO {
	private String key;
	private String direction;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public SorterDTO(String key, String direction) {
		super();
		this.key = key;
		this.direction = direction;
	}

	public SorterDTO() {
		super();
	}

	@Override
	public String toString() {
		return "SorterDTO [key=" + key + ", direction=" + direction + "]";
	}
}