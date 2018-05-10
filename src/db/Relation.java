package db;

import java.util.ArrayList;
import java.util.List;

/**
 * A database relation.
 */
public class Relation {

	private String name;
	private Attribute[] attributes;
	private List<AccessMethod> accessMethods = new ArrayList<AccessMethod>();
	
	public Relation(String name, Attribute[] attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	public String getName() {
		return this.name;
	}
	
	public Attribute[] getAttributes() {
		return this.attributes;
	}
	
	public List<AccessMethod> getAccessMethods() {
		return this.accessMethods;
	}
	
	public void addAccessMethod(AccessMethod accessMethod) {
		
		if (!accessMethod.getRelation().equals(this))
			throw new IllegalArgumentException();
		this.accessMethods.add(accessMethod);
	}
}
