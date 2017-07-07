package seop.gyun.recipedia;

public class JsonBuilder {

	private StringBuilder sb;

	public JsonBuilder(String key, String value) {
		sb = new StringBuilder("{'").append(key).append("':'").append(value).append("'");
	}
	
	public JsonBuilder append(String key, String value) {
		sb.append(",'").append(key).append("':'").append(value).append("'");
		return this;
	}

	public String build() {
		return sb.append("}").toString();
	}
}
