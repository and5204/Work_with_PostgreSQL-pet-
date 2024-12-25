package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminDAO {


    private final Connection connection;

    public AdminDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    // Метод для выдачи справки о занятии участником призового места
    public void getPrizeCertificate() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID участника:");
            int participantId = scanner.nextInt();
            scanner.nextLine();

            String query = """
                    SELECT p.name AS participant_name, p.dog_name, p.breed, p.medals
                    FROM participants p
                    WHERE p.id = ? AND p.medals > 0
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, participantId);

                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    System.out.printf("Справка о призах участника:%n" +
                                    "Имя участника: %s%n" +
                                    "Имя собаки: %s%n" +
                                    "Порода: %s%n" +
                                    "Количество медалей: %d%n",
                            resultSet.getString("participant_name"),
                            resultSet.getString("dog_name"),
                            resultSet.getString("breed"),
                            resultSet.getInt("medals"));
                } else {
                    System.out.println("Участник не найден или не имеет медалей.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для отчета о выступлении клуба
    public void getClubReport() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите название клуба:");
            String clubName = scanner.nextLine();

            String query = """
                    SELECT c.name AS club_name,
                           COUNT(DISTINCT p.id) AS participants_count,
                           COUNT(DISTINCT p.breed) AS breed_count,
                           SUM(p.medals) AS total_medals
                    FROM clubs c
                    LEFT JOIN participants p ON c.name = p.club_name
                    WHERE c.name = ?
                    GROUP BY c.name
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, clubName);

                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    System.out.printf("Отчет о выступлении клуба '%s':%n" +
                                    "Количество участников: %d%n" +
                                    "Количество пород: %d%n" +
                                    "Всего медалей: %d%n",
                            resultSet.getString("club_name"),
                            resultSet.getInt("participants_count"),
                            resultSet.getInt("breed_count"),
                            resultSet.getInt("total_medals"));
                } else {
                    System.out.println("Клуб не найден.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
