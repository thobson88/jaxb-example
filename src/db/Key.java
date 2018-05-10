package db;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Key {

	private final Relation relation;
	// old: private final Attribute attribute;
	private final Attribute[] attributes;

	public Key(Relation relation, Attribute[] attributes) {
		
		List<String> attrNames = Arrays.stream(attributes)
				.map(attr -> attr.getName())
				.collect(Collectors.toList());
		
		if (!Arrays.stream(relation.getAttributes())
				.map(attr -> attr.getName())
				.collect(Collectors.toList()).containsAll(attrNames))
			throw new IllegalArgumentException("One or more attribute names not found");

		this.relation = relation;
		this.attributes = attributes;
	}

	public Relation getRelation() {
		return relation;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}
}
