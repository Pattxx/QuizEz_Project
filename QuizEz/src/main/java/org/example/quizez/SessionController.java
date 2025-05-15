package org.example.quizez;
///This class simulates local app data session
public class SessionController {
    private static Integer currentUserId = null;
    private static  String currentUsername = null;

    public static void login(int userId, String username){
        currentUserId = userId;
        currentUsername = username;
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
    }

}
