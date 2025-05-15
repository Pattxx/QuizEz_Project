package org.example.quizez.net.sqlitetuorial;


import java.sql.*;

public class Database {

    //path to database
    private static final String DB_URL = "jdbc:sqlite:data/quizez.db";

    //DB connection
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }


    //basic Db connection test with sample query
    public static void testDbConnection(){

        //Db query to execute
        String query = "SELECT username FROM users LIMIT 1";

        //try connection with prepared statement query (execute it)
        try (Connection conn = getConnection();

             //this helps avoid SQL injection
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
}

