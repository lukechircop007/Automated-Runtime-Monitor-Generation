package factory;

import invariants.*;

import java.util.ArrayList;

public class InvariantFactory {

	private static InvariantFactory instance = null;
	private ArrayList<InvariantTemplate> listOfInvariants; 
	
	private InvariantFactory(){
		listOfInvariants = new ArrayList<InvariantTemplate>();		
		populateInvariantList();
	}
	
	public static InvariantFactory getInstance() {
		if(instance == null) {
			instance = new InvariantFactory();
		}
		return instance;
	}
	
	public void populateInvariantList(){
		listOfInvariants.add(new EqualsInvariant());	
		listOfInvariants.add(new Store());
	}
	
	public InvariantTemplate getInvariant(String invariant){
		if(invariant.equals("Equals")){
			return new EqualsInvariant();
		}else if(invariant.equals("Store")){
			return new Store();
		}
		return null;
	}
	
	public ArrayList<InvariantTemplate> getListOfInvariants(){
		return listOfInvariants;		
	}
	
}
