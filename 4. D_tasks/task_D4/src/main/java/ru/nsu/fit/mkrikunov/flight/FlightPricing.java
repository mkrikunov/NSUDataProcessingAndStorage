package ru.nsu.fit.mkrikunov.flight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FlightPricing {

  public static void executeSqlFromFile(Connection connection, String fileName) throws SQLException {
    String sql = readSqlFromFile(fileName);
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.executeUpdate();
    }
  }

  public static String readSqlFromFile(String fileName) {
    try {
      return new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      System.err.println("Exception: " + e.getMessage());
      throw new RuntimeException("Failed to read SQL file: " + fileName);
    }
  }

  public static Map<String, Map<String, Map<String, Double>>> getFlightPrices(String URL, String USER, String PASSWORD) {
    Map<String, Map<String, Map<String, Double>>> flightPrices = new HashMap<>();

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
      String query = readSqlFromFile("src/main/java/ru/nsu/fit/mkrikunov/flight/sql/select_avg_prices.sql");
      try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          while (resultSet.next()) {
            String flightId = resultSet.getString("flight_id");
            String fareConditions = resultSet.getString("fare_conditions");
            double minPrice = resultSet.getDouble("min_price");
            double maxPrice = resultSet.getDouble("max_price");
            double avgPrice = resultSet.getDouble("avg_price");

            flightPrices
                .computeIfAbsent(flightId, k -> new HashMap<>())
                .computeIfAbsent(fareConditions, k -> new HashMap<>())
                .put("min_price", minPrice);
            flightPrices.get(flightId).get(fareConditions).put("max_price", maxPrice);
            flightPrices.get(flightId).get(fareConditions).put("avg_price", avgPrice);
          }
        }
      }
    } catch (SQLException e) {
      System.err.println("Exception: " + e.getMessage());
    }

    return flightPrices;
  }
}