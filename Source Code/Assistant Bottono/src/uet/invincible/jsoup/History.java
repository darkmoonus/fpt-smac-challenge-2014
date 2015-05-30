package uet.invincible.jsoup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import uet.invincible.assistant.R;
import android.content.Context;
import android.util.Log;

public class History {
    public static String getAnswerHistoryEvents(int year, Context context) {
        HashMap<String, String> historyEvents = File.readDBFileToStringHashMap(context, R.drawable.events);

        Iterator<String> keySetIterator = historyEvents.keySet().iterator();

        LinkedList<String> ansList = new LinkedList<String>();

        while(keySetIterator.hasNext()) {
            String time = keySetIterator.next();

            if (time.contains("-")) {
                String[] timeSplit = time.split("-");
                for (int i = 0; i < timeSplit.length; i++) {
                    if (String.valueOf(year).equals(timeSplit[i])) {
                        ansList.add(historyEvents.get(time));
                    }
                }
            } else {
                if (time.equals(String.valueOf(year))) {
                    ansList.add(historyEvents.get(time));
                }
            }
        }

        String answer = "";
//        if (ansList.size() > 3) {
//            for (String ans : ansList) {
//                if (StringUtils.isAllUpperCase("NAM")) {
//                    answer = answer + ans + "\n";
//                }
//            }
//        } else {
            for (String ans : ansList) {
                answer = answer + ans + "\n";
            }
//        }

        return answer;
    }

    public static String getAnswerHistoryTime(String event, Context context) {
        String answer = "NO DATA FROM JSOUP";

        String content = QuestionContent.removeQuestionWords(event);
        Log.d("content", content);

        String[] splits = content.
                replace("nước ta", "Việt Nam").
                replace("bác hồ", "Hồ Chí Minh").
                replace("hồ chủ tịch", "hồ chí minh").
                split(" ");
        for (String split : splits) {
            Log.d("split", split);
        }

        HashMap<String, String> historyEvents = File.readDBFileToStringHashMap(context, R.drawable.events);

        Iterator<String> keySetIterator = historyEvents.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String time = keySetIterator.next();

            boolean contained = true;
            for (String split : splits) {
//                Log.d("w2", split);
                if (!historyEvents.get(time).toLowerCase().contains(split.toLowerCase())) {
                    contained = false;
                    break;
                }
            }

            if (contained) {
                answer = historyEvents.get(time);
            }
        }


        return answer;
    }

    public static String getAnswerHistoryDynasty(String dyn, Context context) {
        String answer = "NO DATA FROM JSOUP";

        String content = QuestionContent.removeQuestionWords(dyn);
        Log.d("content - dysnatry", content);

        HashMap<String, String> dynasties = File.readDBFileToStringHashMap(context, R.drawable.dynasties);

        Iterator<String> keySetIterator = dynasties.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String dynasty = keySetIterator.next();

            if (dynasty.toLowerCase().equals(dyn)) {
                answer = dynasties.get(dynasty);
            }
        }

        return answer;
    }

    public static String getAnswerHistory(String s, Context context) {
        String answer = "NO DATA FROM JSOUP";

        String content = QuestionContent.removeQuestionWords(s);
        Log.d("content", content);

        String[] splits = content.split(" ");
        for (String split : splits) {
            Log.d("split", split);
        }

        HashMap<String, String> history = File.readDBFileToStringHashMap(context, R.drawable.events);

        Iterator<String> keySetIterator = history.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String next = keySetIterator.next();

            boolean contained = true;
            for (String split : splits) {
                Log.d("w2", split);
                if (!history.get(next).toLowerCase().contains(split.toLowerCase())) {
                    contained = false;
                    break;
                }
            }

            if (contained) {
                answer = history.get(next);
            }
        }

        return answer;
    }

//    protected static String replaceStrings(String originString) {
//        String[] origins = {"nước ta", "bác hồ", "Hồ Chủ Tịch"};
//        String[] replacements = {"Việt Nam", "Hồ Chí Minh", "Hồ Chí Minh"};
//
//        StringBuffer originStringBuffer = new StringBuffer(originString);
//
//        Log.d("originStringBuffer", originStringBuffer.toString());
//
//        for (int i = 0 ; i < origins.length; ++i) {
//            Log.d("replpaceStings", String.valueOf(originStringBuffer.lastIndexOf(origins[i])));
//            originStringBuffer.replace(
//                    originStringBuffer.lastIndexOf(origins[i].toLowerCase()),
//                    originStringBuffer.lastIndexOf(origins[i].toLowerCase()) + origins[i].length(),
//                    replacements[i]);
//            Log.d("replpaceStings", originStringBuffer.toString());
//        }
//
//        return originStringBuffer.toString();
//    }

}
