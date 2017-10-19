package reasoner;

import org.semanticweb.owlapi.model.OWLOntology;


import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

public class KnowledgeBase {
	private OWLDataFactory df;
	protected final Configuration configuration;
	 /** set of axioms */
   
    
	public KnowledgeBase(Configuration con, OWLDataFactory df) {
		 this.df = df;
		 this.configuration=con;
	}


	
}
