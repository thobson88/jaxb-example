package io.jaxb.adapted;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import db.Attribute;
import db.Key;
import db.Relation;

@XmlType(propOrder = {"relationName", "attributeName"})
@XmlRootElement(name="key")
public class AdaptedKey {

	Relation relation;
	Attribute[] attributes;
	
	public AdaptedKey() { }
	
	public AdaptedKey(Key key) {
		this.relation = key.getRelation();
		this.attributes = key.getAttributes();
	}
	
	@XmlAttribute(name="relation")
	public String getRelationName() {
		return relation.getName();
	}

	@XmlElements({ @XmlElement(name = "attribute") })
	public String[] getAttributeName() {
		return Arrays.stream(this.attributes)
				.map(attr -> attr.getName())
				.toArray(String[]::new);
	}
}
