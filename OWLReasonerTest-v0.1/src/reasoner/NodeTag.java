package reasoner;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.*;

public enum NodeTag {
	/** illegal entry */                                    BAD           ("bad-tag"),
    /** operations */                                       TOP           ("*TOP*"),
    /** operations */                                       AND           ("and"),
    /** operations */                                       COLLECTION    ("collection"),
    /** operations */                                       FORALL        ("all"),
    /** operations */                                       LE            ("at-least"),
    /** operations */                                       GE            ("at-most"),
    /** operations */                                       EXISTS        ("exists"),
    /** operations */                                       OR            ("or"),
    /** NN-rule was applied */                              NN            ("Nominal node");
	
	@Nonnull private final String name;
	private NodeTag(String s) {
        name = s;
    }
	
}
