package uet.invincible.models;

public class QuestionModel {
	public String question = "";
	public String answer = "";
	public String difficulty = "1";
	public QuestionModel(String question, String answer, String difficulty) {
		super();
		this.question = question;
		this.answer = answer;
		this.difficulty = difficulty;
	}
	
}
