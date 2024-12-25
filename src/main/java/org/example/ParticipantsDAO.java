package org.example;

import java.sql.*;
import java.util.Scanner;

public class ParticipantsDAO {
    private final Connection connection;

    public ParticipantsDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    public void addParticipant(String name, String clubName, String dogName, String breed, int age, String father, String mother, int ringId) {
        String query = "INSERT INTO participants (name, club_name, dog_name, breed, age, father, mother, ring_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, clubName);
            stmt.setString(3, dogName);
            stmt.setString(4, breed);
            stmt.setInt(5, age);
            stmt.setString(6, father);
            stmt.setString(7, mother);
            stmt.setInt(8, ringId);
            stmt.executeUpdate();
            System.out.println("Участник успешно добавлен!");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления участника: " + e.getMessage());
        }
    }

    public void getAllParticipants() {
        String query = "SELECT * FROM participants";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Имя: " + rs.getString("name"));
                System.out.println("Клуб: " + rs.getString("club_name"));
                System.out.println("Кличка собаки: " + rs.getString("dog_name"));
                System.out.println("Порода: " + rs.getString("breed"));
                System.out.println("Возраст: " + rs.getInt("age"));
                System.out.println("Отец: " + rs.getString("father"));
                System.out.println("Мать: " + rs.getString("mother"));
                System.out.println("Ринг: " + rs.getInt("ring_id"));
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения списка участников: " + e.getMessage());
        }
    }

    public void updateParticipantClub(int participantId, String newClubName) {
        String query = "UPDATE participants SET club_name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newClubName);
            stmt.setInt(2, participantId);
            stmt.executeUpdate();
            System.out.println("Клуб участника успешно обновлен!");
        } catch (SQLException e) {
            System.out.println("Ошибка обновления клуба участника: " + e.getMessage());
        }
    }
    // Метод для редактирования данных участника
    public void editParticipant() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ID участника для редактирования:");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Введите новое имя участника:");
            String newName = scanner.nextLine();

            System.out.println("Введите новое имя собаки:");
            String newDogName = scanner.nextLine();

            System.out.println("Введите новую породу собаки:");
            String newBreed = scanner.nextLine();

            System.out.println("Введите новый возраст собаки:");
            int newAge = scanner.nextInt();
            scanner.nextLine();

            String query = "UPDATE participants SET name = ?, dog_name = ?, breed = ?, age = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, newName);
                stmt.setString(2, newDogName);
                stmt.setString(3, newBreed);
                stmt.setInt(4, newAge);
                stmt.setInt(5, id);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Данные участника успешно обновлены.");
                } else {
                    System.out.println("Участник с указанным ID не найден.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getParticipantRing(String participantName) {
        String query = "SELECT ring_id FROM participants WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, participantName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Ринг участника: " + rs.getInt("ring_id"));
                } else {
                    System.out.println("Участник с таким именем не найден.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения ринга участника: " + e.getMessage());
        }
    }
    public void showAllData() {
        try (Statement statement = connection.createStatement()) {
            // Вывод всех клубов
            System.out.println("=== Clubs ===");
            ResultSet clubs = statement.executeQuery("SELECT * FROM clubs");
            while (clubs.next()) {
                System.out.printf("ID: %d, Name: %s, Region: %s%n",
                        clubs.getInt("id"),
                        clubs.getString("name"),
                        clubs.getString("region"));
            }

            // Вывод всех рингов
            System.out.println("\n=== Rings ===");
            ResultSet rings = statement.executeQuery("SELECT * FROM rings");
            while (rings.next()) {
                System.out.printf("ID: %d, Name: %s%n",
                        rings.getInt("id"),
                        rings.getString("name"));
            }

            // Вывод всех участников
            System.out.println("\n=== Participants ===");
            ResultSet participants = statement.executeQuery("SELECT * FROM participants");
            while (participants.next()) {
                System.out.printf("ID: %d, Name: %s, Club: %s, Dog Name: %s, Breed: %s, Age: %d, Father: %s, Mother: %s, Ring ID: %d, Medals: %d%n",
                        participants.getInt("id"),
                        participants.getString("name"),
                        participants.getString("club_name"),
                        participants.getString("dog_name"),
                        participants.getString("breed"),
                        participants.getInt("age"),
                        participants.getString("father"),
                        participants.getString("mother"),
                        participants.getInt("ring_id"),
                        participants.getInt("medals"));
            }

            // Вывод всех экспертов
            System.out.println("\n=== Experts ===");
            ResultSet experts = statement.executeQuery("SELECT * FROM experts");
            while (experts.next()) {
                System.out.printf("ID: %d, Name: %s, Club: %s, Ring ID: %d%n",
                        experts.getInt("id"),
                        experts.getString("name"),
                        experts.getString("club_name"),
                        experts.getInt("ring_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
