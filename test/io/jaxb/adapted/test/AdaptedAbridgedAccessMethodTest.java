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
import io.jaxb.adapted.AdaptedAbridgedAccessMethod;

public class AdaptedAbridgedAccessMethodTest {

	@Test
	public void testMarshall() {
		
		Relation relation = new Relation("relation", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedAbridgedAccessMethod target = new AdaptedAbridgedAccessMethod(am);
		
		Writer writer = new StringWriter();

		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAbridgedAccessMethod.class);
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
						"</accessMethod>\n";

		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRoundTrip() {

		Attribute[] attributes = new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") };
		Relation relation = new Relation("relation", attributes);
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relation);
		relation.addAccessMethod(am);
		
		AdaptedAbridgedAccessMethod target = new AdaptedAbridgedAccessMethod(am);
		
		Writer writer = new StringWriter();

		AdaptedAbridgedAccessMethod unmarshalled = null;
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAbridgedAccessMethod.class);
			Marshaller m = context.createMarshaller();
			Unmarshaller u = context.createUnmarshaller();

			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(target, writer);
			String str = writer.toString();
			unmarshalled = (AdaptedAbridgedAccessMethod) u.unmarshal(new StringReader(str));

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		// Note that we cannot call unmarshalled.toAccessMethod() because unmarshalled
		// is *abridged*, so there's no relation object (which is required in the 
		// AccessMethod constructor). Instead we can only test the fields in the
		// adapted, abridged unmarshalled object.
		Assert.assertEquals("accessMethod", unmarshalled.getName());
		Assert.assertEquals(1, unmarshalled.getAttributes().length);
		Assert.assertEquals("b", unmarshalled.getAttributes()[0].getName());
		Assert.assertEquals(Integer.class, unmarshalled.getAttributes()[0].getType());
		
		// Note that the toAccessMethod method in AdaptedAbridgedAccessMethod, which 
		// takes a Relation instance as an argument, is package private (so cannot
		// be called here) deliberately to avoid the possibility of repeated calling
		// passing the same Relation instance, which would lead to that Relation having
		// multiple associated AccessMethod all of which are "the same". Internally,
		// however, that method is required in the toRelation method in AdaptedRelation.
	}
}
