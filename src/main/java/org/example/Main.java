package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParticipantsDAO participantsDAO = new ParticipantsDAO();
        ClubsDAO clubsDAO = new ClubsDAO();
        ExpertsDAO expertsDAO = new ExpertsDAO();
        RingsDAO ringsDAO = new RingsDAO();


        System.out.println("Выберите роль:");
        System.out.println("1. Администратор");
        System.out.println("2. Пользователь");
        int role = scanner.nextInt();
        scanner.nextLine(); // Считывание символа новой строки

        if (role == 1) {
            System.out.println("Введите пароль");
            int password = scanner.nextInt();
            scanner.nextLine();
            if (password == 1234){
                adminMenu(scanner, participantsDAO, clubsDAO, expertsDAO, ringsDAO);
            }
            else{
                System.out.println("Неправельный пароль");
            }

        } else if (role == 2) {
            userMenu(scanner, participantsDAO, clubsDAO, expertsDAO, ringsDAO);
        } else {
            System.out.println("Неверный выбор. Программа завершена.");
        }
    }

    private static void adminMenu(Scanner scanner, ParticipantsDAO participantsDAO, ClubsDAO clubsDAO, ExpertsDAO expertsDAO, RingsDAO ringsDAO) {
        while (true) {
            System.out.println("\nМеню администратора:");
            System.out.println("1. Добавить участника");
            System.out.println("2. Показать все данные");
            System.out.println("3. Обновить клуб участника");
            System.out.println("4. Добавить клуб");
            System.out.println("5. Добавить эксперта");
            System.out.println("6. Назначить ринг для породы");
            System.out.println("7. Редактировать данные клуба");
            System.out.println("8. Редактировать данные участника");
            System.out.println("9. Редактировать данные эксперта");
            System.out.println("10. Редактировать данные ринга");
            System.out.println("11. Выдать справку о занятии призового места участником");
            System.out.println("12. Показать отчет о выступлении клуба");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            AdminDAO adminDAO = new AdminDAO();
            switch (choice) {
                case 1 -> {
                    System.out.print("Имя участника: ");
                    String name = scanner.nextLine();
                    System.out.print("Название клуба: ");
                    String clubName = scanner.nextLine();
                    System.out.print("Кличка собаки: ");
                    String dogName = scanner.nextLine();
                    System.out.print("Порода: ");
                    String breed = scanner.nextLine();
                    System.out.print("Возраст: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Отец собаки: ");
                    String father = scanner.nextLine();
                    System.out.print("Мать собаки: ");
                    String mother = scanner.nextLine();
                    System.out.print("ID ринга: ");
                    int ringId = scanner.nextInt();

                    participantsDAO.addParticipant(name, clubName, dogName, breed, age, father, mother, ringId);
                }
                case 2 -> participantsDAO.showAllData();
                case 3 -> {
                    System.out.print("ID участника: ");
                    int participantId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Название нового клуба: ");
                    String newClubName = scanner.nextLine();
                    participantsDAO.updateParticipantClub(participantId, newClubName);
                }
                case 4 -> {
                    System.out.print("Название клуба: ");
                    String clubName = scanner.nextLine();
                    System.out.print("Регион клуба: ");
                    String region = scanner.nextLine();
                    clubsDAO.addClub(clubName, region);
                }
                case 5 -> {
                    System.out.print("ФИО эксперта: ");
                    String expertName = scanner.nextLine();
                    System.out.print("Название клуба эксперта: ");
                    String clubName = scanner.nextLine();
                    System.out.print("Номер ринга: ");
                    int ringId = scanner.nextInt();

                    expertsDAO.addExpert(expertName, clubName, ringId);
                }
                case 6 -> {
                    System.out.print("Порода: ");
                    String breed = scanner.nextLine();
                    System.out.print("Номер ринга: ");
                    int ringId = scanner.nextInt();

                    ringsDAO.assignRingToBreed(breed, ringId);
                }
                case 7 -> clubsDAO.editClub();
                case 8 -> participantsDAO.editParticipant();
                case 9 -> expertsDAO.editExpert();
                case 10 -> ringsDAO.editRing();

                case 11 -> adminDAO.getPrizeCertificate();
                case 12 -> adminDAO.getClubReport();
                case 0 -> {
                    System.out.println("Выход...");
                    return;
                }

                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void userMenu(Scanner scanner, ParticipantsDAO participantsDAO, ClubsDAO clubsDAO, ExpertsDAO expertsDAO, RingsDAO ringsDAO) {
        while (true) {
            System.out.println("\nМеню пользователя:");
            System.out.println("1. Узнать ринг участника");
            System.out.println("2. Узнать породы клуба");
            System.out.println("3. Узнать медали клуба");
            System.out.println("4. Узнать экспертов по породе");
            System.out.println("5. Узнать собаку эксперта");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Имя участника: ");
                    String participantName = scanner.nextLine();
                    participantsDAO.getParticipantRing(participantName);
                }
                case 2 -> {
                    System.out.print("Название клуба: ");
                    String clubName = scanner.nextLine();
                    clubsDAO.getBreedsByClub(clubName);
                }
                case 3 -> {
                    System.out.print("Название клуба: ");
                    String clubName = scanner.nextLine();
                    clubsDAO.getMedalsByClub(clubName);
                }
                case 4 -> {
                    System.out.print("Порода: ");
                    String breed = scanner.nextLine();
                    expertsDAO.getExpertsByBreed(breed);
                }
                case 5 -> {
                    System.out.print("ФИО эксперта: ");
                    String expertName = scanner.nextLine();
                    expertsDAO.getDogByExpert(expertName);
                }
                case 0 -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
