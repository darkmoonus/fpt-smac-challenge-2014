package uet.invincible.jsoup;

import android.util.Log;

public class QuestionContent {
    public static String removeQuestionWords(String s) {
        String[] questionWords = {
                "bạn có biết không", "không",
                "tên là gì", "là bao nhiêu", "là mấy", "là ai", "là người nào", "là gì",
                "là những tổ chức nào",
                "là thành phố nào",
                "diễn ra vào khi nào", "diễn ra khi nào", "là khi nào", "vào khi nào", "khi nào",
                "diễn ra vào năm nào", "diễn ra năm nào", "là năm nào", "vào năm nào", "năm nào",
                "diễn ra vào ngày tháng năm nào", "diễn ra vào tháng năm nào",
                "diễn ra vào ngày nào", "diễn ra ngày nào", "là ngày nào", "vào ngày nào", "ngày nào",
                "diễn ra vào năm bao nhiêu", "diễn ra năm bao nhiêu", "là năm bao nhiêu", "vào năm bao nhiêu", "năm bao nhiêu",
                "diễn ra vào ngày bao nhiêu", "diễn ra ngày bao nhiêu", "là ngày bao nhiêu", "vào ngày bao nhiêu", "ngày bao nhiêu",
                "diễn ra vào thời điểm nào", "diễn ra thời điểm nào", "là thời điểm nào", "vào thời điểm nào", "thời điểm nào",
                "diễn ra vào thời gian nào", "diễn ra thời gian nào", "là thời gian nào", "vào thời gian nào", "thời gian nào",
                "diễn ra ở đâu", "diễn ra tại đâu",
                "là thủ đô nước nào", "là thủ đô của nước nào",
                };
        String[] leadingQuestionWords = {
                "bạn hãy cho biết", "hãy cho biết", "bạn có biết về", "bạn cho biết",
                "cho biết", "hãy cho biết ai là", "ai là", "bạn biết gì về", "bạn có biết", "sự kiện"};

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
