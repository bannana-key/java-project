package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    public Event(LocalDate date, LocalTime startTime, LocalTime endTime, String description) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s ~ %s)", date, description, startTime, endTime);
    }
}
