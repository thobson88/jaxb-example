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

import db.Attribute;
import io.jaxb.adapted.AdaptedRelation;

public class AdaptedRelationTest {

	@Test
	public void testMarshall() {

		AdaptedRelation target = new AdaptedRelation("relation", 
				new Attribute[] { new Attribute(String.class, "a"),new Attribute(Integer.class, "b") });
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
						"</relation>\n";

		Assert.assertEquals(expected, actual);
	}

		@Test
		public void testRoundTrip() {
			
			Attribute[] attributes = new Attribute[] { new Attribute(String.class, "a"),new Attribute(Integer.class, "b") };
			AdaptedRelation target = new AdaptedRelation("relation", attributes);
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
		}
}
