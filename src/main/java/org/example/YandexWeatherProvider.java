package org.example;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.IntStream;

public class YandexWeatherProvider implements WeatherProvider{

    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";
    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;

    /**
     * Основной конструктор
     * @param apiKey ключ
     * @param client http-client
     */
    public YandexWeatherProvider(String apiKey, HttpClient client) {
        this.apiKey = apiKey;
        this.client = client != null ? client : HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    /**
     * Упрощенный конструктор для обычного использования
     * @param apiKey ключ
     */
    public YandexWeatherProvider(String apiKey) {
        this(apiKey, null);
    }


    @Override
    public WeatherData getCurrentWeather(double latitude, double longitude) {
        try {
            // Формируем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?lat=" + latitude + "&lon=" + longitude))
                    .header("X-Yandex-Weather-Key", apiKey)
                    .GET()
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());

            // Извлекаем текущую температуру
            double currentTemp = root.path("fact").path("temp").asDouble();
            return new WeatherData(currentTemp);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении данных о погоде: " + e.getMessage(), e);
        }
    }

    @Override
    public double getAverageTemperature(double latitude, double longitude, int limit) {
        try {
            // Формируем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?lat=" + latitude + "&lon=" + longitude + "&limit=" + limit))
                    .header("X-Yandex-Weather-Key", apiKey)
                    .GET()
                    .build();

            // Выполняем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());

            // Вычисляем среднюю температуру
            JsonNode forecasts = root.path("forecasts");
            return IntStream.range(0, forecasts.size())
                    .mapToDouble(i -> forecasts.get(i).path("parts").path("day").path("temp_avg").asDouble())
                    .average()
                    .orElse(Double.NaN);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при вычислении средней температуры: " + e.getMessage(), e);
        }
    }
}
