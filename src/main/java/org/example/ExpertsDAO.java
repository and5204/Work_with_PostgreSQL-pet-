package org.example;

import java.sql.*;
import java.util.Scanner;

public class ExpertsDAO {
    private final Connection connection;

    public ExpertsDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
    // Метод для редактирования данных эксперта
    public void editExpert() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID эксперта для редактирования:");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Введите новое имя эксперта:");
            String newName = scanner.nextLine();

            System.out.println("Введите новый клуб эксперта:");
            String newClubName = scanner.nextLine();

            System.out.println("Введите новый ID ринга:");
            int newRingId = scanner.nextInt();
            scanner.nextLine();

            String query = "UPDATE experts SET name = ?, club_name = ?, ring_id = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, newName);
                stmt.setString(2, newClubName);
                stmt.setInt(3, newRingId);
                stmt.setInt(4, id);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Данные эксперта успешно обновлены.");
                } else {
                    System.out.println("Эксперт с указанным ID не найден.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addExpert(String expertName, String clubName, int ringId) {
        String query = "INSERT INTO experts (name, club_name, ring_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, expertName);
            stmt.setString(2, clubName);
            stmt.setInt(3, ringId);
            stmt.executeUpdate();
            System.out.println("Эксперт успешно добавлен!");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления эксперта: " + e.getMessage());
        }
    }

    public void getExpertsByBreed(String breed) {
        String query = "SELECT DISTINCT experts.name FROM experts JOIN participants ON experts.ring_id = participants.ring_id WHERE participants.breed = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, breed);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Эксперты, обслуживающие породу " + breed + ":");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения экспертов по породе: " + e.getMessage());
        }
    }

    public void getDogByExpert(String expertName) {
        String query = "SELECT participants.dog_name FROM participants JOIN experts ON participants.ring_id = experts.ring_id WHERE experts.name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, expertName);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Собаки, судимые экспертом " + expertName + ":");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("dog_name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения собак эксперта: " + e.getMessage());
        }
    }
}
