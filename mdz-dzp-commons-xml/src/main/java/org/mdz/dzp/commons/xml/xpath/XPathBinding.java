package org.mdz.dzp.commons.xml.xpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XPathBinding {
  String valueTemplate() default "";
  String defaultNamespace() default "";
  boolean multiValued() default false;
  XPathVariable[] variables();
}