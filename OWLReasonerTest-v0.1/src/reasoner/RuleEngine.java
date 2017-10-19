package reasoner;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.model.*;

import reasoner.graph.*;
import reasoner.todolist.ToDoList;

public class RuleEngine {
	Internalization intl;
	CompletionGragh cg;
	ToDoList todo;
	
	public RuleEngine(Internalization i, CompletionGragh g, ToDoList todo) {
		this.intl= i;
		this.cg = g;
		this.todo = todo;
	}

	public void applyExistentialRule(Node from, OWLObjectSomeValuesFrom objSV) {
		System.out.println("Applying Exixts Rule");
		OWLObjectPropertyExpression role = objSV.getProperty();
		OWLClassExpression filler = objSV.getFiller();
		//Missing functionality: check node 'from' is not blocked 
		Edge e = this.cg.getEdge(from, filler, role);
		if(e == null) {
			Node to =this.cg.addNode(null, filler);
			//Missing functionality: update todo list
			if(filler instanceof OWLClass) 
				absorbRule1(filler, to);
			else if(filler instanceof OWLObjectIntersectionOf)
				todo.addEntry(to, NodeTag.AND, filler);
			else if(filler instanceof OWLObjectUnionOf)
				todo.addEntry(to, NodeTag.OR, filler);
			else if(filler instanceof OWLObjectSomeValuesFrom)
				todo.addEntry(to, NodeTag.EXISTS, filler);
			else if(filler instanceof OWLObjectAllValuesFrom)
				todo.addEntry(to, NodeTag.FORALL, filler);
			this.cg.addEdge(from, to, role);
		}
	}
	
	public void applyForAllRule(Node from, OWLObjectAllValuesFrom objAV) {
		System.out.println("Applying For All Rule");
		OWLObjectPropertyExpression role = objAV.getProperty();
		OWLClassExpression filler = objAV.getFiller();
		
		Set<Edge> edges = this.cg.getEdge(from, role);
		if(edges.size()!=0) {
			for(Edge e : edges) {
				Node n = e.getToNode();
				n.addLabel(filler);
				
				if(filler instanceof OWLClass) 
					absorbRule1(filler, n);
				else if(filler instanceof OWLObjectIntersectionOf)
					todo.addEntry(n, NodeTag.AND, filler);
				else if(filler instanceof OWLObjectUnionOf)
					todo.addEntry(n, NodeTag.OR, filler);
				else if(filler instanceof OWLObjectSomeValuesFrom)
					todo.addEntry(n, NodeTag.EXISTS, filler);
				else if(filler instanceof OWLObjectAllValuesFrom)
					todo.addEntry(n, NodeTag.FORALL, filler);
					
			}
		}
	}
	
	public void applyAndRule(Node n, OWLObjectIntersectionOf objIn) {
		System.out.println("Applying And Rule size: "+ objIn.asConjunctSet().size());
		for(OWLClassExpression ce : objIn.asConjunctSet()) {
			n.addLabel(ce);
			if(ce instanceof OWLClass) 
				absorbRule1(ce, n);
			else if(ce instanceof OWLObjectIntersectionOf)
				todo.addEntry(n, NodeTag.AND, ce);
			else if(ce instanceof OWLObjectUnionOf)
				todo.addEntry(n, NodeTag.OR, ce);
			else if(ce instanceof OWLObjectSomeValuesFrom)
				todo.addEntry(n, NodeTag.EXISTS, ce);
			else if(ce instanceof OWLObjectAllValuesFrom)
				todo.addEntry(n, NodeTag.FORALL, ce);
		}
	}
	
	public void applyOrRule(Node n, OWLObjectUnionOf objUn) {
		System.out.println("Applying or Rule");
		for(OWLClassExpression ce : objUn.asDisjunctSet()) {
			n.addLabel(ce);
			if(ce instanceof OWLClass) { 
				absorbRule1(ce, n); return;
			}
			else if(ce instanceof OWLObjectIntersectionOf) {
				todo.addEntry(n, NodeTag.AND, ce); return;
			}
			else if(ce instanceof OWLObjectUnionOf) {
				todo.addEntry(n, NodeTag.OR, ce); return;
			}
			else if(ce instanceof OWLObjectSomeValuesFrom) {
				todo.addEntry(n, NodeTag.EXISTS, ce); return;
			}
			else if(ce instanceof OWLObjectAllValuesFrom) {
				todo.addEntry(n, NodeTag.FORALL, ce); return;
			}
			return;
		}
	}
	
	public void applyNominalRule() {
		
	}
	
	public boolean checkClash(Node n, OWLClass c) {
		if(n.getLabel().contains(c.getComplementNNF())) {
			n.addLabel(OWLFunctionalSyntaxFactory.OWLNothing());
			return true;
		}
		return false;
	}
	
	public void absorbRule1(OWLClassExpression ce, Node n) {
		
		System.out.println("Applying absorb Rule1");
		Set<OWLClassExpression> sup = this.intl.findConcept(ce);
		if(sup.size()!=0) {
			for(OWLClassExpression c : sup) {
				n.addLabel(c);
				
				if(c instanceof OWLClass) 
					absorbRule1(c, n);
				else if(c instanceof OWLObjectIntersectionOf)
					todo.addEntry(n, NodeTag.AND, c);
				else if(c instanceof OWLObjectUnionOf)
					todo.addEntry(n, NodeTag.OR, c);
				else if(c instanceof OWLObjectSomeValuesFrom)
					todo.addEntry(n, NodeTag.EXISTS, c);
				else if(c instanceof OWLObjectAllValuesFrom)
					todo.addEntry(n, NodeTag.FORALL, c);
			}
		}
	}
	public void absorbRule2() {
		
	}
	public boolean findClash() {
		return false;
	}
}
