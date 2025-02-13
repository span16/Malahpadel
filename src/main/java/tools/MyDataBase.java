package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String URL="jdbc:mysql://localhost:3306/pidev3A19";
    public final String USER="root";
    public final String PWD ="";
    private Connection cnx;
    private static MyDataBase instance;

    private MyDataBase(){
        try {
            cnx = DriverManager.getConnection(URL,USER,PWD);
            System.out.println("cnx etablie!!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static MyDataBase getInstance(){
        if(instance==null)
            instance= new MyDataBase();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
