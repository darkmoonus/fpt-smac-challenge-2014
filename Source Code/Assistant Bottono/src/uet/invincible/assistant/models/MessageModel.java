package uet.invincible.assistant.models;

public class MessageModel {
	
	public String mMessage;
	public String mTime;
	public int tag;
	
	public MessageModel(String mMessage, String mTime, int tag) {
		this.mMessage = mMessage;
		this.mTime = mTime;
		this.tag = tag;
	}
	public MessageModel(MessageModel model) {
		this.mMessage = model.mMessage;
		this.mTime = model.mTime;
		this.tag = model.tag;
	}
}
