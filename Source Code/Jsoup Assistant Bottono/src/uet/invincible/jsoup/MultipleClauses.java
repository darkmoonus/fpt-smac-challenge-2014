package uet.invincible.jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import uet.invincible.assistant.R;

public class MultipleClauses {
	public static String getMultipleArea(String query, Context context) {
		String answer = "";

        HashMap<String, String> areaMap = File.readDBFileToStringHashMap(context, R.drawable.dientich);       

        query = QuestionContent.removeQuestionWords(query);        
        query = query.replace("diện tích của", "").replace("diện tích", "").trim();
        
        String[] places = query.split(" và ");
        for(int i = 0; i < places.length; ++i) 
        	places[i] = places[i].trim();		
                   
        for (int i = 0; i < places.length; ++i) {
        	Iterator<String> keySetIterator = areaMap.keySet().iterator();
        	while(keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                Log.d("key", key + " " + areaMap.get(key)); 
	        	if (places[i].toLowerCase().equals(key.toLowerCase())) {
	        		answer += "Diện tích " + key + " là " + areaMap.get(key) + ". ";
	        	}
        	}       
        }

        if (answer.equals("")) {
        	answer = "NO DATA FROM JSOUP";
        }
		return answer;		
	}
	
	public static String getMultiplePopularity(String query, Context context) {
		String answer = "";

        HashMap<String, String> popularityMap = File.readDBFileToStringHashMap(context, R.drawable.danso);
        
        query = QuestionContent.removeQuestionWords(query);        
        query = query.replace("dân số của", "").replace("dân số", "").trim();
        
        String[] places = query.split(" và ");
        for(int i = 0; i < places.length; ++i) {
        	places[i] = places[i].trim();
        	Log.e("places", "'" + places[i] + "'");
        }
        
        for (int i = 0; i < places.length; ++i) {
        	Iterator<String> keySetIterator = popularityMap.keySet().iterator();
        	while(keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                Log.d("key", key + " " + popularityMap.get(key));
                if (places[i].toLowerCase().equals(key.toLowerCase())) {
            		answer += "Dân số " + key + " là " + popularityMap.get(key) + ". ";
            	}                   
            }        	
        }

        if (answer.equals("")) {
        	answer = "NO DATA FROM JSOUP";
        }
		return answer;		
	}
}
