/* by Tin Vo */

package Weather;
import java.util.*;
import java.io.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Weather.Weather;

@RestController
@RequestMapping("/weather")
public class WeatherController {

	private Hashtable<String, Object> weatherRepo = new Hashtable<String, Object>();

	/* Create initial db */
	public WeatherController() {
		try {
			File file = new File("src/main/java/Weather/result.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			String city = "";
			String[] tokens, items;
			String[] keys = {"Weather", "Temp", "Pressure", "Humidity", "Wind"};

			while((line = bufferedReader.readLine()) != null) {
				tokens = line.split("t@t");
				Hashtable<String, String> con = new Hashtable<String, String>();
				for(int i = 0; i < tokens.length; i++) {
					items = tokens[i].split(" ");
					if( i == 0) {
						city = items[1];
					} else {
						con.put(keys[i-1], items[1]);
					}
				}
				Weather weather = new Weather(city, con);
				weatherRepo.put(city, weather);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* POST */
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, Object> createBook(@RequestBody Map<String, Object> weatherMap) {
		/* Get the city and condition */
		String city = weatherMap.get("city").toString().toLowerCase();
		String weatherCon = weatherMap.get("weather").toString();
		String tempCon = weatherMap.get("temp").toString();
		String pressureCon = weatherMap.get("pressure").toString();
		String humidityCon = weatherMap.get("humidity").toString();
		String windCon = weatherMap.get("wind").toString();
		city = city.contains(" ") ? city.replaceAll(" ", "_") : city;
		city = city.contains("-") ? city.replaceAll("-", "_") : city;

		/* Create condition */
		Hashtable<String, String> con = new Hashtable<String, String>();
		con.put("Weather", weatherCon);
		con.put("Temp", tempCon + "°F");
		con.put("Pressure", pressureCon + "hpa");
		con.put("Humidity", humidityCon + "%");
		con.put("Wind", windCon + " m/s");

		/* Create Weather */
		Weather weather = new Weather(city, con);
		weatherRepo.put(city, weather);

		/* Response */
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("Message", "City & Weather Added Successfully!!!");
		response.put("Weather", weather);
		return response;
	}


	/* GET with city name */
	@RequestMapping(method = RequestMethod.GET, value="/{city}")
	public Object getWeather(@PathVariable("city") String city){
		
		/* Check on the city that the user entered */
	 	city = city.toLowerCase();
	 	city = city.contains(" ") ? city.replaceAll(" ", "_") : city;
	 	city = city.contains("-") ? city.replaceAll("-", "_") : city;

	 	/* Check for the key */
	 	if(weatherRepo.containsKey(city)) {
	 		return weatherRepo.get(city);
	 	}
     	return null;
	}


	/* GET ALL */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> getAllWeather() {
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("Total ", weatherRepo.size());
		response.put("Cites ", weatherRepo);
		return response;
	}


	/* DELETE */
	@RequestMapping(method = RequestMethod.DELETE, value="/{city}")
	public Map<String, String> deleteWeather(@PathVariable("city") String city){
		/* Check on the city that the user entered */
		city = city.toLowerCase();
		city = city.contains(" ") ? city.replaceAll(" ", "_") : city;
		city = city.contains("-") ? city.replaceAll("-", "_") : city;

		/* Remove from repo */
		weatherRepo.remove(city);

		/* Response */
		Map<String, String> response = new HashMap<String, String>();
		response.put("Message", city + " Deleted Successfully!!!");
		return response;
	}


	/* PUT */
	@RequestMapping(method = RequestMethod.PUT, value="/{city}")
	public Map<String, Object> updateWeather(@PathVariable("city") String city, @RequestBody Map<String, Object> weatherMap){
		/* Check on the city that the user entered */
		city = city.toLowerCase();
		String weatherCon = weatherMap.get("weather").toString();
		String tempCon = weatherMap.get("temp").toString();
		String pressureCon = weatherMap.get("pressure").toString();
		String humidityCon = weatherMap.get("humidity").toString();
		String windCon = weatherMap.get("wind").toString();
		city = city.contains(" ") ? city.replaceAll(" ", "_") : city;
		city = city.contains("-") ? city.replaceAll("-", "_") : city;

		/* Create condition */
		Hashtable<String, String> con = new Hashtable<String, String>();
		con.put("Weather", weatherCon);
		con.put("Temp", tempCon + "°F");
		con.put("Pressure", pressureCon + "hpa");
		con.put("Humidity", humidityCon + "%");
		con.put("Wind", windCon + " m/s");

		/* Create Weather and Update */
		Weather weather = new Weather(city, con);
		weatherRepo.put(city, weather);

		/* Response */
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("Message", "Weather Updated Successfully");
		response.put("Weather", weather);
		return response;
	}
}