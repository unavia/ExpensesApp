package ca.kendallroth.expensesapp.utils.response;

/**
 * Utility class to indicate the response of an action
 */
public class Response {
  protected StatusCode statusCode;
  protected String message;

  public StatusCode getStatusCode() {
    return this.statusCode;
  }

  public String getMessage() {
    return this.message;
  }

  /**
   * Response from an operation
   * @param statusCode Operation completion status code
   * @param message    Operation completion message
   */
  public Response(StatusCode statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  /**
   * Check if the response succeeded with an optional warning
   * @return Whether the response succeeded
   */
  public boolean isSuccess() {
    return this.statusCode == StatusCode.SUCCESS || this.statusCode == StatusCode.WARNING;
  }

  /**
   * Check if the response failed or has errors
   * @return Whether the response failed
   */
  public boolean isFailure() {
    return this.statusCode == StatusCode.ERROR || this.statusCode == StatusCode.FAILURE;
  }

  /**
   * Convert the Response to a string
   * @return String representation of Response object
   */
  @Override
  public String toString() {
    return String.format("Status Code: '%s'  -  Message: '%s'", statusCode, message);
  }
}
