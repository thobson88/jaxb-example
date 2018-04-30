package io.jaxb.adapted;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import db.Attribute;
import db.Relation;

@XmlRootElement(name="relation")
public class AdaptedRelation {

	public AdaptedRelation() { }
	
	public AdaptedRelation(String name, Attribute[] attributes) {
		this.name = name;
		this.attributes = attributes;
	}
	
	private String name;
	private Attribute[] attributes;
	
	@XmlAttribute(name = "name")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@XmlElements({ @XmlElement(name = "attribute", type = AdaptedAttribute.class) })
	public AdaptedAttribute[] getAttributes() {
		return Arrays.stream(this.attributes)
				.map(attr -> new AdaptedAttribute(attr))
				.toArray(AdaptedAttribute[]::new);
	}
	
	public void setAttributes(AdaptedAttribute[] attributes) {
		this.attributes = Arrays.stream(attributes)
				.map(attr -> attr.toAttribute())
				.toArray(Attribute[]::new);
//		this.attributes = attributes;
	}

	public Relation toRelation() {
		return new Relation(this.name, this.attributes);
	}
}
