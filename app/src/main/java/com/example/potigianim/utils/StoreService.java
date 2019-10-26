package com.example.potigianim.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StoreService
{
    private static StoreService instance;
    private Gson gson;
    private Map<String, Object> cache;

    private StoreService() {
        this.cache = new HashMap<>();
        this.gson = new Gson();
    }

    public static StoreService getInstance() {
        if (instance == null) {
            instance = new StoreService();
        }

        return instance;
    }

    public <T> void set(String key, T object, Context context) {
        cache.put(key, object);
        persist(key, object, context);
    }

    public <T> T get(String key, Class<T> objectClass, Type objectType, Context context) {
        if (cache.containsKey(key)) {
            return objectClass.cast(cache.get(key));
        }

        return request(key, objectClass, objectType, context);
    }

    public void remove(String key, Context context) {
        if (cache.containsKey(key)) {
            cache.remove(key);
        }

        delete(key, context);
    }

    public void cleanOldContent(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date dateOneMonthAgo = cal.getTime();

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefs.getAll()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().endsWith(Constants.DATE_SUFFIX)
                    && gson.fromJson(entry.getValue().toString(), Date.class).before(dateOneMonthAgo))
                .map(Map.Entry::getKey)
                .forEach(key -> {
                    if (!Constants.API_KEY.equals(key)) {
                        prefsEditor.remove(key);
                        prefsEditor.remove(key.replace(Constants.DATE_SUFFIX, ""));
                    }
                });
        prefsEditor.apply();
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    private <T> void persist(String key, T object, Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(key, gson.toJson(object));
        prefsEditor.putString(key + Constants.DATE_SUFFIX, gson.toJson(new Date()));
        prefsEditor.apply();
    }

    private <T> T request(String key, Class<T> unused, Type objectType, Context context) {
        String defaultValue = "#!NULL";
        SharedPreferences prefs = getPreferences(context);

        String json = prefs.getString(key, defaultValue);

        if (defaultValue.equals(json)) {
            return null;
        }

        return gson.fromJson(json, objectType);
    }

    private void delete(String key, Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.remove(key);
        prefsEditor.remove(key + Constants.DATE_SUFFIX);
        prefsEditor.apply();
    }
}
