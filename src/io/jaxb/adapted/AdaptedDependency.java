package io.jaxb.adapted;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import db.Dependency;
import db.Key;

@XmlType(propOrder = {"head", "body"})
public class AdaptedDependency {

	protected Key head;
	protected Key[] body;
	
	public AdaptedDependency() { } 
	
	public AdaptedDependency(Dependency dependency) { 
		this.head = dependency.getHead();
		this.body = dependency.getBody();
	}
	
	@XmlElements({ @XmlElement(name = "key") })
	@XmlElementWrapper(name="head")
	public AdaptedKey[] getHead() {
		return new AdaptedKey[] { new AdaptedKey(this.head) };
	}

	@XmlElements({ @XmlElement(name = "key") })
	@XmlElementWrapper(name="body")
	public AdaptedKey[] getBody() {
		return Arrays.stream(this.body)
				.map(key -> new AdaptedKey(key))
				.toArray(AdaptedKey[]::new);
	}
}

