package uet.invincible.models;

import java.util.ArrayList;

public class StudentModel {
	public String name;
	public String uId;
	public String gender;
	public String studentCode;
	public String cuoiKi;
	public String email;
	public String mobilePhone;
	public String giuaKi;
	public String chuyenCan;
	public String tongKet;
	public String age;
	public String classs;
	public String address;
	public ArrayList<CheckinModel> checkinList = new ArrayList<CheckinModel>();
	public StudentModel() {
		super();
	}
	public StudentModel(String name, String gender, String uId, String studentCode,
			 String email, String mobilePhone, String cuoiKi, String giuaKi,
			String chuyenCan, String tongKet, String age, String classs,
			String address, ArrayList<CheckinModel> checkinList) {
		super();
		this.gender = gender;
		this.name = name;
		this.uId = uId;
		this.studentCode = studentCode;
		this.cuoiKi = cuoiKi;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.giuaKi = giuaKi;
		this.chuyenCan = chuyenCan;
		this.tongKet = tongKet;
		this.age = age;
		this.classs = classs;
		this.address = address;
		this.checkinList = checkinList;
	}
}
