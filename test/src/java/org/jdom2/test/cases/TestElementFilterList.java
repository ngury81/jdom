package org.jdom2.test.cases;

import java.util.List;

import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;
import org.jdom2.test.util.AbstractTestList;
import org.junit.Before;

public class TestElementFilterList extends AbstractTestList<Element> {
	
	private static final Element base = new Element("dummy");
	private static final Element parent = new Element("parent").addContent(base);


	public TestElementFilterList() {
		super(Element.class, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Element> buildEmptyList() {
		base.getContent().clear();
		return (List<Element>)base.getContent(new ElementFilter());
	}

	@Override
	public Element[] buildSampleContent() {
		return new Element[]{ new Element("zero"), 
				new Element("one"), new Element("two"), 
				new Element("three"), new Element("four"),
				new Element("five"), new Element("six")};
	}

	@Override
	public Object[] buildIllegalClassContent() {
		Object[] ret = new Object[] {
				new Text("Hello"),
				new Comment("Hello!")
		};
		return ret;
	}
	
	@Override
	public Element[] buildIllegalArgumentContent() {
		return new Element[]{base, parent};
	};
	
	@Before
	public void detatchAll () {
		// make sure all content is detatched before each test.
		for (Content c : buildSampleContent()) {
			c.detach();
		}
	}
	
}
