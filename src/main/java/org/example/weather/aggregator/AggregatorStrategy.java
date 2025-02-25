package org.example.weather.aggregator;

import org.example.weather.model.DailyTemp;

import java.util.List;

public interface AggregatorStrategy {
    double aggregate(List<DailyTemp> temperatures);
}
