import java.sql.*;
import java.time.LocalDateTime;

public class DBUtil {

    public static Connection connect() throws Exception {
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/plagiarism",
        "root",
        "@1Two3456"
    );
}

    

    public static void createTable() {
        try (Connection con = connect();
             Statement st = con.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS reports(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fileA TEXT, fileB TEXT, similarity REAL, date TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveReport(String a, String b, double sim) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO reports(fileA,fileB,similarity,date) VALUES(?,?,?,?)")) {

            ps.setString(1, a);
            ps.setString(2, b);
            ps.setDouble(3, sim);
            ps.setString(4, LocalDateTime.now().toString());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

