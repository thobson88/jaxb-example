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
import io.jaxb.adapted.AdaptedAccessMethod;

public class AdaptedAccessMethodTest {

	@Test
	public void testMarshall() {

		Relation relation = new Relation("relation", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedAccessMethod target = new AdaptedAccessMethod(am);
		
		Writer writer = new StringWriter();

		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAccessMethod.class);
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
						"<accessMethod name=\"accessMethod\">\n" +
						"    <attribute name=\"b\" type=\"java.lang.Integer\"/>\n" +
						"    <relation name=\"relation\">\n" +
						"        <attribute name=\"a\" type=\"java.lang.String\"/>\n" +
						"        <attribute name=\"b\" type=\"java.lang.Integer\"/>\n" +
						"    </relation>\n" +
						"</accessMethod>\n";

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRoundTrip() {

		Relation relation = new Relation("relation", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedAccessMethod target = new AdaptedAccessMethod(am);
		
		Writer writer = new StringWriter();

		AdaptedAccessMethod unmarshalled = null;
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAccessMethod.class);
			Marshaller m = context.createMarshaller();
			Unmarshaller u = context.createUnmarshaller();

			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(target, writer);
			String str = writer.toString();
			unmarshalled = (AdaptedAccessMethod) u.unmarshal(new StringReader(str));

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("accessMethod", unmarshalled.getName());

		// In contrast to the *abridged* case, here we *can* call toAccessMethod.
		
		Assert.assertEquals(am.getName(), unmarshalled.toAccessMethod().getName());
		Assert.assertArrayEquals(am.getAttributes(), unmarshalled.toAccessMethod().getAttributes());
		
		// Check that the Relation is associated with the unserialised AccessMethod.
		Assert.assertEquals(relation.getName(), unmarshalled.toAccessMethod().getRelation().getName());
		Assert.assertArrayEquals(relation.getAttributes(), unmarshalled.toAccessMethod().getRelation().getAttributes());

		// Check the circular references (from AccessMethod to Relation and back again).
		Assert.assertEquals(1, unmarshalled.toAccessMethod().getRelation().getAccessMethods().size());
		Assert.assertEquals(am.getName(), unmarshalled.toAccessMethod().getRelation().getAccessMethods().get(0).getName());
		Assert.assertArrayEquals(am.getAttributes(), unmarshalled.toAccessMethod().getRelation().getAccessMethods().get(0).getAttributes());
	}
}
