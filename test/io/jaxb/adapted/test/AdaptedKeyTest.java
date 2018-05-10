package io.jaxb.adapted.test;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Assert;
import org.junit.Test;

import db.Attribute;
import db.Key;
import db.Relation;
import io.jaxb.adapted.AdaptedKey;

public class AdaptedKeyTest {

	@Test
	public void testMarshall() {
		
		Relation relationA = new Relation("relationA", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });

		Key key = new Key(relationA, new Attribute[] { relationA.getAttributes()[0], relationA.getAttributes()[1] });
		
		AdaptedKey target = new AdaptedKey(key);
		Writer writer = new StringWriter();
		
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedKey.class);
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
				"<key relation=\"relationA\">\n" + 
				"    <attribute>a</attribute>\n" + 
				"    <attribute>b</attribute>\n" + 
				"</key>\n";
		
		Assert.assertEquals(expected, actual);
	}}
