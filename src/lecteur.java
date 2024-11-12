import java.sql.*;
public class lecteur {
    public static void main(String[] args)
    {
        try
        {
            DriverManager.registerDriver(
                new oracle.jdbc.driver.OracleDriver();
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
