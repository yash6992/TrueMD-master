package info.truemd.materialdesign.helper;

/**
 * Created by yashvardhansrivastava on 04/04/16.
 */

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TrueMDJSONUtils {

    public static final String TrueValue = "true";
    public static final String FalseValue = "false";

    public static boolean isEmpty(JSONObject jsonObject) {
        return jsonObject == null || jsonObject.length() == 0;
    }

    public static boolean isNotEmpty(JSONObject jsonObject) {
        return !isEmpty(jsonObject);
    }

    public static boolean isEmpty(JSONArray jsonArray) {
        return jsonArray == null || jsonArray.length() == 0;
    }

    public static boolean isNotEmpty(JSONArray jsonArray) {
        return !isEmpty(jsonArray);
    }

    public static JSONObject newJSONObject(String jsonString) throws JSONException {
        if (StringUtils.isEmpty(jsonString)) {
            return new JSONObject();
        }
        return new JSONObject(jsonString);
    }

    public static String goThroughNullCheck(String jsonString) {

        try {
            if (jsonString.trim().equalsIgnoreCase("null")) {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return jsonString == null ? "" : jsonString;

        }
        return jsonString;
    }



    /**
     * there are brilliant cases in our app where we dont know what we stored in properties.
     * The method is supposed to return a new jsonObject from the string passed in parameter.
     * If it happend to be a json object then the same object is returned.
     *
     * @param o
     * @return
     * @throws JSONException
     */
    public static JSONObject newJSONObjectFromStringOrJsonObject(Object o) throws JSONException {
        if (o instanceof String && StringUtils.isNotBlank((String) o)) {
            return new JSONObject((String) o);
        }
        if (o instanceof JSONObject) {
            return (JSONObject) o;
        }
        return new JSONObject();
    }

    /**
     * @param object1
     * @param object2 merges object 2 in object 1. Ignore the key if value is null
     */
    public static void merge(JSONObject object1, JSONObject object2) throws JSONException {
        if (object1 == null || isEmpty(object2)) {
            return;
        }
        Iterator<String> keyIterator = object2.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = object2.get(key);
            if (value != null) {
                object1.put(key, value);
            }
        }
    }

    public static JSONObject nullSafeJSONObject(JSONObject object) {
        if (object == null) {
            return new JSONObject();
        }
        return object;

    }

    public static JSONArray nullSafeJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new JSONArray();
        }
        return jsonArray;
    }

    public static boolean addNullSafe(JSONObject outputJsonObject, String key, Object value) {
        if (value != null && StringUtils.isNotEmpty(key)) {
            try {
                outputJsonObject.put(key, value);
                return true;
            } catch (JSONException e) {
            }
        }
        return false;
    }

    public static boolean addNullSafe(JSONObject outputJsonObject, String key, List<?> value) {
        if (CollectionUtils.isNotEmpty(value) && StringUtils.isNotEmpty(key)) {
            try {
                outputJsonObject.put(key, value);
                return true;
            } catch (JSONException e) {
            }
        }
        return false;
    }

    public static List<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        if (isEmpty(jsonArray)) {
            return Collections.EMPTY_LIST;
        }
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.optString(i));
        }
        return list;
    }


    /**
     * @param jsonObject
     * @param key
     * @return this is an alternate to optString(). optString returns "null" string in certain cases, this method will return
     *         an empty string instead
     */
    public static String nullSafeGetString(JSONObject jsonObject, String key) {
        try {
            String value = jsonObject.optString(key);
            return nullIfValueIsNull(value);
        } catch (Exception e) {
            ;
        }
        return "";
    }

    public static String nullIfValueIsNull(String s) {
        if (StringUtils.isBlank(s) || "null".equalsIgnoreCase(s)) {
            return null;
        }
        return s;
    }

    public static JSONObject emptyJSONObject() {
        return new JSONObject();
    }
}
