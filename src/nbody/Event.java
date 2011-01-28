package nbody;

public class Event {

	private String descr;
	private String sourceName;

	public Event(String descr, String sourceName) {
		this.descr = descr;
		this.sourceName = sourceName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getDescription() {
		return descr;
	}
}
