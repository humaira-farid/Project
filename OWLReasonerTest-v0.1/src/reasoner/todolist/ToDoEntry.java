package reasoner.todolist;

import org.semanticweb.owlapi.model.*;

import reasoner.NodeTag;
import reasoner.graph.Node;

public class ToDoEntry {
	
	private final Node node;
	private final OWLClassExpression ce;
	private final NodeTag type;
	
	public ToDoEntry(Node node, OWLClassExpression ce, NodeTag type) {
		this.node = node;
		this.ce = ce;
		this.type = type;
		
	}

	public Node getNode() {
		return node;
	}

	public OWLClassExpression getClassExpression() {
		return ce;
	}

	public NodeTag getType() {
		return type;
	}
}
