package servlet;

import org.json.*;
import java.util.*;
import java.sql.*;
import java.util.logging.Logger;

import database.database;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static void main(String[] args) {
        try {
            database db = new database();
            String[] arr = {};
            List<JSONObject> jsonData;
            try (ResultSet rs = db.select("`states`", arr, "", "")) {
                jsonData = new ArrayList<>();
                while (rs.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", rs.getInt("id"));
                    jsonObject.put("name", rs.getString("name"));
                    jsonObject.put("code", rs.getString("code"));
                    jsonData.add(jsonObject);
                }
            }
            JSONArray jsonArray = new JSONArray(jsonData);
            System.out.println(jsonArray.toString());
        } catch (SQLException e) {
            logger.severe(e.toString());
        }

    }

}