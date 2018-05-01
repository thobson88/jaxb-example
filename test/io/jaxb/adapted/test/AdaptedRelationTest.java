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
import io.jaxb.adapted.AdaptedRelation;

public class AdaptedRelationTest {

	@Test
	public void testMarshall() {

		Relation relation = new Relation("relation", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedRelation target = new AdaptedRelation(relation);
		
		Writer writer = new StringWriter();

		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedRelation.class);
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
						"    <accessMethod name=\"accessMethod\">\n" + 
						"        <attribute name=\"b\" type=\"java.lang.Integer\"/>\n" +
						"    </accessMethod>\n" +
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
		
		AdaptedRelation target = new AdaptedRelation(relation);

		Assert.assertFalse(target.toRelation().getAccessMethods().isEmpty());
		
		Writer writer = new StringWriter();

		AdaptedRelation unmarshalled = null;
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedRelation.class);
			Marshaller m = context.createMarshaller();
			Unmarshaller u = context.createUnmarshaller();

			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(target, writer);
			String str = writer.toString();
			unmarshalled = (AdaptedRelation) u.unmarshal(new StringReader(str));

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("relation", unmarshalled.getName());

		// Call toRelation() and then getAttributes(), since getAttributes 
		// on the unmarshalled AdaptedRelation returns an AdaptedAttribute[],
		// not an Attribute[].
		Assert.assertArrayEquals(attributes, unmarshalled.toRelation().getAttributes());
		
		// The unmarshalled Relation has a reference to the associated AccessMethod,
		// because serialisation was *unabridged*.
		Assert.assertFalse(unmarshalled.toRelation().getAccessMethods().isEmpty());
		Assert.assertEquals("accessMethod", unmarshalled.toRelation().getAccessMethods().get(0).getName());
		Assert.assertArrayEquals(new Attribute[] { new Attribute(Integer.class, "b") }, 
				unmarshalled.toRelation().getAccessMethods().get(0).getAttributes());
		
		// Check the circular reference from the access method back to the relation.
		Relation deserialisedRelation = unmarshalled.toRelation(); 
		Assert.assertEquals(deserialisedRelation, deserialisedRelation.getAccessMethods().get(0).getRelation());
	}
	
	// MOST IMP TODO: test with multiple AccessMethods associated before serialisation.
}
