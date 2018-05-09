package db;

import java.util.Arrays;

public class Key {

	private final Relation relation;
	private final Attribute attribute;

	public Key(Relation relation, Attribute attribute) {
		
		if (!Arrays.stream(relation.getAttributes())
				.anyMatch(attr -> attr.getName() == attribute.getName()))
			throw new IllegalArgumentException("Attribute name not found");

		this.relation = relation;
		this.attribute = attribute;
	}

	public Relation getRelation() {
		return relation;
	}

	public Attribute getAttribute() {
		return attribute;
	}
}
