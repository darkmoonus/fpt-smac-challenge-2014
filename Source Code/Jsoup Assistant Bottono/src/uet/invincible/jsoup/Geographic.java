package uet.invincible.jsoup;


import java.util.HashMap;
import java.util.Iterator;

import uet.invincible.assistant.R;
import android.content.Context;
import android.util.Log;


public class Geographic {
    public static String getAnswerGeographicCountry(String capital, Context context) {
        String answer = "NO DATA FROM JSOUP";

        HashMap<String, String> geoCountry = File.readDBFileToStringHashMap(context, R.drawable.capital);

        String capitalQuery = QuestionContent.removeQuestionWords(capital);
        Log.d("capitalQuery", capitalQuery);

        Iterator<String> keySetIterator = geoCountry.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String key = keySetIterator.next();

            if (capitalQuery.toLowerCase().equals(key.toLowerCase())) {
                answer = geoCountry.get(key);

            }
        }

        return answer;
    }

    public static String getAnswerGeographicCapital(String country, Context context) {
        String answer = "NO DATA FROM JSOUP";

        HashMap<String, String> geoCountry = File.readDBFileToStringHashMap(context, R.drawable.capital);

        String countryQuery = QuestionContent.removeQuestionWords(country);

        String[] leadings = {"thủ đô của nước", "thủ đô của", "thủ đô"};
        for (String leading : leadings) {
            if (countryQuery.contains(leading)) {
//                countryQuery = countryQuery.substring(leading.length() + 1, countryQuery.length());
            	countryQuery = countryQuery.replace(leading, "").replace("  ", " ").trim();
            }
            
        }
        Log.d("countryQuery", countryQuery);

        Iterator<String> keySetIterator = geoCountry.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            if (countryQuery.toLowerCase().equals(geoCountry.get(key).toLowerCase())) {
                answer = key;
            }
        }

        return answer;
    }

    public static String getAnswerGeographicPopularity(String country, Context context) {
        String answer = "NO DATA FROM JSOUP";

        HashMap<String, String> geoPopularity = File.readDBFileToStringHashMap(context, R.drawable.popularity);

        String countryQuery = QuestionContent.removeQuestionWords(country);

        String[] leadingPops = {"dân số của nước", "dân số của", "dân số nước"};
        for (String leading : leadingPops) {
            if (countryQuery.contains(leading))
                countryQuery = countryQuery.substring(leading.length() + 1, countryQuery.length());
        }
        Log.d("countryQuery", countryQuery);

        Iterator<String> keySetIterator = geoPopularity.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            if (countryQuery.toLowerCase().equals(key.toLowerCase())) {
                answer = geoPopularity.get(key);
            }
        }

        return answer;
    }

    public static String getAnswerGeographicGeo(String country, Context context) {
        String answer = "NO DATA FROM JSOUP";

        HashMap<String, String> geoPopularity = File.readDBFileToStringHashMap(context, R.drawable.geo);

        String countryQuery = QuestionContent.removeQuestionWords(country);

        String[] leadingPops = {"nằm ở đâu"};
        for (String leading : leadingPops) {
            if (countryQuery.contains(leading))
                countryQuery = countryQuery.substring(0, countryQuery.length() - leading.length() - 1);
        }
        Log.d("countryQuery", countryQuery);

        Iterator<String> keySetIterator = geoPopularity.keySet().iterator();

        while(keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            if (countryQuery.toLowerCase().contains(key.toLowerCase())) {
                answer = geoPopularity.get(key);
            }
        }

        return answer;
    }

//    class GetNameEntityLocationRecognition extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            StringBuilder builder = new StringBuilder();
//            HttpClient client = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet("http://54.255.200.131/fti-qa/nlp/vi/ner?text=" + strings[0].replace(" ", "%20"));
//
//            try {
//                HttpResponse response = client.execute(httpGet);
//                StatusLine statusLine = response.getStatusLine();
//                int statusCode = statusLine.getStatusCode();
//                if (statusCode == 200) {
//                    HttpEntity entity = response.getEntity();
//                    InputStream content = entity.getContent();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        builder.append(line);
//                    }
//                } else {
//                    Log.e("getNameEntityLocationRecognition", "Failed to download file");
//                }
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return builder.toString();
//        }
//    }
//
//    private static String getLocation(String nameEntityRegconitionLocation) {
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = null;
//        try {
//            db = dbf.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//
//        InputSource is = new InputSource();
//        is.setCharacterStream(new StringReader(nameEntityRegconitionLocation));
//
//        Document doc = null;
//        try {
//            doc = db != null ? db.parse(is) : null;
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        NodeList nodes = doc != null ? doc.getElementsByTagName("LOC") : null;
//        Element line = (Element) (nodes != null ? nodes.item(0) : null);
//
//        return getCharacterDataFromElement(line);
//    }
//
//    private static String getCharacterDataFromElement(Element e) {
//        Node child = e.getFirstChild();
//        if (child instanceof CharacterData) {
//            CharacterData cd = (CharacterData) child;
//            return cd.getData();
//        }
//        return "?";
//    }
}
