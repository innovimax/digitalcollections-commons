package org.mdz.dzp.commons.xml.xpath;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface XPathVariable {
  String name() default "[unnamed]";
  String[] paths();
}