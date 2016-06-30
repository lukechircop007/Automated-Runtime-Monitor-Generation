package invariants;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.lang.model.type.ReferenceType;

import factory.InvariantTemplate;
import states.EventInvariant;

public class EqualsInvariant implements InvariantTemplate{

	public InvType type = null;
	private String pathToSource;
	private String returnVariableGetterString;
	private boolean returnHasVariables;
	
	/**
	 * Enum created to keep track of the type of invariant that was detected
	 * @author User
	 *
	 */
	public enum InvType {
		EQUALS,SMALLER,GREATER,SMALLEREQUAL,GREATEREQUAL, NOTEQUAL
	}
	
	
	@Override
	public void setPathToSource(String pathToSource) {
		this.pathToSource = pathToSource;
	}

	/**
	 * Checks if the invariant passed matches with the invariants supported. 
	 */
	public boolean checkIfInvariantMatches(String invariant) {
		if(invariant.contains("==")){
			type = InvType.EQUALS;
			return true;			
		} else if(invariant.contains("<=")){
			type = InvType.SMALLEREQUAL;
			return true;			
		} else if(invariant.contains(">=")){
			type = InvType.GREATEREQUAL;
			return true;			
		} else if(invariant.contains("<")){
			type = InvType.SMALLER;
			return true;			
		} else if(invariant.contains(">")){
			type = InvType.GREATER;
			return true;			
		} else if(invariant.contains("!=")){
			type = InvType.NOTEQUAL;
			return true;			
		}
		return false;
	}
	
	/**
	 * Loads the class given as a string and returns it
	 * @param className
	 * @return
	 */
	private Class<?> loadClass(String className){
		try {
			//load source file
			File file = new File(pathToSource);
			// if it does not exist throw error
			if(!file.exists()){System.out.println("WARNING the path to the source code is incorrect!"); return null;}
			
		    // Convert File to a URL
		    URL url = file.toURL();         
		    URL[] urls = new URL[]{url};

		    // Create a new class loader with the directory
		    ClassLoader cl = new URLClassLoader(urls);
		    // Load in the class; MyClass.class should be located
		    Class<?> cls = cl.loadClass(className);
		    
		    //if it is not null return the loaded class
		    if(cls != null){
		    	return cls;
		    }			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Returns the method from the loaded class 
	 * 
	 * @param cls
	 * @param methodName
	 * @return
	 */
	private Method loadMethodFromClass(Class<?> cls, String methodName){
		try {
			// Get all methods declared in a class
			Method[] allMethods = cls.getDeclaredMethods();
			//Loop through the methods until you find the method that is being asked for 
			for(Method m: allMethods){
				// if the method name matches, return the method
				if (m.getName().equals(methodName)) {
				    return m;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the appropriate cast required by the parameters.
	 * @param position
	 * @param paramTypes
	 * @return
	 */
	private String getCast(String position, ArrayList<String> paramTypes){
		
		// need to check primitives and return cast only for those... 
		
		int pos = Integer.parseInt(position.trim());
		String cast = paramTypes.get(pos);
		if(cast.equals("V") || cast.equals("Z")){
			return "";
		}else if(cast.contains(";")){
			if(cast.contains("java.lang.")){
				if(cast.contains("Integer") || cast.contains("String")|| cast.contains("Byte")|| cast.contains("Long")|| cast.contains("Short")|| cast.contains("Float") || cast.contains("Boolean")|| cast.contains("Character") || cast.contains("Double")){
					//System.out.println("Cast: "+cast);
					//types usually of the following format: Ljava.lang.String
					return cast.substring(1).replace(";", "");
				}
			}
			//System.out.println("Cast: "+cast.substring(1).replace(";", ""));
		}else if(cast.equals("D")){
			return "Double";
		}
		System.out.println("WARNING: Parameter with type: "+cast+" Not Supported");		
		return "";
	}
	
	/**
	 * 
	 * @param param
	 * @param eventName
	 * @return
	 */
	private String getReturnCast(String param, String eventName){

		//Load class
		Class<?> cls = loadClass(eventName.substring(0, eventName.lastIndexOf(".")));
		//Find method using event name
		Method m = loadMethodFromClass(cls, eventName.substring(eventName.lastIndexOf(".")+1));
		
		//Depending on parameter return cast
		if(param.trim().equals("returnValue")){//If param is only returnValue
			return "";//CURRENTLY NO NEED TO SUPPORT IT
		} else if(param.trim().contains("returnValue.")){
			//Taking into consideration different return types... 
			if(!param.trim().replace("returnValue.", "").contains(".")){//returnValue.VARIABLENAME
				Class<?> methodRetType = m.getReturnType();
				//System.out.println("ClassName: "+m.getGenericReturnType().getTypeName()+ "type is: "+m.getGenericReturnType().getClass().isArray());
								
				if(methodRetType.isArray() || methodRetType.getTypeName().contains("ArrayList")){
//					if(m.getGenericReturnType().toString().replace("class ", "").contains("ArrayList<")){
//						return "("+m.getGenericReturnType().toString().replace("class ", "")+")";
//					}
					System.out.println("WARNING: Array return type not supported yet!");
				}else {
					Class<?> retCls = loadClass(m.getGenericReturnType().getTypeName());
					try {
						Field var = retCls.getDeclaredField(param.replace("returnValue.", "").trim());
						var.setAccessible(true);	
						if(var.getType().isEnum()){
							return "";
						}
						return "("+var.getType().toString().replace("class ","").trim()+")";
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
//				//for return values that are of the following type: returnValue.VARIABLE
//				Class<?> methodRetType = m.getReturnType();
//				//System.out.println("ClassName: "+m.getGenericReturnType().getTypeName()+ "type is: "+m.getGenericReturnType().getClass().isArray());
//				
//				if(methodRetType.isPrimitive()){
//					System.out.println("Primitive type: "+methodRetType.getTypeName());
//				}else{
//					System.out.println("Not primitive: "+methodRetType.getTypeName());
//				}
				
			}
		} 
		//Other return types?
		System.out.println("WARNING: return type not supported yet! "+param);
		return "";
	}

		
	/**
	 * 
	 * @param param
	 * @param eventName
	 * @return
	 */
	private String getReturnParam(String param, String eventName){
		//types of return values: returnValue,  returnValue.VARIABLENAME, returnValue.ENUMVARIABLE.STATE
		if(param.trim().equals("returnValue")){
			//If boolean need to translate it into integer ...
			//May need to take care of other types of variables
			//load class
			Class<?> cls = loadClass(eventName.substring(0, eventName.lastIndexOf(".")));
			//find method
			Method m = loadMethodFromClass(cls, eventName.substring(eventName.lastIndexOf(".")+1));

			// If a boolean...
			if(m.getGenericReturnType().toString().replace("class ", "").contains("boolean")){
				return "(returnValue ? 1 : 0)";
			}
			return param;
		}else if(param.trim().contains("returnValue.")){
			// if returnValue.VARIABLENAME
			if(!param.trim().replace("returnValue.", "").contains(".")){
				String processing = param.trim().replace("returnValue.", "");
				returnVariableGetterString += "\nField "+param.replace(".", "")+" = returnValue.getClass().getDeclaredField(\""+processing+"\");\n";
				returnVariableGetterString += param.replace(".", "").trim()+".setAccessible(true);\n";
				returnHasVariables = true;
				String cast = getReturnCast(param, eventName);
				if(cast.contains("boolean")){
					return "(("+cast+""+param.replace(".", "").trim()+".get(returnValue)) ? 1 : 0)";
				}else if (cast.contains("ArrayList<")){//Stopped supporting for version 1
					//return "("+cast+""+param.replace(".", "").trim()+".get(returnValue)).size()";
				}else if(cast.contains("")) return "";
				return cast+""+param.replace(".", "").trim()+".get(returnValue)";
			}else {//ENUM ETC... 				
				String processing = param.trim().replace("returnValue.","");
				//Load class
				Class<?> cls = loadClass(eventName.substring(0, eventName.lastIndexOf(".")));				
				//Load method
				Method m = loadMethodFromClass(cls, eventName.substring(eventName.lastIndexOf(".")+1));
				//Process the parameter even more to get the variable name
				String processingVariable = processing.trim().substring(0, processing.indexOf("."));
				
				try {
					// Load the class of the type of the return value
					Class<?> returnCls = loadClass(m.getGenericReturnType().getTypeName());	
					//Get the parameter				
					Field returnFld = returnCls.getDeclaredField(processingVariable);
					//retrieve its type
					Class<?> fld = returnFld.getType();
					//if it is of type enum: 
					if(fld.isEnum()){	
						//Enum types usually of the following form:
						// returnValue.mode.ACTIVE.ordinal or  returnValue.mode.ACTIVE.name
						if(param.contains(".ordinal")){
							//System.out.println("Ordinal Enum: "+param);	
							//append .ordinal()
						}else if(param.contains(".name")){
							//System.out.println("Name Enum: "+param);	
							//append .name()
						}else{
							//System.out.println("Normal Enum: "+param);	
							//Assume that it is normal enum i.e. returnValue.type.SILVER
							//will need to get enum field and work around it as usual 
						}
						//System.out.println("WARNING: Enums currently not supported");
						return "";
					}else{
						//System.out.println("Param: "+param);
						return "";
					}
					//Else arraylist...
						
				} catch (Exception e) {
					e.printStackTrace();
				}				
				//System.out.println("WARNING: Variable type not yet supported! "+param);
			}		
		} 
		return param;
	}
	
	/**
	 * Will be called to process the left and right condition separately. ASSUMING that bct considers only parameters and return values no global variables included
	 * 
	 * @param doCasting
	 * @param param
	 * @param eventName
	 * @param paramTypes
	 * @return
	 */
	private String processParam(boolean doCasting, String param, String eventName, ArrayList<String> paramTypes){
		
		//First we need to process the parameter given
		//If it contains a calculation of some sorts
		if(param.contains("%")){//if modulus of two parameters Not all the types may be supported yet
			String leftParam = processParam(true, param.substring(0,param.indexOf("%")),eventName,paramTypes);
			String rightParam = processParam(true, param.substring(param.indexOf("%")+2,param.length()-1), eventName,paramTypes);
			
			if(!(leftParam.contains("")||rightParam.contains(""))) {
				return leftParam+" % "+rightParam;	
			}else{
				return "";
			}
		} else if(param.contains("orig(")){//orig(PARAMETER)
			String cast = getCast(param.replace("orig(", "").replace(")", "").replace("parameter[", "").replace("]", ""),paramTypes);
			if(cast.equals("")) return "";
			return "("+cast+")"+eventName.replace(".","")+".get(thisJoinPoint.getThis()).get("+processParam(false,param.replace("orig(", "").replace(")", ""), eventName,paramTypes)+")";			
		} else if(param.contains("parameter[")){//parameter[POSITION].EXTRA.STUFF
			if(param.substring(param.indexOf("]")+1).trim().equals("")){
				if(doCasting){// if the parameter is of the following type: parameter[NUMBER]
					String cast = getCast(param.replace("parameter[", "").replace("]", ""),paramTypes);
					if(cast.equals("")) return "";
					return "("+cast+")thisJoinPoint.getArgs()["+processParam(false,param.replace("parameter[", "").replace("]", ""), eventName,paramTypes)+"]";		
				}// if parameter is of the following type: orig(parameter[NUMBER])
				return "thisJoinPoint.getArgs()["+processParam(false,param.replace("parameter[", "").replace("]", ""), eventName,paramTypes)+"]";
			}else{
				System.out.println("WARNING: param containing parameter "+param+" not supported");
			}
		} else if(param.contains("returnValue")){// if the parameter type is a return value
			return getReturnParam(param,eventName);	
		}
		//returns the parameter passed if it did not match any
		return param;
	}
		
	/**
	 * Must be called to generate condition for invariant... 
	 * Will process left and right side conditions
	 * 
	 * @param invariant The whole condition
	 * @param eventName The event name 
	 * @return
	 */
	private String getCondition(String invariant, String eventName, ArrayList<String> paramTypes){
		//Check type of the invariant that was detected 
		String cond = "";
		String leftParam = "";
		String rightParam = "";
		
		if(type == InvType.EQUALS){
			cond = "==";
		} else if(type == InvType.SMALLER){
			cond = "<";
		} else if(type == InvType.GREATER){
			cond = ">";
		} else if(type == InvType.SMALLEREQUAL){
			cond = "<=";
		} else if(type == InvType.GREATEREQUAL){
			cond = ">=";			
		} else if(type == InvType.NOTEQUAL){
			cond = "!=";
		}	
		if(type != null && !cond.equals("")){
			leftParam = processParam(true ,invariant.substring(0,invariant.indexOf(cond)), eventName ,paramTypes);
			rightParam = processParam(true ,invariant.substring(invariant.indexOf(cond)+2,invariant.length()-1), eventName ,paramTypes);
			if(!(leftParam.equals("") || rightParam.equals("") || (leftParam.contains("java.lang.String") || rightParam.contains("java.lang.String")))){
				return leftParam+" "+cond+" "+rightParam;					
			}
		}
		
		// return empty invariant if it was not one of the supported invariant... This return should never fire 
		//System.out.println("WARNING Type of invariant is not supported: "+type.toString());
		return "";
	}
	
	/**
	 * generates the appropriate monitoring code addording to the invariant passed 
	 */
	public StringBuilder generateMonitoringCode(String invariant, String eventName, Object event ) {
				
		StringBuilder toPrint = new StringBuilder();
		
		String condition = "";
		returnHasVariables = false;
		returnVariableGetterString = "";
		
		//Casting to event invariant type since we know that we are currently only processing events.. This may need to change in the future. 
		EventInvariant temp = (EventInvariant)event;
		
		//Get invariant condition
		condition = getCondition(invariant,eventName,temp.getParameters());
		
		//If it is empty, then there are no conditions to check.... and return empty
		if(condition.equals(""))return new StringBuilder();
		
		//if the getCondition method finds  a returnValue parameter.....
		if(returnHasVariables){
			//Initialize try catch 
			toPrint.append("try{\n");
			//print the returnValue getters
			toPrint.append(returnVariableGetterString);//to print variable getters here
		}
		
		//Printing of monitor condition checking 
		toPrint.append("if(!("+condition+")){\n");
			toPrint.append("System.out.println(\"VIOLATION!: Equals property Violated\");\n");
		toPrint.append("}\n");
		
		//close the try when the condition has return type parameters
		if(returnHasVariables){
			toPrint.append("} catch (Exception e) {\n e.printStackTrace();\n}\n");
		}
		
		return toPrint;
	}
	
	public String getInvariantTypeName() {
		return "IsEqualTo";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//	private String processParam(boolean doCasting, String param, String eventName, ArrayList<String> paramTypes){
//		//If the parameter has a calculation inside.... Not all the types may be supported yet
//		if(param.contains("%")){//if modulus of two parameters
//			String leftParam = processParam(true, param.substring(0,param.indexOf("%")),eventName,paramTypes);
//			String rightParam = processParam(true, param.substring(param.indexOf("%")+2,param.length()-1), eventName,paramTypes);
//			if(!(leftParam.contains("")||rightParam.contains(""))) {
//				return leftParam+" % "+rightParam;	
//			}else{
//				return "";
//			}
//		}else if(param.contains("returnValue")){// if the parameter type is a return value
//			return getReturnParam(param,eventName);			
//		}else if(param.contains("orig(")){//orig(PARAMETER)			
//			return "("+getCast(param.replace("orig(", "").replace(")", "").replace("parameter[", "").replace("]", ""),paramTypes)+")"+eventName.replace(".","")+".get(thisJoinPoint.getThis()).get("+processParam(false,param.replace("orig(", "").replace(")", ""), eventName,paramTypes)+")";
//		}else if(param.contains("parameter[")){//parameter[POSITION]
//			
//			
//			if(doCasting){// if the parameter is of the following type: parameter[NUMBER]
//				return "("+getCast(param.replace("parameter[", "").replace("]", ""),paramTypes)+")thisJoinPoint.getArgs()["+processParam(false,param.replace("parameter[", "").replace("]", ""), eventName,paramTypes)+"]";		
//			}// if parameter is of the following type: orig(parameter[NUMBER])
//			return "thisJoinPoint.getArgs()["+processParam(false,param.replace("parameter[", "").replace("]", ""), eventName,paramTypes)+"]";
//		}
//		//returns the parameter passed if it did not match any
//		return param;
//	}
//	
//	
//	private String getReturnCast(String param, String eventName){
//
//		//Load class
//		Class<?> cls = loadClass(eventName.substring(0, eventName.lastIndexOf(".")));
//		//Find method using event name
//		Method m = loadMethodFromClass(cls, eventName.substring(eventName.lastIndexOf(".")+1));
//		
//		//Depending on parameter return cast
//		if(param.trim().equals("returnValue")){//If param is only returnValue
//			return "";//CURRENTLY NO NEED TO SUPPORT IT
//		} else if(param.trim().contains("returnValue.")){
//			//Taking into consideration different return types... 
//			if(!param.trim().replace("returnValue.", "").contains(".")){//returnValue.VARIABLENAME
//				Type methodRetType = m.getGenericReturnType();
//				//System.out.println("ClassName: "+m.getGenericReturnType().getTypeName()+ "type is: "+m.getGenericReturnType().getClass().isArray());
//				
//				if(methodRetType.getClass().isArray()|| methodRetType.getTypeName().contains("ArrayList")){
//					if(m.getGenericReturnType().toString().replace("class ", "").contains("ArrayList<")){
//						return "("+m.getGenericReturnType().toString().replace("class ", "")+")";
//					}
//					System.out.println("WARNING Array return type has yet to be supported!");
//				}else {
//					Class<?> retCls = loadClass(m.getGenericReturnType().getTypeName());
//					try {
//						Field var = retCls.getDeclaredField(param.replace("returnValue.", "").trim());
//						var.setAccessible(true);	
//						if(var.getType().isEnum()){
//							return "";
//						}
//						return "("+var.getType().toString().replace("class ","").trim()+")";
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} 
//		//Other return types?
//		System.out.println("WARNING return type not supported yet! "+param);
//		return "";
//	}
	
	
	
	
	
	
	
	
	
}
