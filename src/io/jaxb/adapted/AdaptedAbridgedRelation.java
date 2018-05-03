package io.jaxb.adapted;

import javax.xml.bind.annotation.XmlRootElement;

import db.Relation;

@XmlRootElement(name="relation")
public class AdaptedAbridgedRelation extends AbstractAdaptedRelation {

	public AdaptedAbridgedRelation() { }

	public AdaptedAbridgedRelation(Relation relation) {
		super(relation);
	}
}
