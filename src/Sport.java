import java.awt.Color;
import java.util.ArrayList;

public class Sport {
	
	private Color color;
	private String name;
	private ArrayList<String> sessionTypes;

	public Sport(String sport, Color color) {

		name = sport;
		this.color = color;
		sessionTypes = new ArrayList<String>();
		
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void addSessionType(String typeName) {
		sessionTypes.add(typeName);		
	}
	
	public void removeSessionType(String typeName) {
		sessionTypes.remove(typeName);
	}

	/*
	 * Returns a shallow copy of the sessionTypes
	 */
	public ArrayList<String> getSessionTypes() {
		ArrayList<String> copy = new ArrayList<String>();
		if(!sessionTypes.isEmpty()) {
			copy.addAll(sessionTypes);
		}
		return copy;
	}
	
	/*
	 * Overriding the equals method for Sport class.
	 */
	public boolean equals(Object other) {
		if(other.getClass() == this.getClass() && ((Sport) other).getName().equals(name)) {
			return true;
		}
		return false;
	}

}
