package utilities;

import java.sql.*;

/**
 * Created by Michal Ogrodniczak on 22/10/2015.
 * Utility class to handle MYSQL communications
 */
public class SQL_driver {
    private static String URL  = "jdbc:mysql://localhost:3306/BankDatabase";
    private static String USER = "root";
    private static String PASS = "";

    private Connection conn;

    /**
     * Parametrised constructor
     * @param URL url of the database
     * @param USER username for the database
     * @param PASS password for the user
     */
    public SQL_driver(String URL, String USER, String PASS) {
        this.URL  = URL;
        this.USER = USER;
        this.PASS = PASS;
    }

    /**
     * Default builder method, this returns SQL driver that uses default connection parameters.
     * @return
     */
    public static SQL_driver defaultSqlDriverBuilder(){
        return new SQL_driver(URL, USER, PASS);
    }

    public static void main(String[] args) throws SQLException {
        SQL_driver m = new SQL_driver(URL, USER, PASS);

        m.conn = DriverManager.getConnection(URL, USER, PASS);

    }

    /**
     * Connects to te database.
     * @return true if connection successful, false otherwise.
     */
    public boolean connect() {
        boolean connected = false;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            connected = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connected;
    }

    /**
     * returns an user's first name and last name if they are in the database, null otherwise
     * @param id id of the user we're searching for
     * @return first name last name of the user
     * @throws SQLException
     */
    public String getApplicantByID(int id) throws SQLException {
        PreparedStatement pstmt = null;
        String applicant = null;
        try {
            String SQL = "SELECT * FROM registeredapplicants WHERE AccountNum = ?";
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.first()) {
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                applicant = fName + " " + lName;
            }
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return applicant;
    }


    public String getURL() {
        return URL;
    }

    public String getUSER() {
        return USER;
    }

}
