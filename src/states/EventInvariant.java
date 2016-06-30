package states;

import java.util.ArrayList;

public class EventInvariant {

	private String event;
	private String arguments;
	private String condition;
	private Type eventType;
	private int exitValue;
	private ArrayList<Invariant> invariantList;
	private ArrayList<String> parameters;
	
	
	public EventInvariant(){
		event = "";
		arguments = "";
		condition = "";
		eventType = null;
		exitValue = -1;
		invariantList = new ArrayList<Invariant>();
		parameters = new ArrayList<String>();
	}
	
	public EventInvariant(String event, String arguments ,String condition , Type eventType){
		this.setEvent(event);
		this.arguments = arguments;
		this.condition = condition;
		this.eventType = eventType;	
		invariantList = new ArrayList<Invariant>();
	}
	
	//Setters

	/**
	 * 
	 * @param eventType
	 */
	public void setType(Type eventType) {
		this.eventType = eventType;
	}

	/**
	 * 
	 * @param event
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	
	/**
	 * 
	 * @param arguments
	 */
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * 
	 * @param condition
	 */
	public void setCondition(String condition){
		this.condition = condition;
	}
	
	/**
	 * 
	 * @param exitValue
	 */
	public void setExitValue(int exitValue){
		this.exitValue = exitValue;
	}
	
	/**
	 * 
	 * @param invariantList
	 */
	public void setInvariantList(ArrayList<Invariant> invariantList){
		this.invariantList = invariantList;
	}
	
	/**
	 * 
	 * @param parameters
	 */
	public void setParameters(ArrayList<String> parameters){
		this.parameters = parameters;
	}
	
	//Getters
	
	/**
	 * 
	 * @return
	 */
	public Type getType() {
		return eventType;
	}

	/**
	 * 
	 * @return
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * 
	 * @return
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * 
	 * @return
	 */
	public String getCondition(){
		return condition;
	}	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Invariant> getInvariantList(){
		return this.invariantList;
	}
	
	/**
	 * 
	 */
	public int getExitValue(){
		return this.exitValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getParameters(){
		return this.parameters;
	}
	
	//Methods
	
	/**
	 * 
	 * @param invariant
	 */
	public void addNewInvariant(Invariant invariant){
		this.invariantList.add(invariant);
	}
	
	/**
	 * 
	 */
	public void addParameter(String parameter){
		parameters.add(parameter);
	}
	
	/**
	 * 
	 */
	public String toString(){
		StringBuilder evntInv = new StringBuilder();

		evntInv.append("===========================================================================\n");
		evntInv.append(getEvent()+"("+getArguments()+")");
		
		//different types of events
		if(getType() == Type.ENTER){
			evntInv.append(":::ENTER\n");
		}else if(getType() == Type.EXIT){
			evntInv.append(":::EXIT\n");			
		}else if(getType() == Type.EXITWITHNUM){
			evntInv.append(":::EXIT"+getExitValue()+"\n");			
		}else if(getType() == Type.EXITWITHCOND){
			evntInv.append(":::EXIT;condition=\""+getCondition()+"\"");			
		}else if(getType() == Type.EXITWITHNUMANDCOND){
			evntInv.append(":::EXIT"+getExitValue()+";condition=\""+getCondition()+"\"");			
		}

		for(Invariant inv :invariantList ){
			evntInv.append(inv.toString()+"\n");			
		}		
		return evntInv.toString();
	}
	
}
