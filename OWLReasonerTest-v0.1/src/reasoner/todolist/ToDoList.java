package reasoner.todolist;

import static reasoner.todolist.PriorityMatrix.NREGULAROPTIONS;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLClassExpression;

import reasoner.NodeTag;
import reasoner.graph.Node;


public class ToDoList {
	private final List<RegQueue> waitQueue = new ArrayList<>(NREGULAROPTIONS);
	private final PriorityMatrix matrix = new PriorityMatrix();
	/** number of un-processed entries */
    private int noe;
	public ToDoList() {
		noe = 0;
		for (int i = 0; i < NREGULAROPTIONS; i++) {
            waitQueue.add(new RegQueue());
        }
	}
	
	public void addEntry(Node node, NodeTag type, OWLClassExpression ce) {
        int index = matrix.getIndex(type);
        waitQueue.get(index).add(node, ce, type);
        System.out.println("entry added ");
        /*switch (index) {
            case NREGULAROPTIONS: // unused entry
                return;
            case PRIORITYINDEXID: // ID
                queueID.add(node, c);
                break;
            case PRIORITYINDEXNOMINALNODE: // NN
                queueNN.add(node, c);
                break;
            default: // regular queue
                waitQueue.get(index).add(node, c);
                break;
        }*/
        ++noe; 
    }
	
	public ToDoEntry getNextEntry() {
        if(isEmpty())
        		return null;
        // decrease amount of elements-to-process
       
        // check ID queue
     /*   if (!queueID.isEmpty()) {
            return queueID.get();
        }
        // check NN queue
        if (!queueNN.isEmpty()) {
            return queueNN.get();
        }*/
        // check regular queues
        else {
        	 --noe;
        for (int i = 0; i < NREGULAROPTIONS; ++i) {
            RegQueue arrayQueue = waitQueue.get(i);
            if (!arrayQueue.isEmpty()) {
                return arrayQueue.get();
            }
        }
        // that's impossible, but still...
        return null;
        }
    }
	/** @return check if Todo table is empty */
    public boolean isEmpty() {
        return noe == 0;
    }
    public int entries() {
    		return noe;
    }
}
