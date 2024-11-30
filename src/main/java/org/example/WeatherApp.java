package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WeatherApp {
    /**
     * Широта Красногорск
     */
    public static final double LATITUDE = 55.49;
    /**
     * Долгота Красногорск
     */
    public static final double LONGITUDE = 37.19;
    /**
     * Число дней для прогноза
     */
    public static final int LIMIT = 7;

    public static void main(String[] args) {

        // Создаем провайдера с ключом API
        String apiKey = Config.getProperty("yandex.weather.api.key");
        WeatherProvider provider = new YandexWeatherProvider(apiKey);

        double latitude = LATITUDE;
        double longitude = LONGITUDE;
        int limit = LIMIT;

        // Получение текущей температуры
        WeatherData currentWeather = provider.getCurrentWeather(latitude, longitude);
        System.out.println("Текущая температура: " + currentWeather.getCurrentTemperature() + "°C");

        // Вычисление средней температуры
        double averageTemp = provider.getAverageTemperature(latitude, longitude, limit);
        System.out.println("Средняя температура за " + limit + " дней: " + averageTemp + "°C");
    }
}
