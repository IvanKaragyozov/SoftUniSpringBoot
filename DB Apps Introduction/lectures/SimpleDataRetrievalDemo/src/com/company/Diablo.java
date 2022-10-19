package com.company;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Diablo {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "12345");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/diablo", properties);

        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        PreparedStatement query = connection.prepareStatement("SELECT username, first_name, last_name FROM ... WHERE username = ?");

        query.setString(1, username);

        ResultSet result = query.executeQuery();

        if(result.first()){ // valid username

        } else {
            System.out.println("No such user exists");
        }
    }
}


