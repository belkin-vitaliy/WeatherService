package org.example;

/**
 * Класс для хранения данных о погоде
 */
public class WeatherData {
    private final double currentTemperature;

    public WeatherData(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }
}
