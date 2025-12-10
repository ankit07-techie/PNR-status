
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PNRTracker {
    private static PNRService pnrService;
    private static NotificationService notificationService;
    private static String currentPnr;
    private static String lastKnownStatus;

    public static void main(String[] args) {
        pnrService = new PNRService();
        notificationService = new NotificationService();
        Scanner scanner = new Scanner(System.in);
        try {

            System.out.println("======================================");
            System.out.println("      PNR STATUS TRACKER (MOCK)       ");
            System.out.println("======================================");
            System.out.print("Enter PNR Number to Track: ");
            currentPnr = scanner.nextLine().trim();

            if (currentPnr.isEmpty()) {
                System.out.println("Invalid PNR. Exiting.");
                return;
            }

            System.out.println("Tracking started for PNR: " + currentPnr);

            // Initial check
            lastKnownStatus = pnrService.getStatus(currentPnr);
            System.out.println("Initial Status: " + lastKnownStatus);

            // Schedule periodic checks (e.g., every 5 seconds for demo purposes)
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            Runnable checkTask = () -> {
                try {
                    String newStatus = pnrService.getStatus(currentPnr);
                    if (!newStatus.equals(lastKnownStatus)) {
                        notificationService.notifyUser(currentPnr, newStatus);
                        lastKnownStatus = newStatus;
                    } else {
                        // System.out.print("."); // Heartbeat for console
                    }
                } catch (Exception e) {
                    System.err.println("Error checking status: " + e.getMessage());
                }
            };

            // Check every 5 seconds
            scheduler.scheduleAtFixedRate(checkTask, 5, 5, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                java.nio.file.Files.write(java.nio.file.Paths.get("error.log"),
                        (e.toString() + "\n" + java.util.Arrays.toString(e.getStackTrace())).getBytes());
            } catch (Exception io) {
            }
            System.out.println("Press Enter to exit...");
            new java.util.Scanner(System.in).nextLine();
        }
    }
}
