package db;

import java.util.HashMap;
import java.util.Map;

public class Schema {

	private Relation[] relations;
	private Map<Key, Key> dependencies = new HashMap<Key, Key>();
	
	public Schema(Relation[] relations, Map<Key, Key> dependencies) {
		this.relations = relations;
		// TODO: check that: 
		// - all relations in the dependencies are in the given relations array
		// - the attribute types in the dependencies are consistent
		// - the relations in each key-value pair are different
		// - there are no repeated dependencies
		this.dependencies = dependencies;
	}

	public Relation[] getRelations() {
		return relations;
	}

	public Map<Key, Key> getDependencies() {
		return dependencies;
	}
}
