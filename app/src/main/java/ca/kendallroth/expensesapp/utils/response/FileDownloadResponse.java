package ca.kendallroth.expensesapp.utils.response;

import java.io.File;

public final class FileDownloadResponse extends Response {

  private File file;

  /**
   * Response from an operation that indicates true or false
   *
   * @param statusCode Operation completion status code
   * @param message    Operation completion message
   * @param file       Downloaded file
   */
  public FileDownloadResponse(StatusCode statusCode, String message, File file) {
    super(statusCode, message);

    this.file = file;
  }

  /**
   * Get the downloaded file
   * @return Downloaded file
   */
  public File getFile() {
    return this.file;
  }

  /**
   * Convert the Response to a string
   * @return String representation of Response object
   */
  @Override
  public String toString() {
    return String.format(
        "Status Code: '%s'  -  Message: '%s'",
        this.statusCode, this.message
    );
  }
}
