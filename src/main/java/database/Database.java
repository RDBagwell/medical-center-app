package database;

import config.AppConfig;

import java.sql.*;
import java.util.Arrays;

public class Database implements AutoCloseable {
    private final Connection con;

    public Database() throws SQLException {
        String userName = AppConfig.MYSQL_USER;
        String password = AppConfig.MYSQL_PASSWORD;
        String url = AppConfig.DB_URL;
        con = DriverManager.getConnection(url, userName, password);
    }

    public ResultSet query(
            String sql,
            Object... params
    ) throws SQLException {
        PreparedStatement pst = con.prepareStatement(sql);
        bindParameters(pst, params);
        return pst.executeQuery();
    }

    public ResultSet select(
            String table,
            String[] columns,
            String clause,
            String ord,
            Object... params
    ) throws SQLException {
        final String fields = columns.length > 0 ? String.join((", "), columns) : "*";
        final String where = clause.isEmpty() ? "" : " WHERE " + clause;
        final String orderBy = ord.isEmpty() ? "" : " ORDER BY " + ord;

        String sql = "SELECT " + fields + " FROM " + table + where + orderBy + ";";

        return query(sql, params);
    }

    public void insert(
            String table,
            String[] cols,
            Object[] vals
    ) throws SQLException {
        String columns = String.join((", "), cols);
        String placeholders = "?, ".repeat(Math.max(0, cols.length - 1)) + "?";
        String sql = "INSERT INTO " + table + "(" + columns + ") VALUES (" + placeholders + ");";
        executeSQL(sql, vals);
    }

    public void update(
            String table,
            String[] columns,
            Object[] vals,
            String clause,
            Object... whereParams
    ) throws SQLException {

        if (clause.isBlank()) {
            throw new IllegalArgumentException("UPDATE requires a WHERE condition");
        }
        if(columns.length != vals.length){
            throw new IllegalArgumentException("columns and vals need to be the same length");
        }
        final String where = " WHERE " + clause;
        int i;
        StringBuilder placeholders = new StringBuilder();
        for (i = 0; i < columns.length - 1; i++) {
            placeholders.append(columns[i]).append(" = ?, ");
        }
        placeholders.append(columns[i]).append(" = ?");
        String sql = "UPDATE " + table + " SET " + placeholders + where + ";";
        // 1. A new, longer array: vals's contents copied in, the extra slots null
        Object[] params = Arrays.copyOf(vals, vals.length + whereParams.length);
        // 2. Copy whereParams into those empty slots at the end
        System.arraycopy(whereParams, 0, params, vals.length, whereParams.length);
        executeSQL(sql, params);
    }

    public void delete(
            String table,
            int id
    ) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE `id` = ?";
        executeSQL(sql, id);
    }

    public void close() throws SQLException {
        con.close();
    }

    private static void setParameter(
            PreparedStatement pst,
            int index,
            Object value
    ) throws SQLException {
        if (value == null) {
            pst.setNull(index, Types.NULL);
        } else {
            pst.setObject(index, value);
        }
    }

    private static void bindParameters(
            PreparedStatement pst,
            Object... params
    ) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            setParameter(pst, i + 1, params[i]);
        }
    }

    private int executeSQL(
            String sql,
            Object... params
    ) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            bindParameters(pst, params);
            return pst.executeUpdate();
        }
    }

}