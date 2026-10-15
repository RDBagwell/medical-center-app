package database;

import java.sql.*;
import java.util.logging.Logger;

import config.AppConfig;

public class database {
    private static final Logger logger = Logger.getLogger(database.class.getName());
    private Statement st;
    private ResultSet rs;
    private static Connection con = null;

    public database() throws SQLDataException {
        String userName = AppConfig.MYSQL_USER;
        String password = AppConfig.MYSQL_PASSWORD;
        String url = AppConfig.DB_URL;
        try {
            con = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            logger.severe(e.toString());
        }
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
            String clause
    ) throws SQLException {
        int i;
        StringBuilder placeholders = new StringBuilder();
        for (i = 0; i < columns.length - 1; i++) {
            placeholders.append(columns[i]).append(" = ?, ");
        }
        placeholders.append(columns[i]).append(" = ?");
        String sql = "UPDATE " + table + " SET " + placeholders + clause + ";";
        executeSQL(sql, vals);
    }

    public void delete(
            String table,
            int id
    ) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE `id` = ?";
        executeSQL(sql, id);
    }

    public void truncate(
            String tbl
    ) throws SQLException {
        String query = "TRUNCATE TABLE " + tbl;
        System.out.println(query);
        st = con.createStatement();
        st.executeQuery(query);
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
        PreparedStatement pst = con.prepareStatement(sql);
        bindParameters(pst, params);
        return pst.executeUpdate();
    }

}