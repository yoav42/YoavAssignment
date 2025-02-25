package org.example.weather;

import org.example.weather.aggregator.AggregationType;
import org.example.weather.aggregator.AggregatorStrategy;
import org.example.weather.aggregator.AverageAggregator;
import org.example.weather.api.WeatherAPI;
import org.example.weather.model.City;
import org.example.weather.model.CityTemperature;
import org.example.weather.model.DailyTemp;
import org.example.weather.thread.ParallelExecutor;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class WeatherProgram {
    private static final int POPULATION_THRESHOLD = 50000;
    private static final int TOP_CITIES_COUNT = 3;

    private WeatherAPI weatherAPI;
    private ParallelExecutor parallelExecutor;
    private Map<AggregationType, AggregatorStrategy> aggregators;

    public WeatherProgram(WeatherAPI weatherAPI) {
        this.weatherAPI = weatherAPI;
        this.parallelExecutor = new ParallelExecutor();

        this.aggregators = new HashMap<>();
        aggregators.put(AggregationType.AVG, new AverageAggregator());
    }

    public List<CityTemperature> getTopCitiesByAggregation(Set<String> cityIds, AggregationType aggregationType) {
        AggregatorStrategy aggregator = aggregators.get(aggregationType);

        List<City> bigCities = weatherAPI.getAllCitiesByIds(cityIds).stream()
                .filter(city -> city.getPopulation() > POPULATION_THRESHOLD)
                .toList();

        List<CityTemperature> resultAggregations = runParallelAggregations(bigCities, aggregator);

        return resultAggregations.stream()
                .sorted((a, b) -> Double.compare(b.getAggregatedTemperature(), a.getAggregatedTemperature()))
                .limit(TOP_CITIES_COUNT)
                .collect(Collectors.toList());
    }

    private List<CityTemperature> runParallelAggregations(List<City> cities, AggregatorStrategy aggregator) {
        List<Callable<CityTemperature>> tasks = new ArrayList<>();
        for (City city : cities) {
            tasks.add(() -> processCity(city, aggregator));
        }

        return parallelExecutor.executeTasks(tasks, 5, TimeUnit.SECONDS);
    }

    private CityTemperature processCity(City city, AggregatorStrategy aggregator) {
        List<DailyTemp> temps = weatherAPI.getLastYearTemprature(city.getId());
        double aggregatedTemp = aggregator.aggregate(temps);
        return new CityTemperature(city, aggregatedTemp);
    }
}