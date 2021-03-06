package de.digitalcollections.commons.xml.xpath;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class XPathMapperTest {

  private TestMapper mapper;

  private interface TestMapper {

    String TEI_NS = "http://www.tei-c.org/ns/1.0";
    String BIBLSTRUCT_PATH = "/tei:TEI/tei:teiHeader/tei:fileDesc/tei:sourceDesc/tei:listBibl/tei:biblStruct";

    @XPathBinding(
            defaultNamespace = TEI_NS,
            valueTemplate = "{author}",
            multiLanguage = true,
            variables = {
              @XPathVariable(name = "author", paths = {BIBLSTRUCT_PATH + "/tei:monogr/tei:author/tei:persName/tei:name"})
            }
    )
    Map<Locale, String> getAuthor() throws XPathMappingException;

    @XPathBinding(
            defaultNamespace = TEI_NS,
            valueTemplate = "{title}<: {subtitle}>< [. {partNumber}<, {partTitle}>]>",
            variables = {
              @XPathVariable(name = "title", paths = {BIBLSTRUCT_PATH + "/tei:monogr/tei:title[@type=\"main\"]"}),
              @XPathVariable(name = "subtitle", paths = {BIBLSTRUCT_PATH + "/tei:monogr/tei:title[@type=\"sub\"]"}),
              @XPathVariable(name = "partNumber", paths = {BIBLSTRUCT_PATH + "/tei:series/tei:biblScope[@ana=\"#norm\"]"}),
              @XPathVariable(name = "partTitle", paths = {BIBLSTRUCT_PATH + "/tei:monogr/tei:title[@type=\"part\"]"})
            }
    )
    String getTitle() throws XPathMappingException;

    @XPathBinding(valueTemplate = "{broken}", variables = {})
    String broken() throws XPathMappingException;
  }

  @BeforeEach
  public void setUp() throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("bsbstruc.xml");
    Document doc = db.parse(is);
    this.mapper = XPathMapper.makeProxy(doc, TestMapper.class);
  }

  @Test
  public void testTemplateWithSingleVariable() throws Exception {
    assertThat(mapper.getAuthor().get(Locale.GERMAN)).isEqualTo("Kugelmann, Hans");
    assertThat(mapper.getAuthor().get(Locale.ENGLISH)).isEqualTo("Name, English");
  }

  @Test
  public void testTemplateWithContext() throws Exception {
    assertThat(mapper.getTitle()).isEqualTo("Ein Titel: Ein Untertitel");
  }

  @Test
  public void testMissingVariableThrows() throws Exception {
    assertThatThrownBy(mapper::broken).isInstanceOf(XPathMappingException.class).hasMessageContaining("Could not resolve variable");
  }
}
