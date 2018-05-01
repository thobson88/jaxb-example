package io.jaxb.adapted.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

import db.AccessMethod;
import db.Attribute;
import db.Relation;
import io.jaxb.adapted.AdaptedAbridgedRelation;

public class AdaptedAbridgedRelationTest {

	@Test
	public void testMarshall() {
		
		Relation relation = new Relation("relation", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedAbridgedRelation target = new AdaptedAbridgedRelation(relation);
		
		Writer writer = new StringWriter();

		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAbridgedRelation.class);
			Marshaller m = context.createMarshaller();

			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(target, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		String actual = writer.toString();
		String expected = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
						"<relation name=\"relation\">\n" +
						"    <attribute name=\"a\" type=\"java.lang.String\"/>\n" +
						"    <attribute name=\"b\" type=\"java.lang.Integer\"/>\n" +
						"</relation>\n";

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRoundTrip() {

		Attribute[] attributes = new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") };
		Relation relation = new Relation("relation", attributes);
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedAbridgedRelation target = new AdaptedAbridgedRelation(relation);
		
		// The Relation has an associated AccessMethod, but the adapted, *abridged*
		// object does not (since access methods are omitted in the abridged serialisation).
		Assert.assertFalse(relation.getAccessMethods().isEmpty());
		Assert.assertTrue(target.toRelation().getAccessMethods().isEmpty());

		Writer writer = new StringWriter();

		AdaptedAbridgedRelation unmarshalled = null;
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAbridgedRelation.class);
			Marshaller m = context.createMarshaller();
			Unmarshaller u = context.createUnmarshaller();

			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(target, writer);
			String str = writer.toString();
			unmarshalled = (AdaptedAbridgedRelation) u.unmarshal(new StringReader(str));

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("relation", unmarshalled.getName());

		// Here we can call toRelation on the *abridged* adapted object (i.e. unmarshalled)
		// because a Relation may be instantiated without an AccessMethod (this is in 
		// contrast to the case of an unmarshalled abridged access method). However, the 
		// resulting Relation will have "lost" it's access method(s), of course. 
		
		// Call toRelation() and then getAttributes(), since getAttributes 
		// on the unmarshalled AdaptedAbridgedRelation returns an AdaptedAttribute[],
		// not an Attribute[].
		Assert.assertArrayEquals(attributes, unmarshalled.toRelation().getAttributes());
		
		// The unmarshalled Relation no longer has an associated AccessMethod, 
		// because serialisation was *abridged*. 
		Assert.assertTrue(unmarshalled.toRelation().getAccessMethods().isEmpty());
	}
}
