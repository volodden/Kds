package com.fairyfalls.kds;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/* ToDo List:
 ** 1. Key = String - дата начала мероприятия, Value = String - вся информация.
 *
 * 2. Распаковка из JSON при чтении.
 * 3. Listener на наличие интернета и загрузка данных за неделю до/две недели после.
 *
 ** 3.1. Нужен ли LruCache, когда его можно заменить просто Map'ом? - Нет.
 *
 * 4. Уведомление о отсутствии интернета.
 * 5. Блокировка любых действий при отсутствии интернета, чтение данных из кэша. (В MainActivity.)
 * 6. ???
 */

public class KDSCache {

    private Map<String, String> cache; //А нужен ли нам LruCache, когда хватит просто Map?

    private final int CACHE_SIZE = 1000;
    private final String FILE_NAME = "kdscache.txt";

    private static KDSCache _instance;

    public static void createInstance() {
        if( null == _instance ) {
            _instance = new KDSCache();
            _instance.init();
        }
    }

    static public KDSCache instance() {
        return _instance;
    }

    KDSCache() {
        init();
    }

    private void init() {
        cache = new HashMap<String, String>(CACHE_SIZE);
    }

    public String getData(String key) {
        return cache.get(key); // null if (key, value) doesn't exist
    }

    //Или же здесь должна быть проверка на наличие интернета, а не в активити?
    public void setData(String key, String value) {
        cache.put(key, value);
    }

    // В onCreate MainActivity:
    // KDSCache.createInstance();
    // KDSCache.instance().loadDataFromStorage(getApplicationContext());
    public boolean loadDataFromStorage(Context context) {

        StringBuilder sb = new StringBuilder();

        File file = new File(context.getFilesDir(), FILE_NAME);
        if( !file.exists() ) {
            return false;
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while( (s = in.readLine()) != null ) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
            return false;
        }

        JSONArray json;
        try {
            json = new JSONArray(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
            return false;
        }

        init();

        for( int i = 0; i < json.length(); ++i ) {
            try {
                JSONObject data = json.getJSONObject(i);
                //"распаковка" JSON
                //cache.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
                return false;
            }
        }
        return true;
    }

    //Вызывать в onDestroy MainActivity
    public void saveDataInStorage(Context context) throws RuntimeException {

        JSONArray json = new JSONArray();
        for( String key : cache.keySet() ) {
            try {
                JSONObject obj = new JSONObject();
                obj.put(key, cache.get(key));
                json.put(obj);
            }
            catch (JSONException e) {
                //Log.i("CACHE_EXCPT", "JSON Exception " + e.toString());
                e.printStackTrace();
                return;
            }
        }

        File file = new File(context.getFilesDir(), FILE_NAME);
        try {
            if( !file.exists() ) {
                if( !file.createNewFile() ) {
                    return;
                    //throw new RuntimeException("File isn't created");
                }
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                out.print(json.toString());
            }
            finally {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
