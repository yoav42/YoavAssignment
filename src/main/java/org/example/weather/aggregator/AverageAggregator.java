package org.example.weather.aggregator;

import org.example.weather.model.DailyTemp;

import java.util.List;

public class AverageAggregator implements AggregatorStrategy{

    public double aggregate(List<DailyTemp> temperatures) {
        return temperatures.stream()
                .mapToDouble(DailyTemp::getTemperature)
                .average()
                .orElse(0.0);
    }
}
