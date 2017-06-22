package de.digitalcollections.commons.xml.xpath;

import javax.xml.xpath.XPathExpressionException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XPathExpressionCacheTest {

  private static final String EXPRESSION = "//child::Book[count(./page)<=100][count(./page)>=10]";

  private XPathExpressionCache expressionCache;

  @Before
  public void setUp() {
    expressionCache = new XPathExpressionCache();
  }

  @Test
  public void shouldCacheExpressions() throws XPathExpressionException {
    assertThat(expressionCache.get(EXPRESSION)).isEqualTo(expressionCache.get(EXPRESSION));
  }

  @Test
  public void shouldCacheExpressionsOnlyOnce() throws XPathExpressionException {
    expressionCache.get(EXPRESSION);
    expressionCache.get(EXPRESSION);
    assertThat(expressionCache.getSize()).isEqualTo(1);
  }
}
