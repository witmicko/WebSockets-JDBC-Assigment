package utilities;

import java.sql.*;

/**
 * Created by witmi on 22/10/2015.
 */
public class SQL_driver {
    private static String URL  = "jdbc:mysql://localhost:3306/BankDatabase";
    private static String USER = "root";
    private static String PASS = "";

    private Connection conn;

    public SQL_driver(String URL, String USER, String PASS) {
        this.URL  = URL;
        this.USER = USER;
        this.PASS = PASS;
    }

    public static SQL_driver defaultSqlDriverBuilder(){
        return new SQL_driver(URL, USER, PASS);
    }

    public static void main(String[] args) throws SQLException {
        String URL  = "jdbc:mysql://localhost:3306/BankDatabase";
        String USER = "root";
        String PASS = "";
        SQL_driver m = new SQL_driver(URL, USER, PASS);

        m.conn = DriverManager.getConnection(URL, USER, PASS);

        System.out.println(m.getApplicantByID(1002));


    }

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

    public Connection getConn() {
        return conn;
    }
}
