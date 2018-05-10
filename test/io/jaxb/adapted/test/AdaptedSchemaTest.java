package io.jaxb.adapted.test;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Assert;
import org.junit.Test;

import db.AccessMethod;
import db.Attribute;
import db.Dependency;
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

		Relation relationC = new Relation("relationC", 
				new Attribute[] { new Attribute(String.class, "j"), new Attribute(Integer.class, "k") });

		Key head1 = new Key(relationA, new Attribute[] { relationA.getAttributes()[0] });
		Key[] body1 = new Key[] {
				new Key(relationB, new Attribute[] { relationB.getAttributes()[0] }),
				new Key(relationC, new Attribute[] { relationC.getAttributes()[0] }),
		};

		Key head2 = new Key(relationB, new Attribute[] { relationB.getAttributes()[1] });
		Key[] body2 = new Key[] {
				new Key(relationC, new Attribute[] { relationC.getAttributes()[1] }),
		};

		Dependency[] dependencies = new Dependency[] {
				new Dependency(head1, body1),
				new Dependency(head2, body2)
		};
		
		Schema schema = new Schema(new Relation[] { relationA, relationB, relationC }, dependencies );
		
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
						"    <relations>\n" +
						"        <relation name=\"relationA\">\n" +
						"            <attribute name=\"a\" type=\"java.lang.String\"/>\n" +
						"            <attribute name=\"b\" type=\"java.lang.Integer\"/>\n" +
						"        </relation>\n" +
						"        <relation name=\"relationB\">\n" +
						"            <attribute name=\"x\" type=\"java.lang.String\"/>\n" +
						"            <attribute name=\"y\" type=\"java.lang.Integer\"/>\n" +
						"        </relation>\n" +
						"        <relation name=\"relationC\">\n" +
						"            <attribute name=\"j\" type=\"java.lang.String\"/>\n" +
						"            <attribute name=\"k\" type=\"java.lang.Integer\"/>\n" +
						"        </relation>\n" +
						"    </relations>\n" +
						"    <dependencies>\n" +
						"        <dependency>\n" +
						"            <head>\n" +
						"                <key relation=\"relationA\">\n" +
						"                    <attribute>a</attribute>\n" +
						"                </key>\n" +
						"            </head>\n" +
						"            <body>\n" + 
						"                <key relation=\"relationB\">\n" +
						"                    <attribute>x</attribute>\n" +
						"                </key>\n" +
						"                <key relation=\"relationC\">\n" +
						"                    <attribute>j</attribute>\n" +
						"                </key>\n" +
						"            </body>\n" +
						"        </dependency>\n" +
						"        <dependency>\n" +
						"            <head>\n" +
						"                <key relation=\"relationB\">\n" +
						"                    <attribute>y</attribute>\n" +
						"                </key>\n" +
						"            </head>\n" +
						"            <body>\n" + 
						"                <key relation=\"relationC\">\n" +
						"                    <attribute>k</attribute>\n" +
						"                </key>\n" +
						"            </body>\n" +
						"        </dependency>\n" +
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

		Relation relationC = new Relation("relationC", 
				new Attribute[] { new Attribute(String.class, "j"), new Attribute(Integer.class, "k") });

		Key head1 = new Key(relationA, new Attribute[] { relationA.getAttributes()[0] });
		Key[] body1 = new Key[] {
				new Key(relationB, new Attribute[] { relationB.getAttributes()[0] }),
				new Key(relationC, new Attribute[] { relationC.getAttributes()[0] }),
		};

		Key head2 = new Key(relationB, new Attribute[] { relationB.getAttributes()[1] });
		Key[] body2 = new Key[] {
				new Key(relationC, new Attribute[] { relationC.getAttributes()[1] }),
		};

		Dependency[] dependencies = new Dependency[] {
				new Dependency(head1, body1),
				new Dependency(head2, body2)
		};
		
		Schema schema = new Schema(new Relation[] { relationA, relationB, relationC }, dependencies );
		
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
						"    \"relations\": {\"relation\": [\n" +
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
						"        },\n" +
						"        {\n" +
						"            \"name\": \"relationC\",\n" +
						"            \"attribute\": [\n" +
						"                {\n" +
						"                    \"name\": \"j\",\n" +
						"                    \"type\": \"java.lang.String\"\n" +
						"                },\n" +
						"                {\n" +
						"                    \"name\": \"k\",\n" +
						"                    \"type\": \"java.lang.Integer\"\n" +
						"                }\n" +
						"            ]\n" +
						"        }\n" +
						"    ]},\n" +
					    "    \"dependencies\": {\"dependency\": [\n" +
					    "        {\n" +
					    "            \"head\": {\"key\": {\n" +
					    "                \"attribute\": \"a\",\n" +
					    "                \"relation\": \"relationA\"\n" +
					    "            }},\n" +
					    "            \"body\": {\"key\": [\n" +
					    "                {\n" +
					    "                    \"attribute\": \"x\",\n" +
					    "                    \"relation\": \"relationB\"\n" +
					    "                },\n" +
					    "                {\n" +
					    "                    \"attribute\": \"j\",\n" +
					    "                    \"relation\": \"relationC\"\n" +
					    "                }\n" +
					    "            ]}\n" +
					    "        },\n" +
					    "        {\n" +
					    "            \"head\": {\"key\": {\n" +
					    "                \"attribute\": \"y\",\n" +
					    "                \"relation\": \"relationB\"\n" +
					    "            }},\n" +
					    "            \"body\": {\"key\": {\n" +
					    "                \"attribute\": \"k\",\n" +
					    "                \"relation\": \"relationC\"\n" +
					    "            }}\n" +
					    "        }\n" +
					    "    ]}\n" +
					    "}}";

		// TODO: correct relation-attribute order in dependencies.

		Assert.assertEquals(expected, actual);
	}
}
