package db;

public class Relation {

	private String name;
	private Attribute[] attributes;
	
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
}
