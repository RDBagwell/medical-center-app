package servlet;

import java.io.*;
import java.util.*;
import java.sql.*;
import database.database;

public class Junk {
    public static void main(String[] args) {
        try {
            database db = new database();
            String[] arr = {};
            ResultSet rs = db.select("`test`", arr, "", "");
            List<Map<String, Object>> jsonData = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> jsonObject = new HashMap<>();
                jsonObject.put("id", rs.getInt("id"));
                jsonObject.put("name", rs.getString("name"));

                jsonData.add(jsonObject);
            }

            StringBuilder json = new StringBuilder("[");
            for (Map<String, Object> obj : jsonData) {
                json.append(mapToJson(obj)).append(",");
            }
            if (!jsonData.isEmpty()) {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");

            // ResultSetMetaData metaData = rs.getMetaData();
            // int columnCount = metaData.getColumnCount();
            // while (rs.next()) {
            // List<Object> values = new ArrayList<>();
            // for(int i = 1; i <= columnCount; i++){
            // Object value = rs.getObject(i);
            // values.add(value);
            // }
            // data.add(values);
            // }

            File file = new File("C:/Users/erbag/OneDrive/Desktop/medical-center-app/src/main/resources/file.txt");
            file.getParentFile().mkdirs();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(json);
            printWriter.println("hello");
            printWriter.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    // Convert a map to a JSON object string
    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        // StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof Number) {
                json.append(entry.getValue());
            } else {
                json.append("\"").append(entry.getValue()).append("\"");
            }
            json.append(",");
        }
        if (!map.isEmpty()) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}");
        return json.toString();
    }

}
