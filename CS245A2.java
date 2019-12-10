/**
 * Assignment 2 - Six Degrees of Kevin Bacon
 * @author acfranklin2 (Alex Franklin, CS245-02)
 */ 
import java.util.*;
import java.io.*;

//In layman's terms, CS245A2 makes a giant, interconnected graph consisting of every actor
//and then finding the shortest paths between any two of them. 
public class CS245A2 {

	private List<Actor> actors;
	private List<Movie> movies;
	private List<String> actorNames;
	private List<ActorNode> actorNodes;

	public CS245A2() {
		actors = new ArrayList<Actor>();
		movies = new ArrayList<Movie>();
		actorNames = new ArrayList<String>();
		actorNodes = new ArrayList<ActorNode>();
	}

	/**This function goes through the CSV file necessary and places the necessary information in either the actors or movies
	 * variable.
	 */
	public void createLists(String arg0)
	{
		//First, it calls on the CSV file
		try{
			BufferedReader file = new BufferedReader(new FileReader(arg0));
			String[] mainInfo; String[] allNames; String line = ""; String[] details = giveDetails();
			//Because this is working with just the actors and not the individual crew members, it will only take in those 
			//and trim off the rest
			System.out.println(details[0]);
			try{
				while((line = file.readLine()) != null) {
					mainInfo = line.split("]");
					allNames = mainInfo[0].split(",");
					//Creates the movie that will later be added to the Movie class.
					Movie mov = new Movie(allNames[1]);
					for(String s: allNames)
						if(s.startsWith(" \"\"name\"\"")) {
							s = s.substring(13, s.length() - 2);
							//It first checks to see if the actor being checked has yet to be in the Actor list.
							if(!(actorNames.contains(s))) {
								//If it isn't, it adds the actor's name to ActorNames, the Actor themselves to the Actor list, and the ActorNode to, well, the ActorNodes list.
								actorNames.add(s);
								actors.add(new Actor(s));
								actorNodes.add(new ActorNode(actors.get(actors.size() - 1)));
								//Because it's a new actor, the placement will obviously be the last one in the array
								actors.get(actors.size() - 1).career.add(mov);
							}
							//If not, it goes through another process where it adds the movie to the determined actor
							else
								actors.get(actorIndex(s)).career.add(mov);
							//Regardless of whether or not the actor is new or already processed, they're still added to the stars list of the movie being processed.
							mov.stars.add(actors.get(actorIndex(s)));

						}
					movies.add(mov);
				}
			}
			catch(Exception e){e.printStackTrace();};
			System.out.println();
			for(Actor star: actors) {
				assignCostars(star);
			}
			System.out.println(details[1]);
			constructNodeBranches();
		}
		catch(FileNotFoundException fe){System.out.println("This didn't work.");};
	}

	/**
	 * Takes an actor and goes through their career, selecting every other actor from their movies and putting it in their costar list.
	 * @param star The actor that is getting their costars assigned to them.  
	 */

	public void assignCostars(Actor star) {
		List<Actor> fellowActors = new ArrayList<Actor>();
		for(Movie m: star.career)
			fellowActors.addAll(m.stars);
		//Then, it removes all known duplicates of an Actor in this ArrayList
		fellowActors = new ArrayList<Actor>(new LinkedHashSet<Actor>(fellowActors));
		//After that, it removes the actor themselves from the costars list
		fellowActors.remove(fellowActors.indexOf(star));
		star.costars.addAll(fellowActors);
		//Now, it adds the appropriate actors to the ActorMatrix
	}

	/**This function constructs the Adjacency List by going from each Node in the ActorNodes list and then connecting it to every Node
	 * where the Actor in said Node is a costar of the actor in the main Node.
	 */
	public void constructNodeBranches()
	{
		//Goes through each of the ActorNodes and assigns the appropriate ActorNodes to their branches (based on costars).
		for(ActorNode node: actorNodes) {
			for(Actor costar: node.actor.costars) 
				node.branches.add(nodeFor(costar));
		}
	}


	/**
	 * This formula finds the index for an Actor with the name specified by the parameter.
	 * @param name The name of the actor we want to find.
	 * @return the integer for the index of the targeted Actor in the actors list.
	 */ 
	public int actorIndex(String name) {
		for(Actor a: actors)
			if(a.name.equals(name))
				return(actors.indexOf(a));
		//In the (though unlikely) case that it somehow doesn't find the actor with the specified name, it returns a different exception value.
		return(-1);
	}

	/** This is a varation of the previous function where:
	 * @param actor The paramater is an Actor instead of a String.
	 * @return the integer for the index of the targeted Actor in the actors list.
	 */
	public int actorIndex(Actor actor) {
		if(actors.contains(actor))
			return(actors.indexOf(actor));
		//In the (though unlikely) case that it somehow doesn't find the actor with the specified name, it returns a different exception value.
		return(-1);
	}

	/**
	 * This function finds the ActorNode within the ActorNodes list for the inputted Actor.
	 * @param actr The inputted Actor.
	 * @return The ActorNode corresponding to the Actor. 
	 */
	public ActorNode nodeFor(Actor actr) {
		for(ActorNode node: actorNodes)
			if(node.actor == actr)
				return(node);
		return(null);
	}



	/**
	* Because only doing one comparison for each usage of this file would be heavily time-consuming, due to the amount of time it
	* takes to put everything from the CSV file in its proper place, the main event takes place in a while loop that will continue
	* forever until the user decides to end it.
	*/
	public void compareActorLoop() {
		System.out.println("Oh, hey, it finally made it through! Thank you so much for your patience! Now... let's begin!\n");
		boolean inLoop = true;
		String name1; String name2;
		while(inLoop) {
			System.out.println("\nActor 1 Name: ");
			name1 = inputName();
			System.out.println("Actor 2 Name: ");
			name2 = inputName();

			List<Actor> shortestPath = shortestConnection(actors.get(actorIndex(name1)), actors.get(actorIndex(name2)));
			printConnection(shortestPath);
			System.out.println("\nWould you like to go again? If not, answer 'No', and if you do, enter anything else.");
			Scanner input = new Scanner(System.in);
			if(input.next().equals("No")) {
				inLoop = false;
			}
		}
		System.out.println("Bye!");
	}

	/**
	 * This function asks the user to enter an actor's name, and if that actor's in the program, it will 
	 * return that name.
	 * @return The name of the actor that the user wants to implement.
	 */
	public String inputName() {
		Scanner input = new Scanner(System.in);
		String aName = input.nextLine();
		aName = capitalsTest(aName);
		//This is a test to see if a name can be equated to another actor's name even if the capitalization is different.
		while(!actorNames.contains(aName)) {
			System.out.println(aName);
			System.out.println("I can't find that actor in this database. Please input another.");
			aName = input.nextLine();
			aName = capitalsTest(aName);
		}
		return(aName);
	}

	public String capitalsTest(String input) {
		String inputLC = input.toLowerCase();
		for(String actorName: actorNames){
			String nameLC = actorName.toLowerCase();
			if(inputLC.equals(nameLC))
				return(actorName);
		}
		return(null);
	}

	/**

	 * This important function takes two actors and, depending on whether or not one of them is within the costar list of the other,
	 * will either immediately return an ArrayList containing just those two actors, or proceed to do a Breadth First Search of the
	 * ActorNodes list. 
	 * @param a1 Actor 1.
	 * @param a2 Actor 2.
	 * @return an ArrayList of the shortest path between those two actors. 
	 */
	public List<Actor> shortestConnection(Actor a1, Actor a2)
	{
		List<Actor> connection = new ArrayList<Actor>();
		//If Actors 1 and 2 are in the same movie, they'll be in each others' costars lists
		if(a1.costars.contains(a2)){
			connection.add(a1);
			connection.add(a2);
			return(connection);
		}
		//Should Actors 1 and 2 NOT be in the same movie, we use Breadth-First-Search on the AdjacencyList
		// that we created.
		else{
			
			//This is BREADTH-FIRST-SEARCH, which is designed to always check each layer outward from the desired root of a Graph, 
			//search tree, etc. 
			ArrayDeque<ActorNode> actorQueue = new ArrayDeque<ActorNode>(); 
			boolean[] visited = new boolean[actors.size()];
			actorQueue.add(nodeFor(a1)); //It does it twice because otherwise, when the queue forms, it will just delete itself.
			visited[actorIndex(actors.get(1))] = true;
			while(!actorQueue.isEmpty()){
				ActorNode tempNode = actorQueue.remove();
				connection.add(tempNode.actor);
				for(ActorNode aNode: tempNode.branches)
					if(visited[actorIndex(aNode.actor)] != true) {
						if(aNode.actor.costars.contains(a2)){
							connection.add(aNode.actor);
							connection.add(a2);
							return(connection);
						}
						actorQueue.add(aNode);
						visited[actorIndex(aNode.actor)] = true;
					}
			}
		}
		//Just in case no connection was found between two actors.
		System.out.println("No connection was found between these two actors.");
		return(connection);
	}

	/**
	 * This function prints out the shortest path between the two previously inputted actors. 
	 * @param connection The ArrayList containing said shortest path.
	 */
	public void printConnection(List<Actor> connection)
	{
		Actor startOfList = connection.get(0);
		Actor endOfList = connection.get(connection.size() - 1);
		System.out.print("Path between " + startOfList.name + " and " + endOfList.name + ": ");
		for(Actor actor: connection) {
			if(actor != endOfList)
				System.out.print(actor.name + " --> ");
			else
				System.out.print(actor.name);
		}
		System.out.println();
	}

	/** 
	 * This wall of text, split among two String variables in a String array is here so that as the program takes in everything from 
	 * the CSV file and builds out the AdjacencyList, the user isn't just sitting there for a while.
	 * @return That array of texts. 
	 */
	public String[] giveDetails(){
		String[] details = new String[2];
		details[0] = "Well, this is awkward. While I have managed to greatly better the efficiency of much" + 
		" of the program, one aspect that has eluded me is the actual creation of the ArrayLists, Nodes, etc. In fact, "+
		"as you read this text, the program is working hard in the background on taking in all the actors and movies from the"+
		" CSV file and putting them in their appropriate lists. It just takes a while because the file is so big. That's why I"+
		" have decided to put up these words for you to read, because I figured that'd at least make this wait not feel as long." +
		" Don't worry, I made it so that when the stuff is actually all loaded up, you can do Actor comparisons over and over again "
		+ "without having to load everything up again. At least until you close the program. Then you'll have to read this again. "
		+ "But this is better than just sitting there and looking at a thing saying, \"Initializing CSV File... This may take a while.\""+
		" I think we can both agree this makes it at least more varied than it would be otherwise.";
		details[1] = "\nI guess I'll use this time to tell you about the program, even though I imagine that whoever's reading this is aware"+
		" of the project and how this works. But it doesn't hurt to clarify the specifics of how I did it, I guess. Anyway...\n"+ "This program takes in the argument that you provided "+
		"in the command line, the CSV file containing thousands of movies and all the actors that starred in them, and will read that information"+
		" into several ArrayLists of different classes: Movie, Actor, ActorNode... The Movie and Actor classes in particular connect each other"+
		" together in the form of having ArrayLists of one class in the other - For Movie, it's the movie's stars, and for Actor, it's the actor's career."+
		" And don't even get me started on the ArrayList of costars an Actor has, which is basically every actor in every movie an Actor's starred in." + 
		" I've really tried to go in-depth with this, heh. After reading all this stuff and putting it together, it will form an AdjacencyList consisting of" +
		" ActorNodes that connect to each other much like the Actors connect to their costars. Once all this is said and done, you'll be able to "+
		"input the names of two actors, and the program will use everything it's set up and locate one of the, if not the shortest path through movies from"+
		" one to the other.\n\n Anyway, I don't know for sure if the program will have finished initializing everything by the time you read this part - Everyone"+
		" has different reading speeds after all. But thank you for your patience and taking the time to read this - I'd say at least then, you weren't bored just waiting several minutes"+
		" and wondering where the heck the program is. And even if I had a thing saying that it was in the process of loading, that'd still be a bit monotonous. Anyway, thank"+
		" you, and having fun finding the shortest path from anyone to Kevin Bacon (or whatever actor you wish)!" +
		" Now just sit tight as the rest of this gets initialized!";
		return(details);
	}

	public static void main(String[] args)
	{
		try{
			CS245A2 a2 = new CS245A2();
			a2.createLists(args[0]);
			a2.compareActorLoop();
		}
		catch(ArrayIndexOutOfBoundsException error){error.printStackTrace(); System.out.println("You need to launch this implementation with the necessary CSV file.");};
	}
}