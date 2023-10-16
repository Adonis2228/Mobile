package Activity;

import android.content.Context;

import model.User;

public class AuthManager {

    private User authUser;

    public interface AuthMiddleware {
        public void onAuth();
    }

    private static AuthManager I;
    private AuthReference AI;

    public static AuthManager gI() {
        if (I == null) {
            I = new AuthManager();
        }
        return I;
    }

    public static void init(Context context) {
        I = new AuthManager();
        I.AI = new AuthReference(context);
    }

    public void setRemember(boolean remember) {
        AuthManager.gI().AI.putBoolean(remember);
    }

    public boolean getRemember() {
        return AuthManager.gI().AI.getBooleanValue(AuthReference.AUTH_REMEMBER);
    }

    public void saveToken(String token) {
        AuthManager.gI().AI.putStringValue("token", token);
    }

    public String getToken() {
        return AuthManager.gI().AI.getStringValue("token");
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }


}
