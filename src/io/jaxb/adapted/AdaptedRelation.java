package io.jaxb.adapted;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import db.Relation;

@XmlRootElement(name="relation")
public class AdaptedRelation extends AbstractAdaptedRelation {

	// Note that the accessMethods member is *adapted* (and abridged) since it is convenient
	// to instantiate AccessMethod objects only when necessary, i.e. when toRelation is called, 
	// because the AccessMethod constructor requires a Relation instance argument.
	protected List<AdaptedAbridgedAccessMethod> accessMethods = new ArrayList<AdaptedAbridgedAccessMethod>();
	Relation relation;
	
	public AdaptedRelation() { }
	
	public AdaptedRelation(Relation relation) {
		this.name = relation.getName();
		this.attributes = relation.getAttributes();
		this.accessMethods = relation.getAccessMethods().stream()
				.map(am -> new AdaptedAbridgedAccessMethod(am))
				.collect(Collectors.toList());
	}

	// Note: getAccessMethods returns a List<AdapatedAbridgedAccessMethod>, not a List<AccessMethod>.
	@XmlElements({ @XmlElement(name = "accessMethod", type = AdaptedAbridgedAccessMethod.class) })
	public List<AdaptedAbridgedAccessMethod> getAccessMethods() {
		return this.accessMethods;
	}
	
	// Note: the type of the argument to setAccessMethods must match that of the 
	// return value from getAccessMethods (else deserialisation will not work).  
	public void setAccessMethods(List<AdaptedAbridgedAccessMethod> accessMethods) {
		// this.relation = new Relation(this.name, this.attributes);
		this.accessMethods = accessMethods;
	}

	// Override toRelation to include any access methods.
	@Override
	public Relation toRelation() {
		Relation relation = super.toRelation();
		for (AdaptedAbridgedAccessMethod am : this.accessMethods)
			relation.addAccessMethod(am.toAccessMethod(relation));
		return relation;
	}
}
