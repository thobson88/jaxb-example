package io.jaxb.adapted;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import db.Attribute;
import db.Key;
import db.Relation;

@XmlType(propOrder = {"relationName", "attributeName"})
public class AdaptedKey {

	Relation relation;
	Attribute attribute;
	
	public AdaptedKey() { }
	
	public AdaptedKey(Key key) {
		this.relation = key.getRelation();
		this.attribute = key.getAttribute();
	}
	
	@XmlElement(name="relation")
	public String getRelationName() {
		return relation.getName();
	}

	@XmlElement(name="attribute")
	public String getAttributeName() {
		return attribute.getName();
	}
}
