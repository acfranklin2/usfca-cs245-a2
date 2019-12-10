import java.util.List;
import java.util.ArrayList;

public class Actor
{
	String name;
	List<Movie> career;
	List<Actor> costars;

	public Actor()
	{
		name = null;
		career = new ArrayList<Movie>();
		costars = new ArrayList<Actor>();
	}

	public Actor(String aName)
	{
		name = aName;
		career = new ArrayList<Movie>();
		costars = new ArrayList<Actor>();
	}

}