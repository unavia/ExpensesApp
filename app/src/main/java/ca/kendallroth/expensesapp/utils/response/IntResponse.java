package ca.kendallroth.expensesapp.utils.response;

public final class IntResponse extends Response {

  private int result;

  /**
   * Response from an operation that has an int value
   *
   * @param statusCode Operation completion status code
   * @param message    Operation completion message
   * @param result     Operation return result
   */
  public IntResponse(StatusCode statusCode, String message, int result) {
    super(statusCode, message);

    this.result = result;
  }

  /**
   * Get the operation result
   * @return Operation return result
   */
  public int getResult() {
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

