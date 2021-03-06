package ca.kendallroth.expensesapp.utils;

import android.content.Context;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File utilities for working with DOM4J XML documents
 */
public abstract class XMLFileUtils {

  // File name for authentication (users) file
  public static String USERS_FILE_NAME = "users.xml";

  /**
   * Get an XML file and prepare it for parsing with DOM4J
   * @param context   Android context
   * @param fileName  File name (path)
   * @return XML document for DOM4J
   * @throws FileNotFoundException File was not found
   * @throws DocumentException     Exception thrown while opening the document
   */
  public static Document getFile(Context context, String fileName) throws FileNotFoundException, DocumentException {
    // Retrieve the specified file and convert it to an XML document for parsing
    FileInputStream fis = context.openFileInput(fileName);
    SAXReader reader = new SAXReader();
    Document document = reader.read(fis);

    return document;
  }

  /**
   * Create a file from a DOM4J XML document
   * @param context   Android context
   * @param fileName  File name (path)
   * @param document  DOM4J XML document
   * @return Whether file was created successfully
   * @throws IOException Exception thrown while performing IO operations on the file
   */
  public static boolean createFile(Context context, String fileName, Document document) throws IOException {
    // Create a file output steam at the specified Android filepath
    FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

    // Prettify the XML file
    OutputFormat format = OutputFormat.createPrettyPrint();

    try {
      // Write the document into the file output stream
      XMLWriter writer = new XMLWriter(fos, format);
      writer.write(document);
      writer.close();
    } catch(Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Delete an XML document
   * @param context   Android context
   * @param fileName  File name (path)
   * @return Whether file was deleted successfully
   */
  public static boolean deleteFile(Context context, String fileName) {
    // Delete a specified file
    return context.deleteFile(fileName);
  }
}
