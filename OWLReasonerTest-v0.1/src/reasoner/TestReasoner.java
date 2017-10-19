package reasoner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;

import reasoner.graph.CompletionGragh;
import reasoner.todolist.ToDoEntry;
import reasoner.todolist.ToDoList;

public class TestReasoner{
	CompletionGragh cg; 
	ToDoList todo; 
	Internalization intr;
	public TestReasoner() {
		cg = new CompletionGragh();
		todo = new ToDoList();
		intr = new Internalization();
	}
	public void useReasoner() throws OWLOntologyCreationException {
		 OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		 File file = new File("/Users/temp/Desktop/PhD/PhD Research/OWL-API/testOnt.owl");
		 OWLOntology ont = man.loadOntologyFromOntologyDocument(file);
		 OWLDataFactory df = man.getOWLDataFactory();
	     ReasonerFactory reasonerFactory = new ReasonerFactory();
	     
		    
	     Reasoner reasoner = reasonerFactory.createReasoner(ont);
	     reasoner.preprocess();
	     // Ask the reasoner to do all the necessary work now
	      reasoner.precomputeInferences();
	   //  Extractor ex = new Extractor();
	    
	    // String s = "ObjectIntersectionOf";
	     //intr.parseClassExpression(ont, s);
	     intr.internalize(ont);
	     OWLClassExpression tgAxiom = intr.getTgAxiom(df);
	     this.createFirstNode(tgAxiom);
	     while(!todo.isEmpty()) {
	    	 System.out.println("while loop");
	    	 	ToDoEntry entry = todo.getNextEntry();
	    	 	System.out.println("expression type: "+ entry.getClassExpression() + " tag: "+ entry.getType());
	    	 	if(entry!=null)
	    	 		this.applyRules(entry);
	     }
	     System.out.println("No. of nodes : "+ cg.getTotalNodes());
	    
	     
	    // intr.getTg();
	     
	 	
	 	
	  //for (OWLSubClassOfAxiom sbg : intr.getTg()) 
		//    	 	System.out.println("TG: Subclass"+sbg.getSubClass() + " , SuperClass" + sbg.getSuperClass());
	    	 	
	    
	    
	  // for (OWLSubClassOfAxiom sbg : intr.getTu()) 
	   // 	 	System.out.println("TG: Subclass"+sbg.getSubClass() + " , SuperClass" + sbg.getSuperClass());
    	 	
	 //   System.out.println( intr.getTgAxiom(ont));
	   //  for(Disjunct dsj : createDisjuncts(ex.getSuperclasses(ont),ex.getEquivalentclasses(ont),ex.getDisjointclasses(ont)))
	    	 //	System.out.println(dsj.getLHS() + " or " + dsj.getRHS());
	 
	     
	     /*
	   /////////// superclasses //////////////  
	     Set<OWLClass> classes = ont.getClassesInSignature();
	     Set<OWLSubClassOfAxiom> sp;
	     Map<String,Set<String>> superClasses = new HashMap<String,Set<String>>();
	     for (OWLClass c : classes ) {
	    	 	sp = ont.getSubClassAxiomsForSubClass(c);
	    	 	String[] cParts = c.toString().split("#", 2);
	    	 	String[] cf = cParts[1].split(">");
    	 		
    	 		
	    	 	Set<String> sup = new HashSet<String>();
	    	 	if(!sp.isEmpty()) {
		    	 	for (OWLSubClassOfAxiom subAx : sp) {
		    	 		
		    	 	//	existential(subAx);
		    	 	System.out.println(subAx.getSuperClass());
		    	 		String[] parts = subAx.toString().split(" ", 2);
		    	 		String[] parts2 = parts[1].split("#", 2);
		    	 		String[] parts3 = parts2[1].split(">");
		    	 		String s = parts3[0];
		    	 		//System.out.println(s);
		    	 		sup.add(s);
		    	 	}
	    	 	}
	    	 	superClasses.put(cf[0], sup);
	     }
	     ///// end super classes
	     
	     ///////////// subclasses //////////////
	     
	     Set<OWLSubClassOfAxiom> sb;
	     Map<String,Set<String>> subClasses = new HashMap<String,Set<String>>();
	     for (OWLClass c : classes ) {
	    	 	sb = ont.getSubClassAxiomsForSuperClass(c);
	    	 	String[] cParts = c.toString().split("#", 2);
	    	 	String[] cf = cParts[1].split(">");
    	 		
    	 		
	    	 	Set<String> sub = new HashSet<String>();
	    	 	if(!sb.isEmpty()) {
		    	 	for (OWLSubClassOfAxiom subAx : sb) {
		    	 	//	existential(subAx);
		    	 	//	System.out.println(subAx.toString());
		    	 		String[] parts = subAx.toString().split("> <", 2);
		    	 		String[] parts2 = parts[0].split("#", 2);
		    	 		String[] parts3 = parts2[1].split(">");
		    	 		String s = parts3[0];
		    	 	//	System.out.println(s);
		    	 		sub.add(s);
		    	 	}
	    	 	}
	    	 	subClasses.put(cf[0], sub);
	     }
	     ///// end sublasses 
	     */
	  // printDisjointClasses(ex.getDisjointclasses(ont));
	   // printSubClasses(ex.getSubclasses(ont));
	    // printEquivalentClasses(ex.getEquivalentclasses(ont));
	  /*   List<OWLAxiom> list = reasoner.importsIncluded(ont);
	     for (OWLAxiom ax : list)
	    	 	System.out.println(ax);
	  */
	   
	     boolean consistent = reasoner.isConsistent();
	     System.out.println("Consistent: " + consistent);
	     System.out.println("\n");
	     
	     Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
	     Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
	        if (!unsatisfiable.isEmpty()) {
	            System.out.println("The following classes are unsatisfiable: ");
	            for (OWLClass cls : unsatisfiable) {
	                System.out.println("    " + cls);
	            }
	        } else {
	            System.out.println("There are no unsatisfiable classes");
	        }
	        System.out.println("\n");
	        
	        
	        
	        man.removeOntology(ont);
	}
	
	public void createFirstNode(OWLClassExpression tgAxiom) {
		
		reasoner.graph.Node from = cg.addNode(null, tgAxiom);
		todo.addEntry(from, NodeTag.AND, tgAxiom);
	}
	public void applyRules(ToDoEntry entry) {
		System.out.println("todo size: "+ todo.entries());
		RuleEngine re = new RuleEngine(intr, cg, todo);
		reasoner.graph.Node n = entry.getNode();
		NodeTag nt = entry.getType();
		System.out.println("node tag: " + nt);
		switch(nt) {
		case AND:
			re.applyAndRule(n, (OWLObjectIntersectionOf)entry.getClassExpression());
			break;
		case OR:
			re.applyOrRule(n, (OWLObjectUnionOf)entry.getClassExpression());
			break;
		case EXISTS:
			re.applyExistentialRule(n, (OWLObjectSomeValuesFrom)entry.getClassExpression());
			break;
		case FORALL:
			re.applyForAllRule(n, (OWLObjectAllValuesFrom)entry.getClassExpression());
			break;
		default:
			break;
		}
		
	}
	 public void printSuperClasses(Map<OWLClassExpression,Set<OWLClassExpression>> sup) {
		 
		 for(OWLClassExpression c : sup.keySet()) {
			 System.out.println("Class " + c.toString() + " has superclasses :" + sup.get(c).toString());
		 }
	 }
	 public void printSubClasses(Map<OWLClassExpression,Set<OWLClassExpression>> sub) {
		 
		 for(OWLClassExpression c : sub.keySet()) {
			 System.out.println("Class " + c + " has subclasses :" + sub.get(c));
		 }
	 }
	 public void printEquivalentClasses(Map<OWLClassExpression,Set<OWLClassExpression>> eq) {
		 
		 for(OWLClassExpression c : eq.keySet()) {
			 System.out.println("Class " + c + " has Equivalent classes :" + eq.get(c));
		 }
	 }
	 public void printDisjointClasses(Map<OWLClassExpression,Set<OWLClassExpression>> dj) {
		 
		 for(OWLClassExpression c : dj.keySet()) {
			 System.out.println("Class " + c + " has Disjoint classes :" + dj.get(c));
		 }
	 }
	 public static Set<IRI> getIRIs(File file, String prefix) throws FileNotFoundException {
		    Set<IRI> iris = new HashSet<IRI>();
		    Scanner scanner = new Scanner(file);
		    while (scanner.hasNextLine()) {
		      String line = scanner.nextLine().trim();
		      if(!line.startsWith(prefix + "http")) { continue; }
		      String suffix = line.substring(prefix.length());
		      String iri = suffix.substring(0, Math.min(suffix.length(), suffix.indexOf(" ")));
		      System.out.println("<"+ iri +">");
		      iris.add(IRI.create(iri));
		    }
		    return iris;
		  }
	 
	 public void existential(OWLSubClassOfAxiom subAx) {
		 
		 if((subAx.getSuperClass() instanceof OWLObjectSomeValuesFrom)) {
			 System.out.println(((OWLObjectSomeValuesFrom)subAx.getSuperClass()).getProperty()+" . " +((OWLObjectSomeValuesFrom)subAx.getSuperClass()).getFiller());
		 }
		 if((subAx.getSuperClass() instanceof OWLClass)) {
			 System.out.println(((OWLObjectSomeValuesFrom)subAx.getSuperClass()).getComplementNNF()+" . " +((OWLObjectSomeValuesFrom)subAx.getSuperClass()).getFiller());
		 } 
	 }
	 public Set<Disjunct> createDisjuncts(Map<OWLClassExpression ,Set<OWLClassExpression>> sp, Map<OWLClassExpression,Set<OWLClassExpression>> eq, Map<OWLClassExpression,Set<OWLClassExpression>> dj) {
			Set<Disjunct> disjuncts = new HashSet<Disjunct>();
			for(OWLClassExpression l : sp.keySet()) {
				for(OWLClassExpression r : sp.get(l)) {
					Disjunct disj = new Disjunct(l.getComplementNNF(),r);
					disjuncts.add(disj);
				}
			 }
			for(OWLClassExpression l : eq.keySet()) {
				for(OWLClassExpression r : eq.get(l)) {
					Disjunct disj = new Disjunct(l.getComplementNNF(),r);
					Disjunct disj2 = new Disjunct(r.getComplementNNF(),l);
					disjuncts.add(disj);
					disjuncts.add(disj2);
				}
			 }
			for(OWLClassExpression l : dj.keySet()) {
				for(OWLClassExpression r : dj.get(l)) {
					Disjunct disj = new Disjunct(l.getComplementNNF(),r.getComplementNNF());
					disjuncts.add(disj);
				}
			 }
			return disjuncts;
		}
}
