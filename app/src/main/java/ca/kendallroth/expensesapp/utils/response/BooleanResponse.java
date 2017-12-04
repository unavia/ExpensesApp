package ca.kendallroth.expensesapp.utils.response;

public final class BooleanResponse extends Response {

  private boolean result;

  /**
   * Response from an operation that indicates true or false
   *
   * @param statusCode Operation completion status code
   * @param message    Operation completion message
   * @param result     Operation return result
   */
  public BooleanResponse(StatusCode statusCode, String message, boolean result) {
    super(statusCode, message);

    this.result = result;
  }

  /**
   * Get the operation result
   * @return Operation return result
   */
  public boolean getResult() {
    return this.result;
  }

  /**
   * Convert the Response to a string
   * @return String representation of Response object
   */
  @Override
  public String toString() {
    return String.format(
        "Status Code: '%s'  -  Message: '%s' - Result: %s",
        this.statusCode, this.message, this.result
    );
  }
}
