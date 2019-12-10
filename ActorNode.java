import java.util.*;

public class ActorNode
{
	Actor actor;
	List<ActorNode> branches;

	public ActorNode(Actor a){
		actor = a;
		branches = new ArrayList<ActorNode>();
	}

}