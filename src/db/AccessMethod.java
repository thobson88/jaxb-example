package db;

/**
 * A database access method.
 */
public class AccessMethod {

	private String name;
	private Attribute[] attributes;
	private final Relation relation;

	// A Relation instance is required to construct an AccessMethod. 
	public AccessMethod(String name, Attribute[] attributes, Relation relation) {
		this.name = name;
		this.attributes = attributes;
		if (relation == null)
			throw new IllegalArgumentException("relation must not be null!");
		this.relation = relation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public Relation getRelation() {
		return this.relation;
	}
}
