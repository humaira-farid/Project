package reasoner;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.*;

public class Internalization {

	private Set<OWLSubClassOfAxiom> Tu;
	private Set<OWLSubClassOfAxiom> Tui;
	private Set<OWLSubClassOfAxiom> Tg;
	
	
	public Internalization(){
		this.Tu = new HashSet<OWLSubClassOfAxiom>();
		this.Tui = new HashSet<OWLSubClassOfAxiom>();
		this.Tg = new HashSet<OWLSubClassOfAxiom>();
	}
	public void internalize(OWLOntology ont) {
		
		Set<OWLSubClassOfAxiom> subAx = new HashSet<>();
	    Set<OWLEquivalentClassesAxiom> eqAx = new HashSet<>();
	    Set<OWLDisjointClassesAxiom> djAx = new HashSet<>();
	    Set<OWLDisjointUnionAxiom> djuAx = new HashSet<>();
	    Set<OWLObjectPropertyDomainAxiom> objdAx = new HashSet<>();
	    Set<OWLObjectPropertyRangeAxiom> objrAx = new HashSet<>();
	    for (OWLAxiom ax : (Iterable<OWLAxiom>)ont.axioms()::iterator) {
	    		if(ax instanceof OWLSubClassOfAxiom) {
	    			subAx.add((OWLSubClassOfAxiom) ax);
	    			if(((OWLSubClassOfAxiom) ax).getSubClass() instanceof OWLClass)
		    			this.Tu.add((OWLSubClassOfAxiom) ax);
	    			else if(((OWLSubClassOfAxiom)ax).getSubClass() instanceof OWLObjectIntersectionOf ) {
	    				if(isSimpleObjectIntersectionOf(((OWLObjectIntersectionOf)((OWLSubClassOfAxiom)ax).getSubClass()).asConjunctSet()))
	    					this.Tui.add((OWLSubClassOfAxiom) ax);
	    			}
	    			else
		    			this.Tg.add((OWLSubClassOfAxiom) ax);
	    		}
	    		else if(ax instanceof OWLEquivalentClassesAxiom)
	    			eqAx.add((OWLEquivalentClassesAxiom) ax);
	    		else if(ax instanceof OWLDisjointClassesAxiom) {
	    			djAx.add((OWLDisjointClassesAxiom) ax);
	    			for(OWLSubClassOfAxiom djsb : ((OWLDisjointClassesAxiom) ax).asOWLSubClassOfAxioms()) {
	    				if(djsb.getSubClass() instanceof OWLClass)
			    			this.Tu.add(djsb);
			    		else
			    			this.Tg.add(djsb);
	    			}
	    		}
	    		else if(ax instanceof OWLDisjointUnionAxiom) {
	    			djuAx.add((OWLDisjointUnionAxiom) ax);
	    			for(OWLSubClassOfAxiom djusb : ((OWLDisjointUnionAxiom) ax).getOWLDisjointClassesAxiom().asOWLSubClassOfAxioms()) {
	    				if(djusb.getSubClass() instanceof OWLClass)
			    			this.Tu.add(djusb);
			    		else
			    			this.Tg.add(djusb);
	    			}
	    		}
	    		else if(ax instanceof OWLObjectPropertyDomainAxiom) {
	    			objdAx.add((OWLObjectPropertyDomainAxiom) ax);
		    		this.Tg.add(((OWLObjectPropertyDomainAxiom) ax).asOWLSubClassOfAxiom());
	    		}
	    		else if(ax instanceof OWLObjectPropertyRangeAxiom) {
	    			objrAx.add((OWLObjectPropertyRangeAxiom) ax);
	    			this.Tg.add(((OWLObjectPropertyRangeAxiom) ax).asOWLSubClassOfAxiom());
	    		}
	    }
	    Set<OWLSubClassOfAxiom> newSbAx = new HashSet<OWLSubClassOfAxiom>();
	    
	    for(OWLEquivalentClassesAxiom eq : eqAx) {
	    		for(OWLSubClassOfAxiom sb : this.Tu) {
	    			if(eq.contains(sb.getSubClass()))
	    				for(OWLSubClassOfAxiom eqsb : eq.asOWLSubClassOfAxioms()) {
	    					if(eqsb.getSubClass() instanceof OWLClass) 
	    						newSbAx.add(eqsb);
	    					else
	    						this.Tg.add(eqsb);
	    			}
	    		}
	    }
	    for(OWLSubClassOfAxiom nsb : newSbAx)
	    		this.Tu.add(nsb);
	    	
	    
	    
	 //   ont.axioms().filter(ax -> ax instanceof OWLSubClassOfAxiom).forEach(ax -> subAx.add((OWLSubClassOfAxiom) ax));
	 //   ont.axioms().filter(ax -> ax instanceof OWLEquivalentClassesAxiom).forEach(ax -> eqAx.add((OWLEquivalentClassesAxiom) ax));
	 //   ont.axioms().filter(ax -> ax instanceof OWLDisjointClassesAxiom).forEach(ax -> djAx.add((OWLDisjointClassesAxiom) ax));
	 //   ont.axioms().filter(ax -> ax instanceof OWLDisjointUnionAxiom).forEach(ax -> djuAx.add((OWLDisjointUnionAxiom) ax));
	     
	}
	
	public boolean isSimpleObjectIntersectionOf(Set<OWLClassExpression> ceSet) {
		for(OWLClassExpression ce : ceSet) {
			if(!(ce instanceof OWLClass))
				return false;
		}
		return true;
	}
		
	public Set<OWLSubClassOfAxiom>  getTu() {
		return this.Tu;
	}
	public Set<OWLSubClassOfAxiom>  getTui() {
		return this.Tui;
	}
	public Set<OWLSubClassOfAxiom>  getTg() {
		return this.Tg;
	}
	public Set<OWLClassExpression> findConcept(OWLClassExpression c){
		Set<OWLClassExpression> ce = new HashSet<OWLClassExpression>();
		this.Tu.stream().filter(sb -> sb.getSubClass().equals(c)).forEach(sb -> ce.add(sb.getSuperClass()));
		return ce;
	}
	
	public OWLClassExpression getTgAxiom(OWLDataFactory df) {
		OWLClassExpression union;
	 	Set<OWLClassExpression> unionSet = new HashSet<>();
		for (OWLSubClassOfAxiom sb : this.getTg()) {
    		union = df.getOWLObjectUnionOf(sb.getSubClass().getComplementNNF(), sb.getSuperClass());
    		unionSet.add(union);
    		
		}
		OWLClassExpression intersection = df.getOWLObjectIntersectionOf(unionSet);
		return intersection;
	}
	/*public OWLClassExpression parseClassExpression(OWLOntology ont, String expression){
		OWLDataFactory dataFactory = ont.getOWLOntologyManager().getOWLDataFactory();
		
		ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory, expression);
		

		return parser.parseClassExpression();
	}*/
	public void addInTu(OWLSubClassOfAxiom eqsb) {
		this.Tu.add(eqsb);
	}
	public void addInTg(OWLSubClassOfAxiom eqsb) {
		this.Tg.add(eqsb);
	}
}
