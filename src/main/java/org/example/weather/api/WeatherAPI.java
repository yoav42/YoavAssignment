package org.example.weather.api;

import org.example.weather.model.City;
import org.example.weather.model.DailyTemp;

import java.util.List;
import java.util.Set;

public interface WeatherAPI {
    Set<City> getAllCitiesByIds(Set<String> cityIds);
    List<DailyTemp> getLastYearTemprature(String cityId);
}