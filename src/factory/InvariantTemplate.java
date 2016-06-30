package factory;

public interface InvariantTemplate {
	
	
	public void setPathToSource(String pathToSource);
	
	public String getInvariantTypeName();

	public boolean checkIfInvariantMatches(String invariant);
	
	public StringBuilder generateMonitoringCode(String invariant, String eventName, Object event);
	
	public String toString();
	
}
