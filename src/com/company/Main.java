package com.company;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
	try{ Connection conn= DriverManager.getConnection("jdbc:sqlite:/Users/ranyou/Documents/Java_Projects/TestingDB/testjava.db");
//        conn.setAutoCommit(false);
	    Statement statement=conn.createStatement();
//	    statement.execute("CREATE Table IF NOT EXISTS contacts "+
//                "(name TEXT,phone INTEGER,email TEXT)");
//	    statement.execute("INSERT INTO contacts (name,phone,email)"+
//                "VALUES('TIM',123445,'tim@gmail.com')");
//        statement.execute("INSERT INTO contacts (name,phone,email)"+
//                "VALUES('Joe',34567,'Joeb@gmail.com')");
//        statement.execute("INSERT INTO contacts (name,phone,email)"+
//                "VALUES('Jane',4327687,'jane@gmail.com')");
//        statement.execute("INSERT INTO contacts (name,phone,email)"+
//                "VALUES('Fido',334555,'Fido@gmail.com')");
//        statement.execute("UPDATE contacts SET phone=009999 WHERE name='Jane'");
        statement.execute("SELECT * from contacts");
        ResultSet results=statement.getResultSet();
        while (results.next()) {
            System.out.println(results.getString("name")+results.getInt("phone")+results.getString("email"));

        }
        results.close();

	    statement.close();
	    conn.close();
    } catch (SQLException e) {
        System.out.println("Something went wrong "+ e.getMessage());
    }
    }
}
