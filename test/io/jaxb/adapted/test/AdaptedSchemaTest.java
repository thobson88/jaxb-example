package io.jaxb.adapted.test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Assert;
import org.junit.Test;

import db.AccessMethod;
import db.Attribute;
import db.Key;
import db.Relation;
import db.Schema;
import io.jaxb.adapted.AdaptedSchema;

public class AdaptedSchemaTest {

	@Test
	public void testMarshall() {

		Relation relationA = new Relation("relationA", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relationA);
		relationA.addAccessMethod(am);

		Relation relationB = new Relation("relationB", 
				new Attribute[] { new Attribute(String.class, "x"), new Attribute(Integer.class, "y") });

		Key keyA = new Key(relationA, relationA.getAttributes()[0]);
		Key keyB = new Key(relationB, relationB.getAttributes()[0]);

		Map<Key, Key> dependencies = new HashMap<Key, Key>();
		dependencies.put(keyA, keyB);

		Schema schema = new Schema(new Relation[] { relationA, relationB }, dependencies);
		AdaptedSchema target = new AdaptedSchema(schema);

		Writer writer = new StringWriter();

		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedSchema.class);
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
						"<schema>\n" +
						"    <relation name=\"relationA\">\n" +
						"        <attribute name=\"a\" type=\"java.lang.String\"/>\n" +
						"        <attribute name=\"b\" type=\"java.lang.Integer\"/>\n" +
						"    </relation>\n" +
						"    <relation name=\"relationB\">\n" +
						"        <attribute name=\"x\" type=\"java.lang.String\"/>\n" +
						"        <attribute name=\"y\" type=\"java.lang.Integer\"/>\n" +
						"    </relation>\n" +
						"    <dependencies>\n" +
						"        <entry>\n" +
						"            <key>\n" +
						"                <relation>relationA</relation>\n" +
						"                <attribute>a</attribute>\n" +
						"            </key>\n" +
						"            <value>\n" +
						"                <relation>relationB</relation>\n" +
						"                <attribute>x</attribute>\n" +
						"            </value>\n" +
						"        </entry>\n" +
						"    </dependencies>\n" +
						"</schema>\n";

		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testMarshallToJson() {
		
		Relation relationA = new Relation("relationA", 
				new Attribute[] { new Attribute(String.class, "a"), new Attribute(Integer.class, "b") });
		AccessMethod am = new AccessMethod("accessMethod", 
				new Attribute[] { new Attribute(Integer.class, "b") }, relationA);
		relationA.addAccessMethod(am);

		Relation relationB = new Relation("relationB", 
				new Attribute[] { new Attribute(String.class, "x"), new Attribute(Integer.class, "y") });

		Key keyA = new Key(relationA, relationA.getAttributes()[0]);
		Key keyB = new Key(relationB, relationB.getAttributes()[0]);

		Map<Key, Key> dependencies = new HashMap<Key, Key>();
		dependencies.put(keyA, keyB);

		Schema schema = new Schema(new Relation[] { relationA, relationB }, dependencies);
		AdaptedSchema target = new AdaptedSchema(schema);

		Writer writer = new StringWriter();		
		try {
			JAXBContext context = JAXBContext.newInstance(AdaptedSchema.class);
			Marshaller m = context.createMarshaller();

			// output pretty printed
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			m.marshal(target, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		// To JSON.
		int PRETTY_PRINT_INDENT_FACTOR = 4;
        JSONObject xmlJSONObj = XML.toJSONObject(writer.toString());
        
        String actual = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        String expected = 
        	"{\"schema\": {\n" + 
        "    \"relation\": [\n" +
        "        {\n" +
        "            \"name\": \"relationA\",\n" +
        "            \"attribute\": [\n" +
        "                {\n" +
        "                    \"name\": \"a\",\n" +
        "                    \"type\": \"java.lang.String\"\n" +
        "                },\n" +
        "                {\n" +
        "                    \"name\": \"b\",\n" +
        "                    \"type\": \"java.lang.Integer\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"name\": \"relationB\",\n" +
        "            \"attribute\": [\n" +
        "                {\n" +
        "                    \"name\": \"x\",\n" +
        "                    \"type\": \"java.lang.String\"\n" +
        "                },\n" +
        "                {\n" +
        "                    \"name\": \"y\",\n" +
        "                    \"type\": \"java.lang.Integer\"\n" +
        "                }\n" +
        "            ]\n" +
        "        }\n" +
        "    ],\n" +
        "    \"dependencies\": {\"entry\": {\n" +
        "        \"value\": {\n" +
        "            \"attribute\": \"x\",\n" +
        "            \"relation\": \"relationB\"\n" +
        "        },\n" +
        "        \"key\": {\n" +
        "            \"attribute\": \"a\",\n" +
        "            \"relation\": \"relationA\"\n" +
        "        }\n" +
        "    }}\n" +
        "}}";
        
        // TODO: correct key-value order in dependencies.
        
        Assert.assertEquals(expected, actual);
	}
	
	
}
