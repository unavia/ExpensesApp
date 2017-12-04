package ca.kendallroth.expensesapp.utils.response;

/**
 * Utility enum to designate the status code of a response
 */
public enum StatusCode {
  SUCCESS,  // Operation successful
  FALSE,    // Operation succeeded but a negative result
  WARNING,  // Operation succeeded with warnings
  ERROR,    // Operation failed with errors
  FAILURE   // Operation did not complete
}
