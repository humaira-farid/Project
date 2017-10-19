package reasoner.graph;

import java.util.*;

import org.semanticweb.owlapi.model.*;

public class Edge {
	
	 private Node node1;

	    private Node node2;

	    private Set<OWLObjectPropertyExpression> edgeLabel = new HashSet<OWLObjectPropertyExpression>();;

	    public Edge(Node node1, Node node2, OWLObjectPropertyExpression edgeLabel) {
	        this.node1 = node1;
	        this.node2 = node2;
	        this.edgeLabel.add(edgeLabel);
	    }

	    public void addLabel(OWLObjectPropertyExpression edgeLabel) {
	    	this.edgeLabel.add(edgeLabel);
	    }
	    public Node getFromNode() {
	        return node1;
	    }

	    public Node getToNode() {
	        return node2;
	    }
 
	    public boolean isBetween(Node node1, Node node2) {
	        return (this.node1 == node1 && this.node2 == node2);
	    }
	    
	    public Set<OWLObjectPropertyExpression> getLabel() {
	    	return this.edgeLabel;
	    }
}
