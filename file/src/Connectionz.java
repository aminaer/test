import java.sql.Connection;
import java.sql.DriverManager;

public class Connectionz {

static Connection con;
	
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			
		}catch (Exception ex) {
			System.out.println(""+ex);
		}
		return con;
	}
}
