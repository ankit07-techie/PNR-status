
public class TestPNR {
    public static void main(String[] args) {
        System.out.println("Running PNR Service Test...");
        PNRService service = new PNRService();
        String pnr = "6461250996";

        String status = service.getStatus(pnr);
        System.out.println("Initial Status: " + status);

        if (status == null || status.isEmpty()) {
            System.err.println("FAIL: Status is empty");
            System.exit(1);
        }

        System.out.println("PASS: Service returned valid status.");
    }
}
