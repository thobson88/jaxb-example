package db;

import java.lang.reflect.Type;

public class Attribute {

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	private final String name;
	private final Type type;
	
	public Attribute(Type type, String name) {
		this.type = type;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Attribute))
			return false;
		Attribute a = (Attribute) other; 
		return this.getName().equals(a.getName()) && this.getType().equals(a.getType());
	}
}
