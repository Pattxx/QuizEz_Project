package org.example.quizez.managers;


import java.sql.*;

public class Database {

    ///path to database
    private static final String DB_URL = "jdbc:sqlite:data/quizez.db";

    ///DB connection
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }


    ///basic Db connection test with sample query
    public static void testDbConnection(){

        ///Db query to execute
        String query = "SELECT username FROM users LIMIT 1";

        ///try connection with prepared statement query (execute it)
        try (Connection conn = getConnection();
             ///this helps avoid SQL injection
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            //System.out.println("Using DB path: " + new java.io.File("data/quizez.db").getAbsolutePath());
            if (rs.next()) {
                System.out.println("Connected. Sample user: " + rs.getString("username"));
            }
            else {
                System.out.println("Connected. No users found.");
            }

        } catch (SQLException e){
            System.err.println("Connection failed :(" + e.getMessage());
        }
    }

    public static boolean updateQuiz(int quizId, String name, String category, String difficulty, int duration) {
        String query = "UPDATE quizzes SET title = ?, category = ?, difficulty = ?, time_limit = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, difficulty);
            stmt.setInt(4, duration);
            stmt.setInt(5, quizId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Failed to update quiz: " + e.getMessage());
            return false;
        }
    }

}

