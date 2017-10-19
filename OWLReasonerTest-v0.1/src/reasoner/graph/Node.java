package reasoner.graph;

import java.util.*;

import org.semanticweb.owlapi.model.*;

public class Node {
	
	private static int idcounter = 0;
	private NodeType nodeType;
	private Set<OWLClassExpression> nodeLabel = new HashSet<OWLClassExpression>();
	private final int id;
    private List<Edge> edges;
    private Set<Edge> incomingEdge = new HashSet<>();
    private Set<Edge> outgoingEdge = new HashSet<>();
    
    
    public Node(Node.NodeType nodeType, OWLClassExpression nodeLabel) {
        this.nodeType = nodeType;
        this.nodeLabel.add(nodeLabel);
        this.edges = new ArrayList<>();
        this.id = getnewId();
    }
    
    private static int getnewId() {
        return idcounter++;
    }
	
  /*  public void addEdge(Node node, OWLObjectPropertyExpression edgeLabel) {
        if (hasEdge(node)) {
        	Edge edge = findEdge(node).get();
        	edge.addLabel(edge, edgeLabel);
            return;
        }
        Edge newEdge = new Edge(this, node, edgeLabel);
        edges.add(newEdge);
    } */
    
    public Set<Edge> getIncomingEdges(){
    	 return this.incomingEdge;
    }
    public Set<Edge> getOutgoingEdges(){
   	 return this.outgoingEdge;
   }
    
    public boolean hasEdge(Node from, Node to) {
        return findEdge(from, to).isPresent();
    }
    
    private Optional<Edge> findEdge(Node from, Node to) {
        return edges.stream()
                .filter(edge -> edge.isBetween(from, to))
                .findFirst();
    }
	
    
    public void addLabel(OWLClassExpression OWLClassExpression) {
    	this.nodeLabel.add(OWLClassExpression);
    }
    
    public Set<OWLClassExpression> getLabel() {
    	return this.nodeLabel;
    }
    public List<Edge> getEdges() {
    	return this.edges;
    }
    
    public Set<Node> getNodeAncestors(Node node) {
        Set<Node> ans = new HashSet<Node>();
        for (Edge edge : node.getIncomingEdges()) {
            ans.add(edge.getFromNode());
        }
        return ans;
    }

    public int getNodeId() {
    		return this.id;
    }
    public static enum NodeType {
        ANONYMOUS,
        NOMINAL,
        PROXY;
        

        private NodeType() {
        }
    }

}
