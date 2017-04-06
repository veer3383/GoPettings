package gopetting.assignment.com.cartapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by viraj on 4/6/16.
 */
public class UserSession {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREFER_NAME = "appuser";
    public static final String CART = "cart";
    public static final String CARTCOUNT = "0";
    public static final String MODE = "mode";
    public static final String RESPONSE = "response";

    // Constructor
    public UserSession(Context context){
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void StoreJSON(String token) {
        editor.putString(RESPONSE, token);
        editor.commit();
    }
    public void Storecart(String token) {
        editor.putString(CART, token);
        editor.commit();
    }
    public void Storecartcount(String token) {
        editor.putString(CARTCOUNT, token);
        editor.commit();
    }
    public void StoreviewMode(String token) {
        editor.putString(MODE, token);
        editor.commit();
    }

    public HashMap<String,String> getCartCount() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(CARTCOUNT, pref.getString(CARTCOUNT, null));
        return user;
    }

    public HashMap<String,String> getCartItems() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(CART, pref.getString(CART, null));

        return user;
    }
    public HashMap<String,String> getViewMode() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(MODE, pref.getString(MODE, null));

        return user;
    }
    public HashMap<String,String> getJSON() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(RESPONSE, pref.getString(RESPONSE, null));

        return user;
    }

}
