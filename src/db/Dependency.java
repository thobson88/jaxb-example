package db;

/**
 * A dependency in a relational database.
 */
public class Dependency {

	private final Key head;
	private final Key[] body;

	public Dependency(Key head, Key[] body) {

		// Check that each of the given Key objects has a single attribute.
		if (head.getAttributes().length != 1)
			throw new IllegalArgumentException("The head Key must have 1 attribute");
		for (Key key : body) {
			if (key.getAttributes().length != 1)
				throw new IllegalArgumentException("Each body Key must have 1 attribute");
		}
		
		this.head = head;
		this.body = body;
	}

	public Key getHead() {
		return head;
	}

	public Key[] getBody() {
		return body;
	}
}
