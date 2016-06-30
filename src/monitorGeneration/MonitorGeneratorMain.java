package monitorGeneration;

import java.util.ArrayList;
import java.util.HashMap;

import states.*;

public class MonitorGeneratorMain {

	private String locationToPrintMonitors = "./Monitors";
	public static HashMap<String,Boolean> variablesToPrint = new HashMap<String,Boolean>();
	
	public MonitorGeneratorMain(){}

	public MonitorGeneratorMain(String locationToPrintMonitors){
		this.locationToPrintMonitors = locationToPrintMonitors;
	}
	
	//Setters

	/**
	 * 
	 * @param locationToPrintMonitors
	 */
	public void setLocationToPrintMonitors(String locationToPrintMonitors){
		this.locationToPrintMonitors = locationToPrintMonitors;
	}
	
	//Getters

	/**
	 * 
	 * @return
	 */
	public String getLocationToPrintMonitors(){
		return locationToPrintMonitors;
	}
	
	//Methods
	
	/**
	 * 
	 * @param stringToWrite
	 */
	public void initializeAspectJFile(StringBuilder stringToWrite){
		stringToWrite.append("package monitor;\n\n");
		stringToWrite.append("import java.lang.reflect.Field;\nimport java.util.HashMap;\n\n");
		stringToWrite.append("public aspect Aspect {\n\n");
	}
	
	/**
	 * 
	 * @param stringToWrite
	 */
	public void endAspectJFile(StringBuilder stringToWrite){
		stringToWrite.append("}");
	}
	
	private StringBuilder generateGlobalVariables(){
		StringBuilder stringToWrite = new StringBuilder();
		
		stringToWrite.append("\n\n");
		for(String invariant: variablesToPrint.keySet()){
			stringToWrite.append(invariant+"\n");
		}
		stringToWrite.append("\n\n");
		
		return stringToWrite;
	}
	
	
	/**
	 * Generates monitors from the invariants observed 
	 */
	public void generateMonitors(boolean generateClasses, boolean generateObjects, boolean generateEvents, String pathToSource, InvariantMain invariantMain){

		StringBuilder stringToWrite = new StringBuilder();
		StringBuilder invariantsToWrite = new StringBuilder();
		
		initializeAspectJFile(stringToWrite);
		
		//Start monitor generation
		//For classes NOT YET IMPLEMENTED
		if(generateClasses){
			System.out.println("WARNING Generation of Classes not yet supported!");
//			ArrayList<ClassInvariant> classInvariants = invariantMain.getClassInvariants();
//			ClassMonitorGenerator clsMonGen = new ClassMonitorGenerator();
//			
//			System.out.println("Size of classes identified: "+classInvariants.size());		
//			//For every class detected
//			for(ClassInvariant clss: classInvariants){
//				invariantsToWrite.append(clsMonGen.getClassMonitor(clss));
//			}
		}
		
		//for objects NOT YET SUPPORTED
		if(generateObjects){
			System.out.println("WARNING Generation of Objects not yet supported!");
//			ArrayList<ObjectInvariant> objectInvariants = invariantMain.getObjectInvariants();
//			ObjectMonitorGenerator objtMonGen = new ObjectMonitorGenerator();
//			
//			System.out.println("Size of objects identified: "+objectInvariants.size());
//			//For every object detected 
//			for(ObjectInvariant objt: objectInvariants){
//				invariantsToWrite.append(objtMonGen.getObjectMonitor(objt));
//			}
		}
		
		//for events
		if(generateEvents){
			ArrayList<EventInvariant> eventInvariants = invariantMain.getEventInvariants();
			EventMonitorGenerator evntMonGen = new EventMonitorGenerator();
			
			System.out.println("Size of events identified: "+eventInvariants.size());
			//For every event detected
			for(EventInvariant event: eventInvariants){
				invariantsToWrite.append(evntMonGen.getEventMonitors(pathToSource, event));
			}			
		}		
		
		//First write the Global Variables
		stringToWrite.append(generateGlobalVariables());
		
		//Then write the Aspects
		stringToWrite.append(invariantsToWrite);
		
		//Finalise AspectJ
		endAspectJFile(stringToWrite);	
		
		//Output Aspectj file
		//System.out.println("Printing file");
		OutputHelperFunctions output = new OutputHelperFunctions();
		output.writeToFile("Aspect", locationToPrintMonitors, stringToWrite);
		
	}
	
}
