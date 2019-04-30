package pl.pojava.kerbnot.model;

public class Popup {
	
	private String text;
	private String lastText;
	
	private String title;
	private boolean propertyChanged;
	
	public Popup(String title, String text) {
		this.text = text;
		this.title = title;
		this.lastText = text;
		propertyChanged = false;
	}
	
	public Popup(String text) {
		this("", text);
	}
	
	public Popup() {
		this("", "");
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text + "\n\n" + this.text;
		this.lastText = text;
		
		propertyChanged = true;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isPropertyChanged() {
		return propertyChanged;
	}
	
	public void setPropertyChanged(boolean propertyChanged) {
		this.propertyChanged = propertyChanged;
	}
	
	public String getLastText() {
		return lastText;
	}
}
