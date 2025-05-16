package notification;

public class ConsoleNotifier implements Notifiable {
    @Override
    public void sendNotification(String message) {
        System.out.println("🔔 알림: " + message);
    }
}
