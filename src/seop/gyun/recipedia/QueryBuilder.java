package seop.gyun.recipedia;

public class QueryBuilder {

	private StringBuilder sb;

	public QueryBuilder(String key, String value, boolean isBody) {
		if (isBody) {
			sb = new StringBuilder(key).append("=").append(value);
		} else {
			sb = new StringBuilder("?").append(key).append("=").append(value);
		}
	}
	
	public QueryBuilder append(String key, String value) {
		sb.append("&").append(key).append("=").append(value);
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
