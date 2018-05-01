package io.jaxb.adapted;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import db.AccessMethod;
import db.Attribute;

public abstract class AbstractAdaptedAccessMethod {

	protected String name;
	protected Attribute[] attributes;
	
	public AbstractAdaptedAccessMethod() { }

	public AbstractAdaptedAccessMethod(AccessMethod accessMethod) {
		this.name = accessMethod.getName();
		this.attributes = accessMethod.getAttributes();
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

	// Note: we do *not* put toAccessMethod() here in the abstract base class, because 
	// it is not possible to instantiate an AccessMethod object from an abridged
	// adapted access method. (This is in contrast to the Relation case.) So the 
	// toAccessMethod() method appears *only* in AdaptedAccessMethod.
}
