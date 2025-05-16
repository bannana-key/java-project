import model.Event;
import exception.DuplicateEventException;
import exception.TimeFormatException;
import notification.ConsoleNotifier;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScheduleManager manager = new ScheduleManager(new ConsoleNotifier());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        while (true) {
            LocalDate date = getValidDate(scanner, dateFormatter);
            LocalTime startTime = getValidTime(scanner, timeFormatter, "시작", date);
            LocalTime endTime;

            while (true) {
                endTime = getValidTime(scanner, timeFormatter, "끝", date);
                if (startTime.isAfter(endTime)) {
                    System.out.println("⛔ 시작 시간이 종료 시간보다 늦습니다. 다시 입력해주세요.");
                } else {
                    break;
                }
            }

            System.out.println("일정 내용을 입력하세요:");
            String description = scanner.nextLine();

            Event event = new Event(date, startTime, endTime, description);

            try {
                manager.addEvent(event);
                System.out.println("✅ 일정이 등록되었습니다: " + event);
            } catch (DuplicateEventException e) {
                System.out.println("❌ " + e.getMessage());
            }

            while (true) {
                System.out.println("계속 일정을 등록하시겠습니까? (y/n):");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("y")) break;
                else if (choice.equals("n")) {
                    System.out.println("\n📋 등록된 일정:");
                    manager.printAllEvents();

                    // 바로 메뉴로 이동하도록 수정
                    launchSearchAndDeleteMenu(scanner, manager);

                    System.out.println("\n⏳ 시간이 흐릅니다. 알림을 확인하세요...");
                    manager.startRealTimeAlarm();
                    scanner.close();
                    return;
                } else {
                    System.out.println("잘못된 입력입니다. 'y' 또는 'n'을 입력해주세요.");
                }
            }
        }
    }

    private static void launchSearchAndDeleteMenu(Scanner scanner, ScheduleManager manager) {
        while (true) {
            System.out.println("\n1. 일정 검색");
            System.out.println("2. 일정 삭제");
            System.out.println("3. 종료");
            System.out.print("선택: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.print("검색할 날짜를 입력하세요 (yyyy-MM 또는 yyyy-MM-dd): ");
                    String keyword = scanner.nextLine().trim();
                    manager.searchEvents(keyword);
                    break;
                case "2":
                    manager.printAllEvents();
                    System.out.print("삭제할 일정 번호를 입력하세요: ");
                    try {
                        int index = Integer.parseInt(scanner.nextLine().trim());
                        manager.deleteEvent(index - 1);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 숫자를 입력해주세요.");
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("❌ 올바른 선택이 아닙니다.");
            }
        }
    }

    private static LocalDate getValidDate(Scanner scanner, DateTimeFormatter formatter) {
        while (true) {
            System.out.println("📅 날짜를 입력하세요 (yyyy-MM-dd):");
            try {
                String input = scanner.nextLine();
                LocalDate date = LocalDate.parse(input, formatter);
                if (date.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("과거 날짜는 입력할 수 없습니다.");
                }
                return date;
            } catch (Exception e) {
                System.out.println("❌ 유효하지 않은 날짜입니다: " + e.getMessage());
            }
        }
    }

    private static LocalTime getValidTime(Scanner scanner, DateTimeFormatter formatter, String label, LocalDate date) {
        while (true) {
            System.out.printf("⏰ %s 시간을 입력하세요 (HH:mm):%n", label);
            String input = scanner.nextLine();
            try {
                LocalTime time = parseTime(input, formatter);
                if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
                    System.out.println("❌ 현재 시간 이전의 시간은 입력할 수 없습니다.");
                    continue;
                }
                return time;
            } catch (TimeFormatException e) {
                System.out.println("❌ 시간 형식 오류: " + e.getMessage());
            }
        }
    }

    private static LocalTime parseTime(String input, DateTimeFormatter formatter) throws TimeFormatException {
        try {
            return LocalTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new TimeFormatException("입력한 시간이 잘못되었습니다. 예) 14:30");
        }
    }
}
