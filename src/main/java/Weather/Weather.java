/* by Tin Vo */

package Weather;
import org.springframework.data.annotation.Id;
import java.util.*;

public class Weather {

	@Id
	private String id;
	private String city;
	private Hashtable<String, String> condition;

	public Weather(String city, Hashtable<String, String> condition) {
		this.city = city;
		this.condition = condition;
	}

	/* Return the condition */
	public Hashtable<String, String> getCondition() {
		return condition;
	}

	/* Return the city name */
	public String getCity() {
		return city;
	}
}