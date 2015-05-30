package uet.invincible.jsoup;

import android.util.Log;

public class QuestionContent {
    public static String removeQuestionWords(String s) {
        String[] questionWords = {
                "bạn có biết không", "không",
                "tên là gì", "là bao nhiêu", "bao nhiêu", "là mấy", "mấy", "là ai", "là người nào", "là gì",
                "là những tổ chức nào",
                "là thành phố nào", "thành phố nào",
                "diễn ra vào khi nào", "diễn ra khi nào", "xảy ra vào khi nào", "xảy ra khi nào", "là khi nào", "vào khi nào", "và khi nào", "khi nào",
                "diễn ra vào năm nào", "diễn ra năm nào", "xảy ra vào năm nào", "xảy ra năm nào", "là năm nào", "vào năm nào", "năm nào",
                "diễn ra vào ngày tháng năm nào", "diễn ra vào tháng năm nào", "xảy ra vào ngày tháng năm nào", "xảy ra vào tháng năm nào", "là ngày tháng năm nào", " vào ngày tháng năm nào",
                "diễn ra vào ngày nào", "diễn ra ngày nào", "xảy ra vào ngày nào", "xảy ra ngày nào", "là ngày nào", "vào ngày nào", "ngày nào",
                "diễn ra vào năm bao nhiêu", "diễn ra năm bao nhiêu", "xảy ra vào năm bao nhiêu", "xảy ra năm bao nhiêu", "là năm bao nhiêu", "vào năm bao nhiêu", "năm bao nhiêu",
                "diễn ra vào ngày bao nhiêu", "diễn ra ngày bao nhiêu", "xảy ra vào ngày bao nhiêu", "xảy ra ngày bao nhiêu", "là ngày bao nhiêu", "vào ngày bao nhiêu", "ngày bao nhiêu",
                "diễn ra vào thời điểm nào", "diễn ra thời điểm nào", "là thời điểm nào", "vào thời điểm nào", "thời điểm nào",
                "diễn ra vào thời gian nào", "diễn ra thời gian nào", "xảy ra vào thời gian nào", "xảy ra thời gian nào", "là thời gian nào", "vào thời gian nào", "thời gian nào",
                "diễn ra ở đâu", "diễn ra tại đâu", "xảy ra ở đâu", "xảy ra tại đâu", "và ở đâu", "ở đâu",
                "là thủ đô nước nào", "là thủ đô của nước nào",
                };
        String[] leadingQuestionWords = {
        		"bạn hãy cho mình biết", "bạn hãy cho tôi biết", "bạn hãy cho biết", "hãy cho mình biết", "hãy cho tôi biết", 
        		"hãy cho biết", "bạn có biết về", "bạn có biết", "bạn cho biết", "đố bạn biết",
                "cho biết", "hãy cho mình biết ai là", "hãy cho tôi biết ai là", "hãy cho biết ai là", "ai là", "bạn biết gì về", "bạn có biết", "sự kiện"};

        Log.d("remove", s);

        s = s.toLowerCase();

        for (int i = 0; i < questionWords.length; ++i) {
            if (s.contains(questionWords[i])) {
//                s = s.substring(0, s.length() - questionWords[i].length() - 1);
            	s = s.replace(questionWords[i], "");            	
                Log.d("QuestionWords", questionWords[i]);
            }
        }

        for (int i = 0; i < leadingQuestionWords.length; ++i) {
            if (s.contains(leadingQuestionWords[i])) {
//                s = s.substring(leadingQuestionWords[i].length() + 1, s.length());
            	s = s.replace(leadingQuestionWords[i], "");
                Log.d("leadingQuestionWords", leadingQuestionWords[i]);
            }
        }

        s = s.replace("  ", " ").trim();        
        Log.d("remove", s);

        return s;
    }
}
