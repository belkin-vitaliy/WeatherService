package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.example.WeatherApp.LATITUDE;
import static org.example.WeatherApp.LONGITUDE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class YandexWeatherProviderTest {

    private static final String API_KEY = "9eaa2a95-8d92-4246-ad17-b00167d27061";
    public static final int HTTP_OK = 200;
    public static final int LIMIT = 3;
    public static final double AVERAGE_TEMP = 15.0;
    public static final double ERROR_DELTA = 0.01;
    public static final double CURRENT_TEMP = 15.0;
    private YandexWeatherProvider provider;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() {
        // Мок-объект для HTTP-клиента
        mockHttpClient = mock(HttpClient.class);
        provider = new YandexWeatherProvider(API_KEY, mockHttpClient);

    }

    @Test
    void getCurrentWeather() throws IOException, InterruptedException {
        // Мок-ответ от API
        String mockResponse = """
                {
                    "fact": {
                        "temp": 15
                    }
                }
                """;

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(mockResponse);
        when(mockHttpResponse.statusCode()).thenReturn(HTTP_OK);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);

        // Тестируем метод
        WeatherData weatherData = provider.getCurrentWeather(LATITUDE, LONGITUDE);
        assertNotNull(weatherData);
        assertEquals(CURRENT_TEMP, weatherData.getCurrentTemperature(), ERROR_DELTA);
    }

    @Test
    void getAverageTemperature() throws IOException, InterruptedException {
        // Мок-ответ от API
        String mockResponse = """
                {
                    "forecasts": [
                        {"parts": {"day": {"temp_avg": 10}}},
                        {"parts": {"day": {"temp_avg": 15}}},
                        {"parts": {"day": {"temp_avg": 20}}}
                    ]
                }
                """;

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(mockResponse);
        when(mockHttpResponse.statusCode()).thenReturn(HTTP_OK);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);

        // Тестируем метод
        double averageTemp = provider.getAverageTemperature(LATITUDE, LONGITUDE, LIMIT);
        assertEquals(AVERAGE_TEMP, averageTemp, ERROR_DELTA);
    }
}