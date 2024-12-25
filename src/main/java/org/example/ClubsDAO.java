package org.example;

import java.sql.*;
import java.util.Scanner;

public class ClubsDAO {
    private final Connection connection;

    public ClubsDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
    public void editClub() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID клуба для редактирования:");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Введите новое название клуба:");
            String newName = scanner.nextLine();

            System.out.println("Введите новый регион клуба:");
            String newRegion = scanner.nextLine();

            String query = "UPDATE clubs SET name = ?, region = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, newName);
                stmt.setString(2, newRegion);
                stmt.setInt(3, id);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Данные клуба успешно обновлены.");
                } else {
                    System.out.println("Клуб с указанным ID не найден.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка редактирования " + e.getMessage());
        }
    }
    public void addClub(String clubName, String region) {
        String query = "INSERT INTO clubs (name, region) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, clubName);
            stmt.setString(2, region);
            stmt.executeUpdate();
            System.out.println("Клуб успешно добавлен!");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления клуба: " + e.getMessage());
        }
    }

    public void getBreedsByClub(String clubName) {
        String query = "SELECT DISTINCT breed FROM participants WHERE club_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, clubName);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Породы, представленные клубом " + clubName + ":");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("breed"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения пород клуба: " + e.getMessage());
        }
    }

    public void getMedalsByClub(String clubName) {
        String query = "SELECT SUM(medals) AS total_medals FROM participants WHERE club_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, clubName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Всего медалей у клуба " + clubName + ": " + rs.getInt("total_medals"));
                } else {
                    System.out.println("У клуба " + clubName + " нет медалей.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения медалей клуба: " + e.getMessage());
        }
    }
}
