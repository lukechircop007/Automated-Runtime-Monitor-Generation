package states;

import java.util.ArrayList;

public class ClassInvariant {
	
	private String className;
	private Type classType;
	private ArrayList<Invariant> invariantList;
	
	public ClassInvariant(){
		this.className = "";
		this.classType = null;
		this.invariantList = new ArrayList<Invariant>();
	}

	//Setters
	
	/**
	 * 
	 * @param className
	 */
	public void setClassName(String className){
		this.className = className;
	}
	
	/**
	 * 
	 * @param classType
	 */
	public void setClassType(Type classType){
		this.classType = classType;
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
	public String getClassName(){
		return this.className;
	}
	
	/**
	 * 
	 * @return
	 */
	public Type getType(){
		return this.classType;
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
		StringBuilder clsInv = new StringBuilder();

		clsInv.append("===========================================================================\n");
		clsInv.append(getClassName()+":::CLASS\n");

		for(Invariant inv :invariantList ){
			clsInv.append(inv.toString()+"\n");			
		}		
		
		return clsInv.toString();
	}
}
