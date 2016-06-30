package monitorGeneration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import factory.InvariantFactory;
import factory.InvariantTemplate;

import states.EventInvariant;
import states.Type;

import states.Invariant;

public class EventMonitorGenerator {

	
	
	//Methods 
	
	/**
	 * 
	 * @param pathToSource
	 * @param evntInv
	 * @return
	 */
	public StringBuilder getEventInvariantChecks(String pathToSource, EventInvariant evntInv){
		StringBuilder stringToWrite = new StringBuilder();
		
		//Get all of the invariants 
		ArrayList<Invariant> evntInvLst = evntInv.getInvariantList();
		
		//Get all of the current invariants supported 
		InvariantFactory invFact = InvariantFactory.getInstance();		
		ArrayList<InvariantTemplate> invList = invFact.getListOfInvariants();

		boolean foundInv = false;
		//For every invariant
		for(Invariant invariant : evntInvLst){
			//reset 
			foundInv = false;
			//Check if there is an invariant matched 
			for(InvariantTemplate invTemp: invList){
				//If it matches append the string check
				if(invTemp.checkIfInvariantMatches(invariant.getInvariantString())){
					foundInv = true;
					invTemp.setPathToSource(pathToSource);
					stringToWrite.append(invTemp.generateMonitoringCode(invariant.getInvariantString(),evntInv.getEvent(),evntInv));
				}
			}
			//Print the invariant string that did not match any of the supported invariants as a warning
			if(!foundInv){
				//System.out.println("WARNING: No invariant was matched. Invariant string: "+invariant.getInvariantString());
			}
		}		
		return stringToWrite;
	}
	
	/**
	 * 
	 * @param evntInv
	 * @return
	 */
	public StringBuilder getAspectEventWithParameters(EventInvariant evntInv){
		StringBuilder stringToWrite = new StringBuilder();
		
		//Currently assuming that we do not need to specify the parameters still they can still be accessed
		stringToWrite.append(evntInv.getEvent());		
		stringToWrite.append("(..)");
		
		return stringToWrite;
	}
	
	private String getReturnType(String pathToSource, EventInvariant evntInv){
		String retType = "Object";
		File file = new File(pathToSource);

		try {
		    // Convert File to a URL
		    URL url = file.toURL();         
		    URL[] urls = new URL[]{url};

		    // Create a new class loader with the directory
		    ClassLoader cl = new URLClassLoader(urls);
		    // Load in the class; MyClass.class should be located
		    Class cls = cl.loadClass(evntInv.getEvent().substring(0, evntInv.getEvent().lastIndexOf(".")));
		    
		    Method[] allMethods = cls.getDeclaredMethods();
		    for (Method m : allMethods) {
				if (!m.getName().equals(evntInv.getEvent().substring(evntInv.getEvent().lastIndexOf(".")+1))) {
				    continue;
				}
				//System.out.println(evntInv.getEvent().substring(evntInv.getEvent().lastIndexOf(".")+1));
				retType = m.getGenericReturnType().toString().replace("class ", "");
				if(retType.equals("void")){
					return "Object";
				}
				return retType;
			}	    
		    
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return retType;
	}
	
	/**
	 * 
	 * @param pathToSource
	 * @param evntInv
	 * @return
	 */
	private StringBuilder getEntryMonitors(String pathToSource, EventInvariant evntInv){
		StringBuilder stringToWrite = new StringBuilder();
		
		stringToWrite.append("before(): call(*  "+getAspectEventWithParameters(evntInv)+"){\n");		
			stringToWrite.append(getEventInvariantChecks(pathToSource,evntInv));		
		stringToWrite.append("};\n\n");
		
		return stringToWrite;
	}
	
	/**
	 * 
	 * @param pathToSource
	 * @param evntInv
	 * @return
	 */
	private StringBuilder getExitMonitors(String pathToSource, EventInvariant evntInv){
		StringBuilder stringToWrite = new StringBuilder();
		
		stringToWrite.append("after() returning("+getReturnType(pathToSource, evntInv)+" returnValue): call(*  "+getAspectEventWithParameters(evntInv)+"){\n");
			stringToWrite.append(getEventInvariantChecks(pathToSource,evntInv));		
		stringToWrite.append("};\n\n");
		
		return stringToWrite;
	}
	
	/**
	 * 
	 * @param pathToSource
	 * @param evntInv
	 * @return
	 */
	public StringBuilder getEventMonitors(String pathToSource, EventInvariant evntInv){
		StringBuilder stringToWrite = new StringBuilder();
		
		//if a before event
		if(evntInv.getType() == Type.ENTER)
		{			
			stringToWrite.append(getEntryMonitors(pathToSource, evntInv));
		}else{//or if an after event
			stringToWrite.append(getExitMonitors(pathToSource, evntInv));
		}
		
		return stringToWrite;
	}
}
