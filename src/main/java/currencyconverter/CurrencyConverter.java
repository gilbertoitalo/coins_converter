package src.main.java.currencyconverter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class CurrencyConverter {

    Dotenv dotenv = Dotenv.configure()
            .directory("C:/Users/Gilberto/Documents/ProgramOne/coins_converter")
            .filename(".env_file")
            .load();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    ;
    private final String apiKey;

    {
        apiKey = dotenv.get("API_KEY");
    }


    public JsonObject getExchangeRates(String baseCurrency) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

        if (jsonResponse.get("result").getAsString().equals("success")) {
            return jsonResponse.getAsJsonObject("conversion_rates");
        } else {
            System.out.println("Error fetching exchange rates. Please try again later.");
            return null;
        }
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency, JsonObject rates) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        if (!fromCurrency.equals("USD")) {
            amount = amount / rates.get(fromCurrency).getAsDouble();
        }
        return amount * rates.get(toCurrency).getAsDouble();
    }
}