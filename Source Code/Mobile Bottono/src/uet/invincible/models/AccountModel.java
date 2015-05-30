package uet.invincible.models;

public class AccountModel {
	public String name;
	public String level;
	public String uId;
	public String gender;
	public String email;
	public String mobilePhone;
	public String age;
	public String address;
	public AccountModel() {
		super();
	}

	public AccountModel(String name, String level, String uId, String gender,
			String email, String mobilePhone, String age, String address) {
		super();
		this.name = name;
		this.level = level;
		this.uId = uId;
		this.gender = gender;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.age = age;
		this.address = address;
	}
}
