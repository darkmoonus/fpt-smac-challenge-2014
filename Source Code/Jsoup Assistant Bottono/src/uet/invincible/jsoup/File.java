package uet.invincible.jsoup;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;

public class File {
    public static HashMap<String, String> readDBFileToStringHashMap(Context context, int resources) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            InputStream in= context.getResources().openRawResource(resources);
            InputStreamReader inreader=new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inreader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splits = line.split(" && ");
//                Log.d("split[0]", splits[0]);
                hashMap.put(splits[0], splits[1]);
            }
            reader.close();

            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
