package states;

public class Invariant {

	private String invariantString;
	
	public Invariant(){
		invariantString = "";
	}
	
	public Invariant(String invariantString){
		this.invariantString = invariantString;
	}
	
	//Setters
	
	/**
	 * 
	 * @param invariantString
	 */	
	public void setInvariantString(String invariantString){
		this.invariantString = invariantString;
	}
	//Getters

	/**
	 * 
	 * @return
	 */
	public String getInvariantString(){
		return invariantString;
	}
	
	//Methods
	
	/**
	 * 
	 * @param appString
	 */
	public void appendInvariantString(String appString){
		this.invariantString += appString;
	}
	
	public String toString(){
		return invariantString;
	}
	
}
