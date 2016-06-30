package states;

import java.util.ArrayList;

public class InvariantMain {

	private ArrayList<ClassInvariant> classInvariant;
	private ArrayList<ObjectInvariant> objectInvariant;
	private ArrayList<EventInvariant> eventInvariant;
	
	public InvariantMain(){
		classInvariant = new ArrayList<ClassInvariant>();
		objectInvariant = new ArrayList<ObjectInvariant>();
		eventInvariant = new ArrayList<EventInvariant>();
	}
	
	//Setters
	
	
	//Getters

	/**
	 * 
	 * @return
	 */
	public ArrayList<ClassInvariant> getClassInvariants(){
		return classInvariant;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<ObjectInvariant> getObjectInvariants(){
		return objectInvariant;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<EventInvariant> getEventInvariants(){
		return eventInvariant;
	}
	
	//Methods 
	/**
	 * Add new CLASS invariant
	 * @param classInvariant
	 */
	public void addNewClassInvariant(ClassInvariant classInvariant){
		this.classInvariant.add(classInvariant);
	}
	
	/**
	 * Add new Object invariant
	 * @param objectInvariant
	 */
	public void addNewObjectInvariant(ObjectInvariant objectInvariant){
		this.objectInvariant.add(objectInvariant);
	}
	
	/**
	 * Add new Event invariant
	 * @param eventInvariant
	 */
	public void addNewEventInvariant(EventInvariant eventInvariant){
		this.eventInvariant.add(eventInvariant);
	}
	
	/**
	 * Prints invariants read	
	 */
	public String toString(){
		StringBuilder invariants = new StringBuilder();
		
		for(ClassInvariant clsInv: classInvariant){
			invariants.append(clsInv.toString());
		}
		for(ObjectInvariant objtInv: objectInvariant){
			invariants.append(objtInv.toString());					
		}
		int count = 0; 
		for(EventInvariant evntInv: eventInvariant){
			invariants.append(evntInv.toString());	
			count++;
		}
		System.out.println("Events: "+count);
		
		return invariants.toString();
	}
	
	
}
