package reasoner.graph;

import java.util.*;

import org.semanticweb.owlapi.model.*;

public class CompletionGragh{
	private int totalNodes=0;
	
	 public Node addNode(Node.NodeType nodeType, OWLClassExpression nodeLabel) {
		 Node node = new Node(nodeType,nodeLabel);
		 totalNodes++;
		 return node;
	 }
	
	 public Edge addEdge(Node from, Node to, OWLObjectPropertyExpression edgeLabel) {
		Edge edge = new Edge(from, to, edgeLabel);
		Edge invEdge = new Edge(to, from, edgeLabel.getInverseProperty());
		edge.getFromNode().getEdges().add(edge);
		edge.getFromNode().getOutgoingEdges().add(edge);
		edge.getFromNode().getIncomingEdges().add(invEdge);
		edge.getToNode().getIncomingEdges().add(edge);
		edge.getToNode().getOutgoingEdges().add(invEdge);
		return edge;
	 }
	 
	 public Edge getEdge(Node from, OWLClassExpression nodeLabel, OWLObjectPropertyExpression edgeLabel) {
		 for(Edge e : from.getOutgoingEdges()) {
			 if(e.getLabel().contains(edgeLabel)) {
				 if(e.getToNode().getLabel().contains(nodeLabel))
					 return e;
			 }
		 }
		 return null;
	 }
	 public Set<Edge> getEdge(Node from, OWLObjectPropertyExpression edgeLabel) {
		 Set<Edge> edges = new HashSet<>();
		 from.getOutgoingEdges().stream().filter(e -> e.getLabel().contains(edgeLabel)).forEach(e -> edges.add(e));
		 return edges;
	 }

	public int getTotalNodes() {
		return totalNodes;
	}

	public void setTotalNodes(int totalNodes) {
		this.totalNodes = totalNodes;
	}
	 
}
