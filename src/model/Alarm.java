package model;  // 패키지 명시

import java.time.format.DateTimeFormatter;
import notification.Notifiable;
import exception.AlarmException;

public class Alarm {
    private Event event;
    private Notifiable notifier;

    public Alarm(Event event, Notifiable notifier) {
        this.event = event;
        this.notifier = notifier;
    }

    public void trigger() throws AlarmException {
        if (event == null || notifier == null) {
            throw new AlarmException("알람 정보가 부족합니다.");
        }

        String message = String.format(
            "%s에 일정이 있습니다: %s (%s ~ %s)",
            event.getDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            event.getDescription(),
            event.getStartTime(),
            event.getEndTime()
        );

        notifier.sendNotification(message);
    }
}
