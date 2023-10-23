package Activity;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthReference {
    private static final String AUTH_REFERENCE = "AUTH_REFERENCES";

    public static final String AUTH_REMEMBER = "AUTH_KEY";
    private Context mContext;


    public AuthReference(
            Context context
    ) {

        this.mContext = context;
    }

    public void putStringValue(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(AUTH_REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(AUTH_REFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void putBoolean(boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(AUTH_REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AUTH_REMEMBER, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(AUTH_REFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

}
