package io.jaxb.adapted;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import db.AccessMethod;
import db.Relation;

@XmlRootElement(name="accessMethod")
public class AdaptedAccessMethod extends AbstractAdaptedAccessMethod {

	protected Relation relation;
	protected AccessMethod accessMethod;

	public AdaptedAccessMethod() { }

	public AdaptedAccessMethod(AccessMethod accessMethod) {
		super(accessMethod);
		this.relation = accessMethod.getRelation();
	}

	// Note: getRelation returns an AdapatedAbridgedRelation, not a Relation.
	@XmlElement(name = "relation")
	public AdaptedAbridgedRelation getRelation() {
		return new AdaptedAbridgedRelation(this.relation);
	}

	// Note: the type of the argument to setRelation must match that of the
	// return value from getRelation.
	public void setRelation(AdaptedAbridgedRelation relation) {
		this.relation = relation.toRelation();
	}

	public AccessMethod toAccessMethod() {

		// Only do the instantiation once (to avoid the possibility of adding 
		// the access method to the relation multiple times).
		if (this.accessMethod == null) {
			this.accessMethod = new AccessMethod(this.name, this.attributes, this.relation);
			this.relation.addAccessMethod(this.accessMethod);
		}
		return this.accessMethod;
	}
}