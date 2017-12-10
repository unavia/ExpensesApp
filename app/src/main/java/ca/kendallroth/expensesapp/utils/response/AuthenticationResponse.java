package ca.kendallroth.expensesapp.utils.response;

public final class AuthenticationResponse extends Response {

  private boolean success;
  private int userId;

  /**
   * Response from an operation that indicates true or false
   *
   * @param statusCode Operation completion status code
   * @param message    Operation completion message
   * @param success    Whether authentication succeeded
   * @param userId     User ID if authentication was successful
   */
  public AuthenticationResponse(StatusCode statusCode, String message, boolean success, int userId) {
    super(statusCode, message);

    this.success = success;
    this.userId = userId;
  }

  /**
   * Determine if authentication succeeded
   * @return Whether authentication succeeded
   */
  public boolean didAuthenticate() {
    return this.success;
  }

  /**
   * Get the authenticated User id
   * @return Authenticated User id
   */
  public int getUserId() {
    return this.userId;
  }

  /**
   * Convert the Response to a string
   * @return String representation of Response object
   */
  @Override
  public String toString() {
    return String.format(
        "Status Code: '%s'  -  Message: '%s' - Result: %s - UserId: %s",
        this.statusCode, this.message, this.success, this.userId
    );
  }
}
