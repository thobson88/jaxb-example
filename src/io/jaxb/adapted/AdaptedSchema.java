package io.jaxb.adapted;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import db.Schema;

@XmlRootElement(name="schema")
@XmlType(propOrder = {"relations", "dependencies"})
public class AdaptedSchema {

	protected List<AdaptedAbridgedRelation> relations;
	protected List<AdaptedDependency> dependencies;
	
	public AdaptedSchema() { }
	
	public AdaptedSchema(Schema schema) {

		this.relations = Arrays.asList(schema.getRelations()).stream()
				.map(relation -> new AdaptedAbridgedRelation(relation))
				.collect(Collectors.toList());
		this.dependencies = Arrays.asList(schema.getDependencies()).stream()
				.map(d -> new AdaptedDependency(d))
				.collect(Collectors.toList());
	}
	
	@XmlElements({ @XmlElement(name = "relation", type = AdaptedAbridgedRelation.class) })
	@XmlElementWrapper(name="relations")
	public List<AdaptedAbridgedRelation> getRelations() {
		return this.relations;
	}
	
	@XmlElements({ @XmlElement(name = "dependency", type = AdaptedDependency.class) })
	@XmlElementWrapper(name="dependencies")
	public List<AdaptedDependency> getDependencies() {
		return this.dependencies;
	}
}
