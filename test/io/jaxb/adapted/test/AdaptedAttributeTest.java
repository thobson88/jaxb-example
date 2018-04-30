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
import io.jaxb.adapted.AdaptedAttribute;

public class AdaptedAttributeTest {

	@Test
	public void testMarshall() {
		
		AdaptedAttribute target = new AdaptedAttribute(new Attribute(String.class, "a"));
		Writer writer = new StringWriter();
		
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAttribute.class);
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
				"<attribute name=\"a\" type=\"java.lang.String\"/>\n";
		
		Assert.assertEquals(expected, actual);
	}

	
	@Test
	public void testRoundTrip() {
		
		AdaptedAttribute target = new AdaptedAttribute(new Attribute(String.class, "a"));
		Writer writer = new StringWriter();

		AdaptedAttribute unmarshalled = null;
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedAttribute.class);
			Marshaller m = context.createMarshaller();
			Unmarshaller u = context.createUnmarshaller();
			
			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			m.marshal(target, writer);
			String str = writer.toString();
			unmarshalled = (AdaptedAttribute) u.unmarshal(new StringReader(str));
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals("a", unmarshalled.getName());
		Assert.assertEquals("java.lang.String", unmarshalled.getTypeName());
		Assert.assertEquals(String.class, unmarshalled.getType());
	}

}
