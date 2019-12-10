import java.util.List;
import java.util.ArrayList;

public class Movie {
	String title;
	List<Actor> stars;

	public Movie() {
		title = null;
		stars = new ArrayList<Actor>();
	}

	public Movie(String name)
	{
		title = name;
		stars = new ArrayList<Actor>();
	}

}