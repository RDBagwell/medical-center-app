package app;

import database.Database;
import helper.AutoLogout;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try (Database db = new Database()){
            AutoLogout autoLogout = new AutoLogout();
            autoLogout.startLogoutTimer();
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
            System.out.println(jsonArray);
        } catch (SQLException e) {
            logger.severe(e.toString());
        }

    }

}