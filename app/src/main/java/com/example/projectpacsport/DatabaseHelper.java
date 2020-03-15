package com.example.projectpacsport;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHelper {

    DatabaseConnection myDatabaseConn;
    Connection conn;
    Context context;

    public DatabaseHelper(Context context) {
        Log.d("Context: ", context == null ? "is null" : "is not null");
        myDatabaseConn = new DatabaseConnection(context);
        this.context = context;
    }

    public Boolean validation(String uEmail, String password, User user) {
        Boolean validated = false;

        conn = DatabaseConnection.connectionclass();

        String query = "SELECT * from dbo.[User] WHERE User_email like '" + uEmail + "'";
        Log.e("query", query);
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            String DBUserEmail;
            String DBPassword;
            if (result.next()) {
                user.setId(result.getInt("User_id"));
                DBUserEmail = result.getString("User_email");
                DBPassword = result.getString("User_password");
                if (DBPassword.equals(password) && DBUserEmail.equals(uEmail))
                    validated = true;
                conn.close();
            } else {
                validated = false;
                //user.setId(5);
            }
        } catch (Exception ex) {
            validated = false;
            Log.e("DB", ex.getMessage());
        }
        return validated;
    }

    public String validateRegisterData(String firstName, String lastName, String password, String passwordmatch, String email) {
        String result = "";
        boolean exists = checkIfRegistered("User_email", email);
        if (!email.isEmpty()) {
            if (exists == false) {
                if (!firstName.isEmpty() || !lastName.isEmpty()) {
                    int length = password.length();
                    if (length >= 1) { //for testing but should be longer
                        if (isValidPassword(password) == true) {
                            if (password.equals(passwordmatch)) {
                                if (!isValidEmail(email)) {
                                    result = "Invalid email";
                                } else {
                                    result = "Success";
                                }
                            } else {
                                result = "Passwords don't match";
                            }
                        } else {
                            result = "Password should contain one uppercase, one number and one special character";
                        }
                    } else {
                        result = "Password is too short";
                    }
                } else {
                    result = "Names can't be empty";
                }
            } else {
                result = "Email already exists";
            }
        } else {
            result = "Email can't be empty";
        }
        return result;
    }

    public boolean checkIfRegistered(String field, String search) {
        if (conn == null) {
            conn = DatabaseConnection.connectionclass();
        }
        boolean alreadyExists;
        String query = "SELECT * from dbo.User WHERE " + field + " like '" + search + "';";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                alreadyExists = true;
                conn.close();
            } else {
                alreadyExists = false;
            }
        } catch (Exception ex) {
            alreadyExists = false;
        }
        return alreadyExists;
    }

    public long addUser(String name, String lastName, String password, String email) {
        long result = -1;
        conn = DatabaseConnection.connectionclass();

        String query = "INSERT INTO dbo.[User] (User_name, User_lastname, User_password, User_email, User_level) VALUES ('" + name + "','" + lastName + "','" + password + "','" + email + "'," + 0 + ")";
        try {
            Statement statement = conn.createStatement();
            result = statement.executeUpdate(query);

            if (result != -1) {
                Log.e("DB: ", "Added user " + name + " " + lastName);
            } else {
                Log.e("DB : ", "Error adding user " + name + " " + lastName);
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB INSERT", ex.getMessage() + query);
        }
        return result;
    }

    public boolean isValidPassword(String password) {
        //needs to implement code to validate password
        return true;
    }

    private boolean isValidEmail(String email) {
        //needs to implement code to validate email
        return true;
    }

    public ArrayList<String> getDataForSpinner(String teamLeague) {
        ArrayList<String> allTeams = new ArrayList<>();
            conn = DatabaseConnection.connectionclass();

        String query = "SELECT * from [dbo].[Team] WHERE Team_league  = '" + teamLeague + "';";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                allTeams.add(result.getString("Team_name"));
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB", ex.getMessage());
        }
        return allTeams;
    }

}

