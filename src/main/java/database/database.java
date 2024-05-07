package database;

import java.sql.*;
import java.util.logging.Logger;

public class database {
    private static final Logger logger = Logger.getLogger(database.class.getName());
    private Statement st;
    private ResultSet rs;
    private Connection con = null;

    public database() throws SQLDataException {
        try {
            String userName = "root";
            String password = "password";
            String host = "localhost:3307";
            String dbName = "central_db";
            String url = "jdbc:mysql://" + host + "/" + dbName;
            con = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            logger.severe(e.toString());
        }
    }

    public ResultSet query(String query) throws SQLException {
        st = con.createStatement();
        rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet select(String table, String[] flds, String cnd, String ord) throws SQLException {
        String fields = "*";
        if (flds.length > 0)
            fields = String.join((", "), flds);
        if (!cnd.isEmpty())
            cnd = " WHERE " + cnd;
        if (!ord.isEmpty())
            ord = " ORDER BY " + ord;

        String query = "SELECT " + fields + " FROM " + table + cnd + ord + ";";
        // System.out.println(query);
        rs = null;
        st = con.createStatement();
        rs = st.executeQuery(query);
        return rs;
    }

    public void insert(String table, String[] flds, Object[] vals) throws SQLException {
        String columns = String.join((", "), flds);

        String temp2 = "?, ".repeat(Math.max(0, flds.length - 1)) +
                "?";

        String query = "INSERT INTO " + table + "(" + columns + ") VALUES (" + temp2 + ");";

        PreparedStatement pst = con.prepareStatement(query);

        for (int i = 0; i < vals.length; i++) {
            setParameter(pst, i + 1, vals[i]);
        }

        pst.executeUpdate();
    }

    public void update(String table, String[] flds, Object[] vals, int id) throws SQLException {
        int i;
        StringBuilder temp2 = new StringBuilder();
        for (i = 0; i < flds.length - 1; i++) {
            temp2.append(flds[i]).append(" = ?, ");
        }
        temp2.append(flds[i]).append(" = ?");
        String query = "UPDATE " + table + " SET " + temp2 + " WHERE `id` = " + id+ ";";
        PreparedStatement pst = con.prepareStatement(query);

        for (int j = 0; j < vals.length; j++) {
            setParameter(pst, j + 1, vals[j]);
        }

        pst.executeUpdate();
        System.out.println(pst);
    }

    public void delete(String table, int id) throws SQLException {
        String query = "DELETE " + table + " WHERE `id` = "+ id;
        st = con.createStatement();
        st.execute(query);
    }

    public void truncate(String tbl) throws SQLException {
        String query = "TRUNCATE TABLE "+tbl;
        System.out.println(query);
        st = con.createStatement();
        st.executeQuery(query);
    }

    public void close() throws SQLException {
        con.close();
    }

    private void setParameter(PreparedStatement pst, int index, Object value) throws SQLException {
        switch (value) {
            case null -> pst.setNull(index, Types.NULL);
            case String s -> pst.setString(index, s);
            case Integer i -> pst.setInt(index, i);
            case Float v -> pst.setFloat(index, v);
            case Double v -> pst.setDouble(index, v);
            case Boolean b -> pst.setBoolean(index, b);
            default -> {
            }
        }
    }

}
