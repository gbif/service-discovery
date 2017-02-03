package org.gbif.discovery.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to extract information from Maven projects.
 */
public class MavenUtils {

  private static final String POM = "pom.xml";
  private static final String ERROR_MSG = "Error reading the maven project";
  private static final Pattern FILE_PATTERN =  Pattern.compile("file:");
  private static final Pattern NESTED_FILE_PATTERN =  Pattern.compile("!/" +
                                                                      MavenUtils.class.getPackage().getName()
                                                                        .replaceAll("\\.", "/") + '/');

  private static final Logger LOG = LoggerFactory.getLogger(MavenUtils.class);

  /**
   * Reads the pom.xml file and returns a MavenProjects instance.
   */
  public static MavenProject getMavenProject() {
    try(InputStream pomFile = MavenUtils.class.getResourceAsStream(getPOMPath())) {
      MavenXpp3Reader mavenReader = new MavenXpp3Reader();
      Model model = mavenReader.read(pomFile);
      return new MavenProject(model);
    } catch (IOException | XmlPullParserException e) {
      LOG.error(ERROR_MSG, e);
      throw Throwables.propagate(e);
    }
  }

  /**
   * Gets the path to the pom.xml file that is inside the jar file.
   */
  private static String getPOMPath() throws IOException {

    String jarPath = FILE_PATTERN.matcher(MavenUtils.class.getResource("").getPath()).replaceAll("");
    jarPath =  NESTED_FILE_PATTERN.matcher(jarPath).replaceAll("");
    try (JarFile jarFile = new JarFile(jarPath)) {
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry jarEntry = entries.nextElement();
        if (jarEntry.getName().endsWith(POM)) {
          return '/' + jarEntry.getName();
        }
      }
    }
    throw new IllegalStateException("Error reading pom.xml file");
  }

  /**
   * Private constructor.
   * Utility classes must have a private constructor.
   */
  private MavenUtils() {
    // empty private constructor
  }
}
