package io.jaxb.adapted;

import javax.xml.bind.annotation.XmlRootElement;

import db.AccessMethod;
import db.Relation;

@XmlRootElement(name="accessMethod")
public class AdaptedAbridgedAccessMethod extends AbstractAdaptedAccessMethod {

	public AdaptedAbridgedAccessMethod() { }

	public AdaptedAbridgedAccessMethod(AccessMethod accessMethod) {
		super(accessMethod);
	}
	
	// Note: this is potentially confusing because the passed relation might 
	// already have access methods associated (e.g. if this method is called
	// repeatedly). Hence we make this method package private.
	AccessMethod toAccessMethod(Relation relation) {
		
		AccessMethod ret = new AccessMethod(this.name, this.attributes, relation);
		relation.addAccessMethod(ret);
		return ret;
	}
}
