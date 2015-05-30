package uet.invincible.models;

public class CheckinModel {
	public String date;
	public boolean status;
	public CheckinModel(String date, boolean status) {
		super();
		this.date = date;
		this.status = status;
	}
	public CheckinModel() {
		super();
	}
}
