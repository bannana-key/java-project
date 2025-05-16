import model.Event;
import model.Alarm;
import notification.Notifiable;
import exception.DuplicateEventException;
import exception.AlarmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ScheduleManager {
    private List<Event> events;
    private Notifiable notifier;

    public ScheduleManager(Notifiable notifier) {
        this.events = new ArrayList<>();
        this.notifier = notifier;
    }

    public void addEvent(Event newEvent) throws DuplicateEventException {
        for (Event event : events) {
            if (event.getDate().equals(newEvent.getDate()) &&
                event.getStartTime().isBefore(newEvent.getEndTime()) &&
                newEvent.getStartTime().isBefore(event.getEndTime())) {
                throw new DuplicateEventException("겹치는 일정이 존재합니다: " + event);
            }
        }
        events.add(newEvent);
    }

    public void printAllEvents() {
        if (events.isEmpty()) {
            System.out.println("📭 등록된 일정이 없습니다.");
            return;
        }

        events.stream()
                .sorted(Comparator.comparing(Event::getDate).thenComparing(Event::getStartTime))
                .forEach(event -> System.out.println((events.indexOf(event) + 1) + ". " + event));
    }

    public void deleteEvent(int index) {
        if (index < 0 || index >= events.size()) {
            System.out.println("❌ 유효하지 않은 번호입니다.");
            return;
        }
        Event removed = events.remove(index);
        System.out.println("🗑️ 삭제된 일정: " + removed);
    }

    public void searchEvents(String keyword) {
        List<Event> results = new ArrayList<>();
        for (Event e : events) {
            if (e.getDate().toString().startsWith(keyword)) {
                results.add(e);
            }
        }

        results.sort(Comparator.comparing(Event::getDate).thenComparing(Event::getStartTime));

        if (results.isEmpty()) {
            System.out.println("🔎 해당 날짜에 일정을 찾을 수 없습니다.");
        } else {
            System.out.println("🔍 검색된 일정:");
            for (Event e : results) {
                System.out.println(e);
            }
        }
    }

    public void startRealTimeAlarm() {
        Thread thread = new Thread(() -> {
            while (!events.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();

                Iterator<Event> iterator = events.iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());

                    if (now.isAfter(eventDateTime) || now.equals(eventDateTime)) {
                        Alarm alarm = new Alarm(event, notifier);
                        try {
                            alarm.trigger();
                        } catch (AlarmException e) {
                            System.out.println("❗ 알림 오류: " + e.getMessage());
                        }
                        iterator.remove();
                    }
                }

                try {
                    Thread.sleep(60 * 1000); // 1분마다 확인
                } catch (InterruptedException e) {
                    System.out.println("⏸️ 알림 스레드가 중단되었습니다.");
                }
            }
        });

        thread.setDaemon(false); // 메인 스레드 종료 시 같이 종료되지 않도록
        thread.start();
    }
}
