package ru.nsu.fit.mkrikunov.flight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class Main {

  private static final String URL = "";
  private static final String USER = "";
  private static final String PASSWORD = "";

  public static void main(String[] args) {
    Map<String, Map<String, Map<String, Double>>> flightPrices = FlightPricing.getFlightPrices(URL, USER, PASSWORD);

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
      // Создаем таблицу правил ценообразования
      FlightPricing.executeSqlFromFile(connection,
          "src/main/java/ru/nsu/fit/mkrikunov/flight/sql/create_pricing_rules_table.sql");
      // Заполняем таблицу данными
      String insertQuery = FlightPricing.readSqlFromFile(
          "src/main/java/ru/nsu/fit/mkrikunov/flight/sql/insert_pricing_rules.sql");
      try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
        for (Map.Entry<String, Map<String, Map<String, Double>>> flightEntry : flightPrices.entrySet()) {
          String flightId = flightEntry.getKey();
          for (Map.Entry<String, Map<String, Double>> fareEntry : flightEntry.getValue().entrySet()) {
            String fareConditions = fareEntry.getKey();
            double avgPrice = fareEntry.getValue().get("avg_price");
            double minPrice = fareEntry.getValue().get("min_price");
            double maxPrice = fareEntry.getValue().get("max_price");
            preparedStatement.setString(1, flightId);
            preparedStatement.setString(2, fareConditions);
            preparedStatement.setDouble(3, minPrice);
            preparedStatement.setDouble(4, maxPrice);
            preparedStatement.setDouble(5, avgPrice);
            preparedStatement.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      System.err.println("Exception: " + e.getMessage());
    }
  }
}
