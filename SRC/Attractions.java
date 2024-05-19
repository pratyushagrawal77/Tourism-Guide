import java.sql.*;
import java.util.Scanner;
public class Attractions {
    public static void main(String[] args) {
        // JDBC connection parameters
        String url = "jdbc:mysql://localhost:3306/travel_guide";
        String username = "root";
        String password = "JustThisOnce";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the State name:");
        String statename = scanner.nextLine();
        System.out.println("Enter the City name:");
        String cityname = scanner.nextLine();
        String sqlQuery = "SELECT city, location, rating FROM " + statename + " WHERE city = '" + cityname + "'";
        try {
            // Create an instance of DatabaseConnector and establish connection
            DatabaseConnector connector = new DatabaseConnector(url, username, password);
            connector.connect();
            ResultSet resultSet = connector.executeQuery(sqlQuery);
            System.out.println("Attractions:");
            System.out.println("----------------------------------------------------");
            System.out.printf("%-15s %-30s %-10s%n", "City", "Location", "Rating");
            System.out.println("----------------------------------------------------");
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                String location = resultSet.getString("location");
                float rating = resultSet.getFloat("rating");
                System.out.printf("%-15s %-30s %-10.1f%n", city, location, rating);
            }
            System.out.println("----------------------------------------------------");
            resultSet.close();
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
