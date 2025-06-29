package org.example.quizez;
///This class simulates local app data session
public class SessionController {
    public static Integer currentUserId = null;
    public static  String currentUsername = null;
    private static boolean isGuest = false;
    public static void login(int userId, String username){
        currentUserId = userId;
        currentUsername = username;
        isGuest = false;
    }

    public static void loginAsGuest() {
        currentUserId = -1;
        currentUsername = "Gość";
        isGuest = true;
    }
    public static boolean isGuest() {
        return isGuest;
    }

    public static Integer getCurrentUserId(){
        return currentUserId;
    }

    public static String getCurrentUsername(){
        return currentUsername;
    }

    public static void logout(){
        currentUserId = null;
        currentUsername = null;
        isGuest = false;
    }

}
