package com.example.app_hotrotapluyen.gym.jdbcConnect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {

        public static Connection connect() {
            Connection connection = null;
            String  ip = "192.168.1.176";
            String database = "App_android_HTTLCN";
            String username = "sa";
            String password = "123";
            String port = "1433";

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String url = "jdbc:jtds:sqlserver://"  + ip + ":" + port + "/" + database;
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }


}
