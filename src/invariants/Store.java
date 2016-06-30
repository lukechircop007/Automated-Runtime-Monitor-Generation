package invariants;

import monitorGeneration.MonitorGeneratorMain;
import factory.InvariantTemplate;


/*
 * Not an invariant but used to store the variable state so that it can be accessed later. 
 */

public class Store implements InvariantTemplate{

	public boolean checkIfInvariantMatches(String invariant) {
		return invariant.contains("store(");
	}
	
	public String processParamNumber(String param){
		System.out.println("WARNING: To Revize Store functionality due to IXARIS invariants detected");
		String num = "";
		
		//Assuming store invariant is of type store(parameter[NUMBER])		
		num = param.trim().replace("store(", "").replace(")", "");
		if(num.contains("parameter[")){
			num = num.replace("parameter[", "").replace("]", "");
		}
		
		return num;
	} 
	
	/**
	 * 
	 */
	public StringBuilder generateMonitoringCode(String invariant, String eventName, Object event) {
		StringBuilder toPrint = new StringBuilder();
		
		//Add to list of variables we need to store...
		MonitorGeneratorMain.variablesToPrint.put("HashMap<Object, HashMap<Integer,Object>> "+eventName.replace(".", "")+" = new HashMap<Object, HashMap<Integer,Object>>();",true);
		//Process invariant to get parameter number to use later on
		String position = processParamNumber(invariant);
		
		//Generate code to store the value of the parameter required 
		toPrint.append("\nif("+eventName.replace(".", "")+".containsKey(thisJoinPoint.getThis())){\n");
		toPrint.append("    "+eventName.replace(".", "")+".get(thisJoinPoint.getThis()).put("+position+", thisJoinPoint.getArgs()["+position+"]);\n");
		toPrint.append("}else{\n");
		toPrint.append("    HashMap<Integer,Object> origParam = new HashMap<Integer,Object>();\n");
		toPrint.append("    origParam.put("+position+", thisJoinPoint.getArgs()["+position+"]);\n");
		toPrint.append("    "+eventName.replace(".", "")+".put(thisJoinPoint.getThis(),origParam);\n");
		toPrint.append("}\n");
		
		return toPrint;
	}

	public String getInvariantTypeName() {
		return "Store";
	}

	@Override
	public void setPathToSource(String pathToSource) {
		// TODO Auto-generated method stub
		
	}

}
