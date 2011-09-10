/*--
 
Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>.  For more information on the
 JDOM Project, please see <http://www.jdom.org/>.

 */


package org.jdom2.test.cases.input;

/**
 * Tests of SAXBuilder functionality.  Since most of these methods are tested in other parts
 * of the test suite, many tests are not filled.
 * 
 * @author Philip Nelson
 * @version 0.5
 */
import java.util.*;
import java.io.*;

import javax.xml.XMLConstants;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import static org.junit.Assert.*;


public final class TestSAXBuilder {
	
	private static final String testxml = "<?xml version=\"1.0\"?><root/>";
	private static final String testpattern = "\\s*<\\?xml\\s+version=\"1.0\"\\s+encoding=\"UTF-8\"\\s*\\?>\\s*<root\\s*/>\\s*";

	private class MySAXBuilder extends SAXBuilder {
		public MySAXBuilder() {
			super();
		}
		
		public MySAXBuilder(String driver) {
			super(driver);
		}
		
		@Override
		public XMLReader createParser() throws JDOMException {
			return super.createParser();
		}
	}
	
	/**
	 * the directory where needed resource files will be kept
	 */
	private final String resourceDir = 
			ResourceBundle.getBundle("org.jdom2.test.Test")
				.getString("test.resourceRoot");

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        JUnitCore.runClasses(TestSAXBuilder.class);
    }	

	@Test
	public void testSAXBuilder() {
		SAXBuilder sb = new SAXBuilder();
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.getValidation());
		assertTrue(sb.getExpandEntities());		
	}

	@Test
	public void testSAXBuilderBooleanFalse() {
		SAXBuilder sb = new SAXBuilder(false);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.getValidation());
		assertTrue(sb.getExpandEntities());
	}

	@Test
	public void testSAXBuilderBooleanTrue() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
	}

	@Test
	public void testSAXBuilderString() {
		MySAXBuilder sb = new MySAXBuilder("org.apache.xerces.parsers.SAXParser");
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader instanceof org.apache.xerces.parsers.SAXParser);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}

		sb = new MySAXBuilder("com.sun.org.apache.xerces.internal.parsers.SAXParser");
		assertEquals("com.sun.org.apache.xerces.internal.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader instanceof com.sun.org.apache.xerces.internal.parsers.SAXParser);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@Test
	public void testSAXBuilderStringTrue() {
		SAXBuilder sb = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
	}

	@Test
	public void testSAXBuilderStringFalse() {
		SAXBuilder sb = new SAXBuilder("org.apache.xerces.parsers.SAXParser", false);
		assertEquals("org.apache.xerces.parsers.SAXParser", sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertFalse(sb.getValidation());
		assertTrue(sb.getExpandEntities());
	}

	@Test
	public void testGetFactory() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		assertTrue(sb.getFactory() instanceof DefaultJDOMFactory);
	}

	@Test
	public void testSetFactory() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		UncheckedJDOMFactory udf = new UncheckedJDOMFactory();
		assertTrue(sb.getFactory() instanceof DefaultJDOMFactory);
		sb.setFactory(udf);
		assertTrue(sb.getFactory() == udf);
	}

	@Test
	public void testSetValidation() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		
		sb.setValidation(false);
		assertFalse(sb.getValidation());
		
		sb.setValidation(true);
		assertTrue(sb.getValidation());

	}

	@Test
	public void testGetErrorHandler() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		
		ErrorHandler handler = new BuilderErrorHandler();
		
		sb.setErrorHandler(handler);
		assertTrue(handler == sb.getErrorHandler());		
	}

	@Test
	public void testGetEntityResolver() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());

		EntityResolver er = new EntityResolver() {
			@Override
			public InputSource resolveEntity(String arg0, String arg1) {
				return null;
			}
		};
		
		sb.setEntityResolver(er);
		assertTrue(er == sb.getEntityResolver());		
	}

	@Test
	public void testGetDTDHandler() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());

		DTDHandler dtd = new DTDHandler() {
			@Override
			public void notationDecl(String arg0, String arg1, String arg2)
					throws SAXException {
			}
			@Override
			public void unparsedEntityDecl(String arg0, String arg1,
					String arg2, String arg3) throws SAXException {
			}
		};
		
		sb.setDTDHandler(dtd);
		assertTrue(dtd == sb.getDTDHandler());		
	}

	@Test
	public void testXMLFilter() {
		MySAXBuilder sb = new MySAXBuilder();
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getExpandEntities());

		XMLFilter filter = new XMLFilterImpl() {
			@Override
			public void startElement(String arg0, String arg1, String arg2,
					Attributes arg3) throws SAXException {
				super.startElement(arg0, "f" + arg1, arg2, arg3);
			}
			@Override
			public void endElement(String arg0, String arg1, String arg2) throws SAXException {
				super.endElement(arg0, "f" + arg1, arg2);
			}
		};
		
		XMLFilter gilter = new XMLFilterImpl() {
			@Override
			public void startElement(String arg0, String arg1, String arg2,
					Attributes arg3) throws SAXException {
				super.startElement(arg0, "g" + arg1, arg2, arg3);
			}
			@Override
			public void endElement(String arg0, String arg1, String arg2) throws SAXException {
				super.endElement(arg0, "g" + arg1, arg2);
			}
		};
		
		filter.setParent(gilter);
		sb.setXMLFilter(filter);
		assertTrue(filter == sb.getXMLFilter());
		
		try {
			Document doc = sb.build(new CharArrayReader(testxml.toCharArray()));
			assertTrue(doc.hasRootElement());
			assertEquals("fgroot", doc.getRootElement().getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not parse XML " + testxml + ": " + e.getMessage());
		}
		
	}

	@Test
	public void testGetIgnoringElementContentWhitespace() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		sb.setIgnoringElementContentWhitespace(true);
		assertTrue(sb.getIgnoringElementContentWhitespace());		
		sb.setIgnoringElementContentWhitespace(false);
		assertFalse(sb.getIgnoringElementContentWhitespace());		
	}

	@Test
	public void testGetIgnoringBoundaryWhitespace() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		sb.setIgnoringBoundaryWhitespace(true);
		assertTrue(sb.getIgnoringBoundaryWhitespace());		
		sb.setIgnoringBoundaryWhitespace(false);
		assertFalse(sb.getIgnoringBoundaryWhitespace());		
	}

	@Test
	public void testGetReuseParser() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		
		sb.setReuseParser(true);
		assertTrue(sb.getReuseParser());		
		sb.setReuseParser(false);
		assertFalse(sb.getReuseParser());		
	}

	@Test
	public void testCreateParser() {
		MySAXBuilder sb = new MySAXBuilder();
		try {
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader instanceof XMLReader);
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@Test
	public void testSetFeature() {
		String feature = XMLConstants.FEATURE_SECURE_PROCESSING;
		MySAXBuilder sb = new MySAXBuilder();
		try {
			sb.setFeature(feature, true);
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader instanceof XMLReader);
			assertTrue(reader.getFeature(feature));
			sb.setFeature(feature, false);
			reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader instanceof XMLReader);
			assertFalse(reader.getFeature(feature));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

	@Test
	public void testSetProperty() {
		LexicalHandler lh = new LexicalHandler() {
			@Override
			public void startEntity(String arg0) throws SAXException {
			}
			@Override
			public void startDTD(String arg0, String arg1, String arg2)
					throws SAXException {
			}
			@Override
			public void startCDATA() throws SAXException {
			}
			@Override
			public void endEntity(String arg0) throws SAXException {
			}
			
			@Override
			public void endDTD() throws SAXException {
			}
			
			@Override
			public void endCDATA() throws SAXException {
			}
			
			@Override
			public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
			}
		};
		
		MySAXBuilder sb = new MySAXBuilder();
		String propname = "http://xml.org/sax/properties/lexical-handler";
		try {
			sb.setProperty(propname, lh);
			XMLReader reader = sb.createParser();
			assertNotNull(reader);
			assertTrue(reader instanceof XMLReader);
			assertTrue(lh == reader.getProperty(propname));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Could not create parser: " + e.getMessage());
		}
	}

    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    @Test
    public void test_TCM__void_setExpandEntities_boolean() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        File file = new File(resourceDir + "/SAXBuilderTestEntity.xml");

        builder.setExpandEntities(true);
        assertTrue(builder.getExpandEntities());
        
        Document doc = builder.build(file);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("simple entity") == 0);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("another simple entity") > 1);        	

        //test that entity declaration appears in doctype
        //and EntityRef is created in content with internal entity
        builder.setExpandEntities(false);
        assertFalse(builder.getExpandEntities());

        doc = builder.build(file);
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("simple entity") > 1));
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("another simple entity") > 1));        	
		List content = doc.getRootElement().getContent();
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(0) instanceof EntityRef);
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(2) instanceof EntityRef);
		
        //test entity expansion on external entity
        file = new File(resourceDir + "/SAXBuilderTestEntity2.xml");

        builder.setExpandEntities(true);
        assertTrue(builder.getExpandEntities());

        doc = builder.build(file);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("simple entity") == 0);
        assertTrue("didn't get entity text", doc.getRootElement().getText().indexOf("another simple entity") > 1);        	

        //test that entity declaration appears in doctype
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        assertFalse(builder.getExpandEntities());
        doc = builder.build(file);
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("simple entity") > 1));
        assertTrue("got entity text", ! (doc.getRootElement().getText().indexOf("another simple entity") > 1));        	
		content = doc.getRootElement().getContent();
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(0) instanceof EntityRef);
		assertTrue("didn't get EntityRef for unexpanded entities",
			content.get(2) instanceof EntityRef);



    }

    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    @Test
    public void test_TCU__DTDComments() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on external entity
        File file = new File(resourceDir + "/SAXBuilderTestDecl.xml");

       //test that entity declaration appears in doctype
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        Document doc = builder.build(file);

        assertTrue("didnt' get internal subset comments correctly", doc.getDocType().getInternalSubset().indexOf("foo") > 0);
		//assertTrue("didn't get EntityRef for unexpanded attribute entities",
		//	doc.getRootElement().getAttribute("test").getValue().indexOf("&simple") == 0);
		


    }

    /**
     * Test that when setExpandEntities is true, enties are
     * always expanded and when false, entities declarations
     * are added to the DocType
     */
    @Test
    public void test_TCU__InternalAndExternalEntities() throws JDOMException, IOException {
        //test entity exansion on internal entity

        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on internal and external entity
        File file = new File(resourceDir + "/SAXBuilderTestIntExtEntity.xml");

        builder.setExpandEntities(true);
        Document doc = builder.build(file);
        assertTrue("didn't get internal entity text", doc.getRootElement().getText().indexOf("internal") >= 0);
        assertTrue("didn't get external entity text", doc.getRootElement().getText().indexOf("external") > 0);
		//the internal subset should be empty since entity expansion is off
		assertTrue("invalid characters in internal subset", doc.getDocType().getInternalSubset().length() == 0);
		assertTrue("incorrectly got entity declaration in internal subset for internal entity", 
			doc.getDocType().getInternalSubset().indexOf("internal") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("external") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("ldquo") < 0);
        //test that local entity declaration appears in internal subset
        //and EntityRef is created in content with external entity
        builder.setExpandEntities(false);
        doc = builder.build(file);
    	
		EntityRef internal = (EntityRef)doc.getRootElement().getContent().get(0);
		EntityRef external = (EntityRef)doc.getRootElement().getContent().get(6);
		assertNotNull("didn't get EntityRef for unexpanded internal entity", internal);
		assertNotNull("didn't get EntityRef for unexpanded external entity", external);
		assertTrue("didn't get local entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("internal") > 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("external") < 0);
		assertTrue("incorrectly got external entity declaration in internal subset", 
			doc.getDocType().getInternalSubset().indexOf("ldquo") < 0);
    }
    
    @Ignore
    @Test
    public void test_TCU__InternalSubset() throws JDOMException, IOException {
    
        SAXBuilder builder = new SAXBuilder();
        //test entity expansion on internal subset
        File file = new File(resourceDir + "/SAXBuilderTestEntity.xml");

        builder.setExpandEntities(true);
        Document doc = builder.build(file);
        String subset = doc.getDocType().getInternalSubset();
        assertEquals("didn't get correct internal subset when expand entities was on"
            , "  <!NOTATION n1 SYSTEM \"http://www.w3.org/\">\n  <!NOTATION n2 SYSTEM \"http://www.w3.org/\">\n  <!ENTITY anotation SYSTEM \"http://www.foo.org/image.gif\" NDATA n1>\n", 
            subset);
        //now do it with expansion off
        builder.setExpandEntities(false);
        doc = builder.build(file);
        String subset2 = doc.getDocType().getInternalSubset();
        final String expect = "<!NOTATION n2 SYSTEM \"http://www.w3.org/\">\n  <!ENTITY anotation SYSTEM \"http://www.foo.org/image.gif\" NDATA n1>\n";
        if (!expect.equals(subset2)) {
        	fail("didn't get correct internal subset when expand entities was off.\n" +
        			"Expect: " + expect + "\n" +
        			"Got:    " + subset2);
        }
    }

	@Test
	public void testSetFastReconfigure() {
		SAXBuilder sb = new SAXBuilder(true);
		assertNull(sb.getDriverClass());
		assertTrue(sb.getEntityResolver() == null);
		assertTrue(sb.getErrorHandler() == null);
		assertTrue(sb.getDTDHandler() == null);
		assertTrue(sb.getXMLFilter() == null);
		assertTrue(sb.getValidation());
		assertTrue(sb.getExpandEntities());
		
		sb.setFastReconfigure(true);
		
		// TODO - Now what?
	}
	
	private void assertXMLMatches(String baseuri, Document doc) {
		XMLOutputter out = new XMLOutputter(Format.getCompactFormat());
		try {
			CharArrayWriter caw = new CharArrayWriter();
			out.output(doc, caw);
			String output = caw.toString();
			if (!output.matches(testpattern)) {
				fail ("Failed to match output:\n  " + output + "\nwith pattern:\n  " + testpattern);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to write Document " + doc + " to CharArrayWriter.");
		}
		if (baseuri == null) {
			assertNull(doc.getBaseURI());
		} else {
			if (!baseuri.equals(doc.getBaseURI())) {
				String moduri = baseuri.replaceFirst(":/", ":///");
				if (!moduri.equals(doc.getBaseURI())) {
					fail("Neither " + baseuri + " nor " + moduri + " matches base URI " + doc.getBaseURI());
				}
			}
		}
	}

	@Test
	public void testBuildInputSource() {
		InputSource is = new InputSource(new CharArrayReader(testxml.toCharArray()));
		try {
			assertXMLMatches(null, new SAXBuilder().build(is));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildInputStream() {
		InputStream is = new ByteArrayInputStream(testxml.getBytes());
		try {
			assertXMLMatches(null, new SAXBuilder().build(is));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildFile() {
		File tmp = null;
		try {
			tmp = File.createTempFile("tst", ".xml");
			tmp.deleteOnExit();
			FileWriter fw = new FileWriter(tmp);
			fw.write(testxml.toCharArray());
			fw.flush();
			fw.close();
			assertXMLMatches(tmp.toURI().toString(), new SAXBuilder().build(tmp));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to write/parse document to file '" + tmp + "': " + e.getMessage());
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

	@Test
	public void testBuildURL() {
		File tmp = null;
		try {
			tmp = File.createTempFile("tst", ".xml");
			tmp.deleteOnExit();
			FileWriter fw = new FileWriter(tmp);
			fw.write(testxml.toCharArray());
			fw.flush();
			fw.close();
			assertXMLMatches(tmp.toURI().toString(), new SAXBuilder().build(tmp.toURI().toURL()));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to write/parse document to file '" + tmp + "': " + e.getMessage());
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

	@Test
	public void testBuildInputStreamString() {
		InputStream is = new ByteArrayInputStream(testxml.getBytes());
		try {
			assertXMLMatches(new File("baseID").toURI().toURL().toExternalForm(), new SAXBuilder().build(is, "baseID"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildReader() {
		Reader is = new CharArrayReader(testxml.toCharArray());
		try {
			assertXMLMatches(null, new SAXBuilder().build(is));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildReaderString() {
		Reader is = new CharArrayReader(testxml.toCharArray());
		try {
			assertXMLMatches(new File("baseID").getCanonicalFile().toURI().toURL().toString(), new SAXBuilder().build(is, "baseID"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to parse document: " + e.getMessage());
		}
	}

	@Test
	public void testBuildString() {
		File tmp = null;
		try {
			tmp = File.createTempFile("tst", ".xml");
			tmp.deleteOnExit();
			FileWriter fw = new FileWriter(tmp);
			fw.write(testxml.toCharArray());
			fw.flush();
			fw.close();
			assertXMLMatches(tmp.getCanonicalFile().toURI().toURL().toString(), new SAXBuilder().build(tmp.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to write/parse document to file '" + tmp + "': " + e.getMessage());
		} finally {
			if (tmp != null) {
				tmp.delete();
			}
		}
	}

}
