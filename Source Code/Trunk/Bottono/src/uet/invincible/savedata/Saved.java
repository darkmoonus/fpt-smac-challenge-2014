package uet.invincible.savedata;

import java.util.ArrayList;

import uet.invincible.models.AccountModel;
import uet.invincible.models.QuestionModel;
import uet.invincible.models.StudentModel;

public class Saved {
	public static ArrayList<StudentModel> studentList;
	public static ArrayList<AccountModel> accountList;
	public static ArrayList<QuestionModel> questionList;
	public static AccountModel account;

	public Saved() {
		studentList = new ArrayList<StudentModel>();
		accountList = new ArrayList<AccountModel>();
		questionList = new ArrayList<QuestionModel>();
		account = new AccountModel();
	}

	public static boolean duplicateStudentId(String id) {
		for (int i = 0; i < studentList.size(); i++) {
			if (id.equals(studentList.get(i).uId)) {
				return true;
			}
		}
		return false;
	}
}
