package db;

/**
 * A database schema.
 */
public class Schema {

	private Relation[] relations;
	private Dependency[] dependencies;
	// old: private Map<Key, Key> dependencies = new HashMap<Key, Key>();
	
	public Schema(Relation[] relations, Dependency[] dependencies) {
		this.relations = relations;
		// TODO: check that: 
		// - all relations in the dependencies are in the given relations array
		// - the attribute types in the dependencies are consistent
		// - the relations in each key-value pair are different
		// - there are no repeated dependencies
		this.dependencies = dependencies;
	}

	public Relation[] getRelations() {
		return this.relations;
	}

	public Dependency[] getDependencies() {
		return this.dependencies;
	}
}
