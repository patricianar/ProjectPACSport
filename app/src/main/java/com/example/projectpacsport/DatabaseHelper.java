package com.example.projectpacsport;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            String DBUserEmail;
            String DBPassword;
            if (result.next()) {
                DBUserEmail = result.getString("User_email");
                DBPassword = result.getString("User_password");
                if (DBPassword.equals(password) && DBUserEmail.equals(uEmail)) {
                    validated = true;
                    user.setId(result.getInt("User_id"));
                    user.setName(result.getString("User_name"));
                    user.setLastname(result.getString("User_lastname"));
                    user.setEmail(result.getString("User_email"));
                }
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

    public Team getTeam(String teamName) {
        Team team = new Team();
        conn = DatabaseConnection.connectionclass();

        String query = "SELECT Team_id from [dbo].[Team] WHERE Team_name  LIKE '%" + teamName + "%';";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                team.setId(result.getInt(1));
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB", ex.getMessage());
        }
        return team;
    }

    public int addEvent(Event event) {
        int eventId = 0;
        conn = DatabaseConnection.connectionclass();

        String query = "INSERT INTO dbo.[Event](Event_name, Event_planner_id, Event_location, Event_address, Event_postal_code, Event_city, " +
                "Event_province, Event_country, Event_date, Event_time, Event_capacity, Event_team1_id, Event_team2_id, Event_image) " + "OUTPUT Inserted.Event_id " +
                "VALUES ('" + event.getName() + "'," + event.getPlannerId() + ",GEOGRAPHY::Point(" + event.getLatitude() + "," + event.getLongitude() + ",4326),'" +
                event.getAddress() + "','" + event.getPostalCode() + "','" + event.getCity() + "','" + event.getProvince() + "','" + event.getCountry() + "','" +
                event.getDate() + "','" + event.getTime() + "'," + event.getCapacity() + "," + event.getTeam1().getId() + "," + event.getTeam2().getId() + ",'" + event.getImage() + "');";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                eventId = result.getInt("Event_id");
                Log.d("DB: ", "Added event " + result.getInt("Event_id") + " : " + event.getName());
            } else {
                Log.d("DB: ", "Error adding user " + result.getInt("Event_id") + " : " +event.getName());
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB INSERT:", ex.getMessage() + query);
        }
        return eventId;
    }

    public ArrayList<Event> getEventRecs(ArrayList<Integer> myEvents) {
        ArrayList<Event> allEvents = new ArrayList<>();
        conn = DatabaseConnection.connectionclass();
        String query = "SELECT dbo.[Event].Event_id, dbo.[Event].Event_name, dbo.[Event].Event_address, dbo.[Event].Event_date, dbo.[Event].Event_city, dbo.[Team].Team_league, dbo.[Event].Event_image\n" +
                " FROM dbo.[Event] LEFT JOIN dbo.[UserEvent] ON dbo.[Event].Event_id = dbo.[UserEvent].Event_id \n" +
                "LEFT JOIN dbo.[Team] ON dbo.[Event].Event_team1_id = dbo.[Team].Team_id GROUP BY dbo.[Event].Event_id, dbo.[Event].Event_name, dbo.[Event].Event_address, dbo.[Event].Event_date, dbo.[Event].Event_city, dbo.[Team].Team_league, dbo.[Event].Event_image;";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                Event event = new Event();
                event.setId(result.getInt("Event_id"));
                event.setName(result.getString("Event_name"));
                event.setAddress(result.getString("Event_address"));
                event.setDate(result.getString("Event_date"));
                event.setCity(result.getString("Event_city"));
                event.setImage(result.getString("Event_image"));
                event.setLeague(result.getString("Team_league"));
                //team = getTeamsInfo(result.getInt("Event_team1_id"));
                //event.setTeam1(team);
                //team = getTeamsInfo(result.getInt("Event_team2_id"));
                //event.setTeam2(team);
                //event.setAttendants(getNumOfAttendants(event.getId()));
                //Log.e("atte", event.getAttendants() + "");

                for (int eventId : myEvents) {
                    if (event.getId() == eventId)
                        event.setSelected(true);
                }
                allEvents.add(event);
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB Events", ex.getMessage());
        }
        return allEvents;
    }

    public ArrayList<Event> getMyMeetups(int userId, int going){
        ArrayList<Event> allEvents = new ArrayList<>();
        conn = DatabaseConnection.connectionclass();
        String query = "";
        if(going == 0){
            query = "SELECT dbo.[Event].Event_id, dbo.[Event].Event_name, dbo.[Event].Event_address, dbo.[Event].Event_date, dbo.[Event].Event_city, dbo.[Team].Team_league, dbo.[Event].Event_image, dbo.[Event].Event_capacity \n" +
                    " FROM dbo.[Event] LEFT JOIN dbo.[UserEvent] ON dbo.[Event].Event_id = dbo.[UserEvent].Event_id \n" +
                    "LEFT JOIN dbo.[Team] ON dbo.[Event].Event_team1_id = dbo.[Team].Team_id WHERE dbo.[Event].Event_planner_id = " + userId +
                    " GROUP BY dbo.[Event].Event_id, dbo.[Event].Event_name, dbo.[Event].Event_address, dbo.[Event].Event_date, dbo.[Event].Event_city, dbo.[Team].Team_league, dbo.[Event].Event_image, dbo.[Event].Event_capacity;";
        }
        else{
            query = "SELECT dbo.[Event].Event_id, dbo.[Event].Event_name, dbo.[Event].Event_address, dbo.[Event].Event_date, dbo.[Event].Event_city, dbo.[Team].Team_league, dbo.[Event].Event_image, dbo.[Event].Event_capacity\n" +
                    " FROM dbo.[Event] LEFT JOIN dbo.[UserEvent] ON dbo.[Event].Event_id = dbo.[UserEvent].Event_id \n" +
                    "LEFT JOIN dbo.[Team] ON dbo.[Event].Event_team1_id = dbo.[Team].Team_id WHERE dbo.[UserEvent].User_id = " + userId +
                    "GROUP BY dbo.[Event].Event_id, dbo.[Event].Event_name, dbo.[Event].Event_address, dbo.[Event].Event_date, dbo.[Event].Event_city, dbo.[Team].Team_league, dbo.[Event].Event_image, dbo.[Event].Event_capacity;";
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                Event event = new Event();
                event.setId(result.getInt("Event_id"));
                event.setName(result.getString("Event_name"));
                event.setAddress(result.getString("Event_address"));
                event.setDate(result.getString("Event_date"));
                event.setCity(result.getString("Event_city"));
                event.setImage(result.getString("Event_image"));
                event.setLeague(result.getString("Team_league"));
                event.setCapacity(result.getInt("Event_capacity"));
                //team = getTeamsInfo(result.getInt("Event_team1_id"));
                //event.setTeam1(team);
                //team = getTeamsInfo(result.getInt("Event_team2_id"));
                //event.setTeam2(team);
                event.setAttendants(getNumOfAttendants(event.getId()));
                Log.e("atte", event.getAttendants() + "");

                allEvents.add(event);
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB MyMeetups", ex.getMessage());
        }
        return allEvents;
    }

    public ArrayList<Integer> getMyEventsIds(int userId) {
        ArrayList<Integer> allMyEventsIds = new ArrayList<>();
        conn = DatabaseConnection.connectionclass();
        String query = "SELECT * FROM dbo.[UserEvent] WHERE User_id = " + userId + ";";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                allMyEventsIds.add(result.getInt("Event_id"));
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB Events Ids ", ex.getMessage());
        }
        return allMyEventsIds;
    }

    public Team getTeamsInfo(int teamId) {
        Team team = new Team();
        conn = DatabaseConnection.connectionclass();

        String query = "SELECT * FROM dbo.[Team] WHERE Team_id = " + teamId;
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                team.setAbbreviation(result.getString("Team_abrev"));
                team.setName(result.getString("Team_name"));
                team.setLeague(result.getString("Team_league"));
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB Team", ex.getMessage());
        }
        return team;
    }

    public void addEventAttendance(int userId, int eventId) {
        conn = DatabaseConnection.connectionclass();

        String query = "INSERT INTO dbo.[UserEvent] (User_id, Event_id) VALUES ( " + userId + "," + eventId + ");";
        try {
            Statement statement = conn.createStatement();
            long result = statement.executeUpdate(query);

            if (result != -1) {
                Log.d("DB: ", "Added Event " + eventId);
            } else {
                Log.d("DB : ", "Error adding Event" + eventId);
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB INSERT", ex.getMessage());
        }
    }

    public void removeEventAttendance(int userId, int eventId) {
        conn = DatabaseConnection.connectionclass();

        String query = "DELETE FROM dbo.[UserEvent] WHERE User_id = " + userId + "AND " + "Event_id = " + eventId + ";";
        try {
            Statement statement = conn.createStatement();
            long result = statement.executeUpdate(query);

            if (result != -1) {
                Log.d("DB: ", "Removed Event " + eventId);
            } else {
                Log.d("DB : ", "Error removing Event" + eventId);
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB DELETE", ex.getMessage());
        }
    }

    public int getNumOfAttendants(int eventId) {
        int attendants = 0;
        conn = DatabaseConnection.connectionclass();

        String query = "SELECT COUNT(*) AS Event_attendants FROM dbo.[UserEvent] WHERE Event_id = " + eventId;
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                attendants = result.getInt("Event_attendants");
            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB Attendants", ex.getMessage());
        }
        return attendants;
    }

    public Event getEvent(int myEvent) {
        Event event = new Event();
        conn = DatabaseConnection.connectionclass();
        String query = "SELECT * FROM dbo.[Event] WHERE Event_id = " + myEvent + ";";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                Team team = new Team();
                event.setId(result.getInt("Event_id"));
                event.setName(result.getString("Event_name"));
                event.setAddress(result.getString("Event_address"));
                event.setDate(result.getString("Event_date"));
                event.setTime(result.getTime("Event_time"));
                event.setCity(result.getString("Event_city"));
                event.setProvince(result.getString("Event_province"));
                event.setPostalCode(result.getString("Event_postal_code"));
                event.setImage(result.getString("Event_image"));
                team = getTeamsInfo(result.getInt("Event_team1_id"));
                event.setTeam1(team);
                team = getTeamsInfo(result.getInt("Event_team2_id"));
                event.setTeam2(team);
                event.setCapacity(result.getInt("Event_capacity"));
                //event.setAttendants(getNumOfAttendants(event.getId()));
                //Log.e("atte", event.getAttendants() + "");

            }
            conn.close();
        } catch (Exception ex) {
            Log.e("DB Event", ex.getMessage());
        }
        return event;
    }

}

