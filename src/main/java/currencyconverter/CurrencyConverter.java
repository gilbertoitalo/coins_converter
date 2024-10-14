package src.main.java.currencyconverter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConverter {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private final String apiKey;

    public CurrencyConverter(String apiKey) {
        this.apiKey = apiKey;
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