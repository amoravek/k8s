package k8s.sample.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutData {
	private String type;
	private String value;

	public OutData() {
		// Jackson deserialization
	}

	public OutData(String type, String value) {
		this.type = type;
		this.value = value;
	}

	@JsonProperty
	public String getType() {
		return this.type;
	}

	@JsonProperty
	public String getValue() {
		return this.value;
	}

}