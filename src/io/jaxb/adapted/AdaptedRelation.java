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

	// Note that, unlike the other field, the accessMethods member is *adapted* 
	// (and abridged). It is convenient to convert to AccessMethod objects only 
	// when necessary, i.e. when toRelation is called, because the AccessMethod 
	// constructor requires a Relation instance argument.
	protected List<AdaptedAbridgedAccessMethod> accessMethods = new ArrayList<AdaptedAbridgedAccessMethod>();
	
	public AdaptedRelation() { }
	
	public AdaptedRelation(Relation relation) {
		super(relation);
		this.accessMethods = relation.getAccessMethods().stream()
				.map(am -> new AdaptedAbridgedAccessMethod(am))
				.collect(Collectors.toList());
	}

	// Note: getAccessMethods returns a List<AdapatedAbridgedAccessMethod>, 
	// not a List<AccessMethod>. We use the abridged option since each access 
	// method is being serialised indirectly, i.e. as part of a relation. 
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
		// First construct a relation without any access methods, then use
		// that object to convert the *abridged* adapted the access methods
		// into AccessMethod instances, then add them to the relation and return.
		Relation relation = super.toRelation();
		for (AdaptedAbridgedAccessMethod am : this.accessMethods)
			relation.addAccessMethod(am.toAccessMethod(relation));
		return relation;
	}
}
