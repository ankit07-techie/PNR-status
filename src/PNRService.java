
import java.util.Scanner;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PNRService {

    public PNRService() {
    }

    /**
     * Fetches the PNR status using the Real Application API.
     */
    public String getStatus(String pnr) {
        return fetchFromRealApi(pnr);
    }

    private String fetchFromRealApi(String pnr) {
        String apiKey = "4ccf4ca58cmsh3095f21fb3171a3p1d2d3bjsne38fdbf1db0f";
        String apiHost = "irctc-indian-railway-pnr-status.p.rapidapi.com";
        String apiUrl = "https://irctc-indian-railway-pnr-status.p.rapidapi.com/getPNRStatus/" + pnr;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-rapidapi-host", apiHost);
            conn.setRequestProperty("x-rapidapi-key", apiKey);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream responseStream = conn.getInputStream();
                Scanner scanner = new Scanner(responseStream);
                String responseBody = scanner.useDelimiter("\\A").next();
                scanner.close();

                return parseStatusSimple(responseBody);
            } else {
                return "Error: API returned " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String parseStatusSimple(String json) {
        // We need to exclude dynamic fields like "generatedTimeStamp" or "timeStamp"
        // to prevent false positive updates.

        StringBuilder stableInfo = new StringBuilder();
        stableInfo.append("PNR Status Summary:\n");

        // Extract known stable fields if present
        String[] keys = { "currentStatus", "currentStatusDetails", "bookingStatus", "bookingStatusDetails", "isWL",
                "ticketFare", "arrivalDate" };

        // Remove quotes and braces for easier parsing
        String cleanJson = json.replace("\"", "").replace("{", "").replace("}", "");
        String[] parts = cleanJson.split(",");

        for (String part : parts) {
            for (String key : keys) {
                if (part.trim().startsWith(key + ":")) {
                    stableInfo.append(part.trim()).append("\n");
                }
            }
        }

        if (stableInfo.length() <= 20) {
            // Fallback if parsing fails (return raw but warn)
            return "Raw Response (Verify Changes): " + cleanJson;
        }

        return stableInfo.toString();
    }
}
