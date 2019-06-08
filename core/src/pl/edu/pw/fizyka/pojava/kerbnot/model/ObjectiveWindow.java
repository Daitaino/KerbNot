package pl.edu.pw.fizyka.pojava.kerbnot.model;

public class ObjectiveWindow {

	private String text = "";
	private String title = "";
	
	public ObjectiveWindow(String title, String text) {
		this.text = text;
		this.title = title;
	}
	
	public ObjectiveWindow(String text) {
		this.text = text;
	}
	
	public ObjectiveWindow() {
		
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
