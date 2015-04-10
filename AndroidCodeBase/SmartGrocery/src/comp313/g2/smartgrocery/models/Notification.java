package comp313.g2.smartgrocery.models;

public class Notification {
	//private fields
	private int id;
	private String from, subject, text, date;
	private boolean isRead = false;
	
	//constructors
	public Notification(int id, String from, String subject, String text, String date){
		this.id = id;
		this.from = from;
		this.subject = subject;
		this.date = date;
		this.text = text;
	}
	
	public Notification(int id, String from, String subject, String text, String date, boolean isRead){
		this(id, from, subject, text, date);
		this.isRead = isRead;
	}
	
	//getters and setters
	public int getId() {
		return id;
	}
	public String getFrom() {
		return from;
	}
	public String getSubject() {
		return subject;
	}
	public String getText() {
		return text;
	}
	public String getDate() {
		return date;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
}
