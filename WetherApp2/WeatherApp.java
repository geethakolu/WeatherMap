import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherApp {

    private static final String API_KEY = "cf917c4d41139335dac70fdf1df95fd4"; // Replace with your actual API key

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("===== Weather Map Application =====");
            System.out.println("1. Get Weather by City");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                System.out.print("Enter city name: ");
                String city = scanner.nextLine();
                JSONObject weatherData = getWeather(city);
                if (weatherData != null) {
                    viewWeather(weatherData);
                } else {
                    System.out.println("Could not fetch weather data. Please check the city name.");
                }
            } else if (choice == 2) {
                System.out.println("Exiting application...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    public static JSONObject getWeather(String city) {
        try {
            String urlString = String.format(
                "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                city, API_KEY
            );
            URL url = URI.create(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());

        } catch (Exception e) {
            return null;
        }
    }

    public static void viewWeather(JSONObject data) {
        String city = data.getString("name");
        JSONObject main = data.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        String description = data.getJSONArray("weather")
                                 .getJSONObject(0)
                                 .getString("description");

        System.out.println("City: " + city);
        System.out.println("Temperature: " + temp + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Description: " + description);
    }
}
