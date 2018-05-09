package io.jaxb.adapted;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import db.Key;
import db.Schema;

@XmlRootElement(name="schema")
@XmlType(propOrder = {"relations", "dependencies"})
public class AdaptedSchema {

	protected List<AdaptedAbridgedRelation> relations;
	protected Map<Key, Key> dependencies;
	
	public AdaptedSchema() { }
	
	public AdaptedSchema(Schema schema) {

		this.relations = Arrays.asList(schema.getRelations()).stream()
				.map(relation -> new AdaptedAbridgedRelation(relation))
				.collect(Collectors.toList());
		this.dependencies = schema.getDependencies();
	}
	
	@XmlElements({ @XmlElement(name = "relation", type = AdaptedAbridgedRelation.class) })
	public List<AdaptedAbridgedRelation> getRelations() {
		return this.relations;
	}
	
	@XmlElements({ @XmlElement })
	public Map<AdaptedKey, AdaptedKey> getDependencies() {
		return this.dependencies.keySet().stream()
				.collect(Collectors.toMap(key -> new AdaptedKey(key), 
						key -> new AdaptedKey(this.dependencies.get(key))));
	}
}
