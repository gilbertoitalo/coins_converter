import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiClient {
    private static final String API_URL = "https://api.exchangeratesapi.io/latest";
    private static final String apikey;

    public ApiClient() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
            this.apiKey = properties.getProperty("api.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRates(String baseCurrency) {
        String url = API_URL + "?base=" + baseCurrency + "&access_key=" + API_KEY;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}