package com.company;

import java.sql.*;

public class Main {
    public static final String DB_NAME="testjava.db";
    public static final String CONNECTION_STRING="jdbc:sqlite:/Users/ranyou/Documents/Java_Projects/TestingDB/"+DB_NAME;
    public static final String TABLE_CONTACTS="contacts";
    public static final String COLUME_NAME="name";
    public static final String COLUME_PHONE="phone";
    public static final String COLUME_EMAIL="email";


    public static void main(String[] args) {
	try{ Connection conn= DriverManager.getConnection(CONNECTION_STRING);
	    Statement statement=conn.createStatement();
	    statement.execute("DROP TABLE IF EXISTS "+ TABLE_CONTACTS);
	    statement.execute("CREATE Table IF NOT EXISTS "+TABLE_CONTACTS+
                "("+COLUME_NAME+" text,"+
                    COLUME_PHONE+" integer,"+
                    COLUME_EMAIL+" text "+
                ")");
	    insertContact(statement,"Joe",12345,"joe@gmail.com");
        insertContact(statement,"Jane",333555,"jane@gmail.com");
        insertContact(statement,"Fido",34563,"Fido@gmail.com");


        ResultSet results=statement.executeQuery("SELECT * from "+TABLE_CONTACTS);
        while (results.next()) {
            System.out.println(results.getString(COLUME_NAME)
                    +" "+results.getInt(COLUME_PHONE)
                    +" "+results.getString(COLUME_EMAIL));
        }
        results.close();

	    statement.close();
	    conn.close();
    } catch (SQLException e) {
        System.out.println("Something went wrong "+ e.getMessage()
        +e.getStackTrace());
    }
}
    private static void insertContact(Statement statement, String name, Integer phone, String email) throws SQLException {
        statement.execute("INSERT INTO "+TABLE_CONTACTS+
                "("+COLUME_NAME+","+
                COLUME_PHONE+","+
                COLUME_EMAIL+
                ")"+ "VALUES('"+ name +"+',"+phone+",'"+email+"')");
    }
}
