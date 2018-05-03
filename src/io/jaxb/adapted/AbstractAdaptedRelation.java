
package io.jaxb.adapted;

import java.util.Arrays;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import db.Attribute;
import db.Relation;

public abstract class AbstractAdaptedRelation {

	protected String name;
	protected Attribute[] attributes;
	
	public AbstractAdaptedRelation() { }

	public AbstractAdaptedRelation(Relation relation) {
		this.name = relation.getName();
		this.attributes = relation.getAttributes();
	}
	
	@XmlAttribute(name = "name")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	// Note: getAttributes returns an AdapatedAttribute[], not an Attribute[].
	@XmlElements({ @XmlElement(name = "attribute", type = AdaptedAttribute.class) })
	public AdaptedAttribute[] getAttributes() {
		return Arrays.stream(this.attributes)
				.map(attr -> new AdaptedAttribute(attr))
				.toArray(AdaptedAttribute[]::new);
	}
	
	// Note: the type of the argument to setAttributes must match that of the 
	// return value from getAttributes (else deserialisation will not work).  
	public void setAttributes(AdaptedAttribute[] attributes) {
		this.attributes = Arrays.stream(attributes)
				.map(attr -> attr.toAttribute())
				.toArray(Attribute[]::new);
	}

	// Note: we put toRelation here in the abstract base class, because it is 
	// possible to instantiate a Relation object from either and abridged or an
	// unabridged adapted relation. (This is in contrast to the AccessMethod case.) 
	public Relation toRelation() {
		return new Relation(this.name, this.attributes);
	}
}
