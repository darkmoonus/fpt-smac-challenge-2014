package uet.invincible.assistant.configure;

public class ServiceConfigure {
	public static String token = "47a461cb-3519-4e46-82a4-4cb91644b4dd";
//	public static String bot_id = "53bd8a0ee4b06092e2cc916f";
//	public static String bot_id = "53ca42f9e4b04a9d44599725";
	public static String bot_id = "54394ca2e4b0bdbd410d9e7d";
	public static String get_all_bots = "http://tech.fpt.com.vn/AIML/api/bots?token=47a461cb-3519-4e46-82a4-4cb91644b4dd";
	public static String get_bot_details = "http://tech.fpt.com.vn/AIML/api/bots/53bd8a0ee4b06092e2cc916f?token=47a461cb-3519-4e46-82a4-4cb91644b4dd";
	public static String bot_chat_api = "http://tech.fpt.com.vn/AIML/api/bots/53bd8a0ee4b06092e2cc916f/chat?request=Xin chào&token=47a461cb-3519-4e46-82a4-4cb91644b4dd";
	public static String get_bot_chat_api(String message) {
		return "http://tech.fpt.com.vn/AIML/api/bots/53bd8a0ee4b06092e2cc916f/chat?request=" + message + "&token=47a461cb-3519-4e46-82a4-4cb91644b4dd";
	}
}
