package states;

import java.util.ArrayList;

public class ObjectInvariant {

	private String objectName;
	private Type objectType;
	
	private ArrayList<Invariant> invariantList;
	
	public ObjectInvariant(){
		this.objectName = "";
		this.objectType = null;
		this.invariantList = new ArrayList<Invariant>();
	}
	
	//Setters

	/**
	 * 
	 * @param className
	 */
	public void setObjectName(String className){
		this.objectName = className;
	}
	
	/**
	 * 
	 * @param classType
	 */
	public void setObjectType(Type classType){
		this.objectType = classType;
	}
	
	/**
	 * 
	 * @param invariantList
	 */
	public void setInvariantList(ArrayList<Invariant> invariantList){
		this.invariantList = invariantList;
	}
	//Getters
	
	/**
	 * 
	 * @return
	 */
	public String getObjectName(){
		return this.objectName;
	}
	
	/**
	 * 
	 * @return
	 */
	public Type getType(){
		return this.objectType;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Invariant> getInvariantList(){
		return this.invariantList;
	}
	
	//Methods
	
	/**
	 * 
	 * @param invariant
	 */
	public void addNewInvariant(Invariant invariant){
		this.invariantList.add(invariant);
	}
	
	
	public String toString(){		
		StringBuilder objtInv = new StringBuilder();
	
		objtInv.append("===========================================================================\n");
		objtInv.append(getObjectName()+":::OBJECT\n");
			
		for(Invariant inv :invariantList ){
			objtInv.append(inv.toString()+"\n");			
		}		
		
		return objtInv.toString();
	}
}
