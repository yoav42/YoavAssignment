import org.example.weather.WeatherProgram;
import org.example.weather.api.WeatherAPI;
import org.example.weather.model.City;
import org.example.weather.model.CityTemperature;
import org.example.weather.model.DailyTemp;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.example.weather.aggregator.AggregationType.AVG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class WeatherProgramTest {

    @Test
    public void testProgram() {
        WeatherAPI testingAPI = new ApiTestingImpl();
        WeatherProgram program = new WeatherProgram(testingAPI);
        Set<String> cities = Set.of("city1", "city2", "city3", "city4", "city5");
        List<CityTemperature> aggregations = program.getTopCitiesByAggregation(cities, AVG);

        assertEquals(3, aggregations.size());
        assertTrue("Expected temperature not found", aggregations.stream()
                .anyMatch(city ->
                        city.getCity().getId().equals("city1") && city.getAggregatedTemperature() == 17.125));
        assertTrue(aggregations.stream()
                .anyMatch(city -> city.getCity().getId().equals("city4")));
        assertTrue(aggregations.stream()
                .anyMatch(city -> city.getCity().getId().equals("city5")));

        assertTrue(aggregations.stream()
                .noneMatch(city -> city.getCity().getId().equals("city2")));
        assertTrue(aggregations.stream()
                .noneMatch(city -> city.getCity().getId().equals("city3")));
    }


    class ApiTestingImpl implements WeatherAPI {
        @Override
        public Set<City> getAllCitiesByIds(Set<String> cityIds) {
            return Set.of(
                    City.builder()
                            .id("city1")
                            .name("City 1")
                            .population(51000)
                            .build(),
                    City.builder()
                            .id("city2")
                            .name("City 2")
                            .population(20000)
                            .build(),
                    City.builder()
                            .id("city3")
                            .name("City 3")
                            .population(10000)
                            .build(),
                    City.builder()
                            .id("city4")
                            .name("City 4")
                            .population(90000)
                            .build(),
                    City.builder()
                            .id("city5")
                            .name("City 5")
                            .population(1000000)
                            .build());
        }

        @Override
        public List<DailyTemp> getLastYearTemprature(String cityId) {
            try {
                Thread.sleep(2000);
                return List.of(
                        new DailyTemp(new Date(), 10.0),
                        new DailyTemp(new Date(), 11.0),
                        new DailyTemp(new Date(), 12.0),
                        new DailyTemp(new Date(), 42.0),
                        new DailyTemp(new Date(), 14.0),
                        new DailyTemp(new Date(), 15.0),
                        new DailyTemp(new Date(), 16.0),
                        new DailyTemp(new Date(), 17.0)
                );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
