package org.example;

/**
 * Интерфейс для получения данных о погоде
 */
public interface WeatherProvider {
    /**
     * Получение текущей температуры
     * @param latitude Широта
     * @param longitude Долгота
     * @return данных о погоде
     */
    WeatherData getCurrentWeather(double latitude, double longitude);

    /**
     * Получение средней температуры
     * @param latitude Широта
     * @param longitude Долгота
     * @param limit Число дней для прогноза
     * @return данных о погоде
     */
    double getAverageTemperature(double latitude, double longitude, int limit);
}
