package io.jaxb.adapted;

import java.lang.reflect.Type;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import db.Attribute;

@XmlRootElement(name="attribute")
public class AdaptedAttribute {
	
	private Type type;
	private String name;
	
	public AdaptedAttribute() {	}
	
	public AdaptedAttribute(Attribute attr) {
		this.type = attr.getType();
		this.name = attr.getName();
	}
	
	// Note: we cannot annotate this method with @XmlAttribute, since Type is
	// an interface (and JAXB can't handle interfaces). Instead we have a 
	// getTypeName method returning a String, and the corresponding setter
	// takes a String but sets the Type field. 
	public Type getType() {
		return this.type;
	}

	@XmlAttribute(name = "type")
	public String getTypeName() {
		return this.getType().getTypeName();
	}

	public void setTypeName(String typeName) throws ClassNotFoundException {
		this.type = Class.forName(typeName);
	}
	
	@XmlAttribute
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Attribute toAttribute() {
		return new Attribute(this.type, this.name);
	}
}
