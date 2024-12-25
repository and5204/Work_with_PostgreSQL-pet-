package org.example;

import java.sql.*;
import java.util.Scanner;

public class RingsDAO {
    private final Connection connection;

    public RingsDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
    // Метод для редактирования данных ринга
    public void editRing() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID ринга для редактирования:");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Введите новое название ринга:");
            String newName = scanner.nextLine();

            String query = "UPDATE rings SET name = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, newName);
                stmt.setInt(2, id);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Данные ринга успешно обновлены.");
                } else {
                    System.out.println("Ринг с указанным ID не найден.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void assignRingToBreed(String breed, int ringId) {
        String query = "UPDATE participants SET ring_id = ? WHERE breed = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ringId);
            stmt.setString(2, breed);
            stmt.executeUpdate();
            System.out.println("Ринг успешно назначен породе!");
        } catch (SQLException e) {
            System.out.println("Ошибка назначения ринга породе: " + e.getMessage());
        }
    }
}
