package de.digitalcollections.commons.file.backend.impl;

import de.digitalcollections.commons.file.backend.api.FileResourceRepository;
import de.digitalcollections.commons.file.config.SpringConfigCommonsFile;
import de.digitalcollections.model.api.identifiable.resource.FileResource;
import de.digitalcollections.model.api.identifiable.resource.MimeType;
import de.digitalcollections.model.api.identifiable.resource.enums.FileResourcePersistenceType;
import static de.digitalcollections.model.api.identifiable.resource.enums.FileResourcePersistenceType.RESOLVED;
import de.digitalcollections.model.api.identifiable.resource.exceptions.ResourceIOException;
import java.net.URI;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfigCommonsFile.class})
public class FileResourceRepositoryImplTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileResourceRepositoryImplTest.class);

  @Autowired
  private FileResourceRepository resourceRepository;

  public FileResourceRepositoryImplTest() {
  }

  @BeforeClass
  public static void setUpClass() {
    System.setProperty("spring.profiles.active", "TEST");
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testReadXMLDocument() throws ResourceIOException {
    String key = "snafu";
    FileResourcePersistenceType resourcePersistenceType = RESOLVED;
    Document document = resourceRepository.getDocument(key, resourcePersistenceType);
    Node rootElement = document.getElementsByTagName("rootElement").item(0);
    String textContent = rootElement.getTextContent();
    Assert.assertEquals("SNAFU", textContent);
  }

  /**
   * Test of create method, of class ResourceRepositoryImpl.
   */
  @Test
  public void testCreate() throws Exception {
    System.out.println("create");

    // test managed
    String key = "a30cf362-5992-4f5a-8de0-61938134e721";
    FileResourcePersistenceType resourcePersistenceType = FileResourcePersistenceType.MANAGED;
    FileResource resource = resourceRepository.create(key, resourcePersistenceType, "xml");
    URI expResult = URI.create("/src/test/resources/repository/dico/a30c/f362/5992/4f5a/8de0/6193/8134/e721/a30cf362-5992-4f5a-8de0-61938134e721.xml");
    URI result = resource.getUri();
    Assert.assertEquals(expResult, result);

    // test resolved
    key = "bsb00001000";
    resourcePersistenceType = RESOLVED;
    resource = resourceRepository.create(key, resourcePersistenceType, "xml");
    expResult = URI.create("http://rest.digitale-sammlungen.de/data/bsb00001000.xml");
    result = resource.getUri();
    Assert.assertEquals(expResult, result);
    Assert.assertFalse(resource.isReadonly());

    // test referenced
    key = "bsb00001000";
    resourcePersistenceType = FileResourcePersistenceType.REFERENCED;
    resource = resourceRepository.create(key, resourcePersistenceType, "xml");
    expResult = URI.create("http://rest.digitale-sammlungen.de/data/bsb00001000.xml");
    result = resource.getUri();
    Assert.assertEquals(expResult, result);
    Assert.assertTrue(resource.isReadonly());
  }

  /**
   * Test of find method, of class ResourceRepositoryImpl.
   */
  @Test
  public void testFind() throws Exception {
    System.out.println("find");
    String key = "snafu";
    FileResourcePersistenceType resourcePersistenceType = RESOLVED;
    FileResource resource = resourceRepository.find(key, resourcePersistenceType, MimeType.MIME_APPLICATION_XML);

    URI expResult = URI.create("classpath:/snafu.xml");
    URI result = resource.getUri();
    Assert.assertEquals(expResult, result);

    long expSize = 71;
    long size = resource.getSize();
    Assert.assertEquals(expSize, size);

    LocalDateTime lastModified = resource.getLastModified();
    Assert.assertTrue(lastModified.getDayOfMonth() > 0);
  }

  @Test
  public void testFindMimeWildcard() throws Exception {
    FileResource res = resourceRepository.find("snafu", RESOLVED, MimeType.MIME_WILDCARD);
    assertThat(res.getUri()).isEqualTo(URI.create("classpath:/snafu.xml"));
  }
}