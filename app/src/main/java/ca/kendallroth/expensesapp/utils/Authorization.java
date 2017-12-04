package ca.kendallroth.expensesapp.utils;

import android.util.Log;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import ca.kendallroth.expensesapp.persistence.User;
import ca.kendallroth.expensesapp.utils.response.BooleanResponse;
import ca.kendallroth.expensesapp.utils.response.IntResponse;
import ca.kendallroth.expensesapp.utils.response.Response;
import ca.kendallroth.expensesapp.utils.response.StatusCode;

/**
 * User authorization implementation and interface with the database
 *
 * Contains many utility methods for interacting and managing user and authentication information.
 */
public abstract class Authorization {

  /**
   * Determine whether a user's credentials are valid authenticated combination
   * @param email    Account email address
   * @param password Account password
   * @return Whether user is authenticated
   */
  public static BooleanResponse authenticateUser(String email, String password) {
    AppDatabase database = AppDatabase.getDatabase();
    User requestedUser = null;

    // Find the requested user in order to compare the passwords
    try {
      requestedUser = database.userDao().getUser(email);

      AppDatabase.destroyInstance();
    } catch (Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error finding user with email '%s' for authentication", email));

      return new BooleanResponse(StatusCode.FAILURE, "authenticate_user_error", false);
    }

    // Only try authentication if the user exists
    if (requestedUser == null) {
      // TODO: Add audit logging
      Log.d("ExpensesApp.auth", String.format("No user with email '%s' was found for authentication", email));

      return new BooleanResponse(StatusCode.ERROR, "authenticate_user_not_found", false);
    }

    // TODO: Hash password before comparison

    // Verify that the requested password matches the user's password
    boolean isAuthenticated = requestedUser.password.equals(password);

    String debugMessage;
    String resultMessage;

    // Indicate whether the authentication succeeded
    if (isAuthenticated) {
      debugMessage = String.format("User with email '%s' authenticated successfully", email);
      resultMessage = "authenticate_user_succeeded";
    } else {
      debugMessage = String.format("User with email '%s' authentication failed", email);
      resultMessage = "authenticate_user_failed";
    }

    Log.d("ExpensesApp.auth", debugMessage);

    return new BooleanResponse(StatusCode.SUCCESS, resultMessage, isAuthenticated);
  }


  /**
   * Get a count of the number of registered users
   * @return Number of registered users
   */
  public static IntResponse countAllUsers() {
    AppDatabase database = AppDatabase.getDatabase();
    int userCount = 0;

    // Count all users
    try {
      userCount = database.userDao().countUsers();

      AppDatabase.destroyInstance();
    } catch(Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", "Error counting all users");

      return new IntResponse(StatusCode.FAILURE, "count_users_error", -1);
    }


    Log.d("ExpensesApp.auth", String.format("User count found '%s' users", userCount));

    return new IntResponse(StatusCode.SUCCESS, "count_users_succeeded", userCount);
  }


  /**
   * Determine whether a user's account is active
   * @param email Account email address
   * @return Whether account is active
   */
  public static BooleanResponse checkUserActive(String email) {
    AppDatabase database = AppDatabase.getDatabase();
    User requestedUser = null;

    // Find the requested user
    try {
      requestedUser = database.userDao().getUser(email);

      AppDatabase.destroyInstance();
    } catch (Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error finding user with email '%s' to verify activation", email));

      return new BooleanResponse(StatusCode.FAILURE, "check_user_active_error", false);
    }

    // Only check whether the user is active if they exist
    if (requestedUser == null) {
      Log.d("ExpensesApp.auth", String.format("No user with email '%s' was found for verifying activation", email));

      return new BooleanResponse(StatusCode.ERROR, "check_user_active_not_found", false);
    }

    // Determine if user account is active
    boolean isActivated = requestedUser.active;

    String debugMessage;
    String resultMessage;

    // Indicate whether the user is activated
    if (isActivated) {
      debugMessage = String.format("User with email '%s' is activated", email);
      resultMessage = "check_user_active_success";
    } else {
      debugMessage = String.format("User with email '%s' is not activated", email);
      resultMessage = "check_user_active_failure";
    }

    Log.d("ExpensesApp.auth", debugMessage);

    return new BooleanResponse(StatusCode.SUCCESS, resultMessage, isActivated);
  }


  /**
   * Determine whether a user account exists for an email address
   * @param email Account email address
   * @return Whether account exists for email address
   */
  public static BooleanResponse checkUserExists(String email) {
    AppDatabase database = AppDatabase.getDatabase();
    User user = null;

    // Check if a user exists with the provided email
    try {
      user = database.userDao().getUser(email);

      AppDatabase.destroyInstance();
    } catch(Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error finding user with email '%s' to verify activation", email));

      return new BooleanResponse(StatusCode.FAILURE, "check_user_exists_failed", false);
    }

    boolean doesUserExist = user != null;
    String debugMessage;
    String resultMessage;

    // Indicate if a user was found with this email
    if (doesUserExist) {
      debugMessage = String.format("User with email: '%s' found", email);
      resultMessage = "check_user_exists_true";
    } else {
      debugMessage = String.format("User with email: '%s' not found", email);
      resultMessage = "check_user_exists_false";
    }


    // TODO: Add audit logging
    Log.d("ExpensesApp.auth", debugMessage);

    return new BooleanResponse(StatusCode.SUCCESS, resultMessage, doesUserExist);
  }


  /**
   * Create a new user account and authentication information
   * @param name     User's first and last names
   * @param email    Account email address
   * @param password Account password
   * @return
   */
  public static Response createUser(String name, String email, String password){
    AppDatabase database = AppDatabase.getDatabase();
    long newRowId = 0;
    BooleanResponse userExists;

    // Only create the user if the email is unique
    userExists = checkUserExists(email);

    if (userExists.getResult()) {
      Log.d("ExpensesApp.auth", String.format("User with email '%s' already exists", email));

      return new Response(StatusCode.ERROR, "create_user_already_exists");
    }

    // Create a new user and add to database
    try {
      Date currentDate = new Date();
      User newUser = new User(0, email, password, name, true, currentDate, currentDate, null, false);

      // TODO: Hash password before adding

      newRowId = database.userDao().addUser(newUser);

      AppDatabase.destroyInstance();
    } catch(Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error creating account with email '%s' and password '%s'", email, password));

      return new Response(StatusCode.FAILURE, "create_user_failed");
    }

    StatusCode resultCode;
    String debugMessage;
    String resultMessage;

    // Indicate if the user was created
    if (newRowId > 0) {
      resultCode = StatusCode.SUCCESS;
      debugMessage = String.format("User created successfully with email '%s' and password '%s'", email, password);
      resultMessage = "create_user_succeeded";
    } else {
      resultCode = StatusCode.ERROR;
      debugMessage = String.format("User creation failed with email '%s' and password '%s'", email, password);
      resultMessage = "create_user_failed";
    }

    // TODO: Add audit logging
    Log.d("ExpensesApp.auth", debugMessage);

    return new Response(resultCode, resultMessage);
  }


  /**
   * Mark an account as deleted
   * @param email Deleted account email
   * @return Whether account deletion succeeded
   */
  public static Response removeUser(String email) {
    AppDatabase database = AppDatabase.getDatabase();
    int rowsDeleted = 0;
    BooleanResponse userExists;

    // Only delete the user if the email exists
    userExists = checkUserExists(email);

    if (userExists.getResult()) {
      Log.d("ExpensesApp.auth", String.format("User with email '%s' does not exist for deletion", email));

      return new Response(StatusCode.ERROR, "remove_user_not_found");
    }

    // Remove the user account
    try {
      rowsDeleted = database.userDao().deleteUser(email);

      AppDatabase.destroyInstance();
    } catch(Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error removing account with email '%s'", email));

      return  new Response(StatusCode.FAILURE, "remove_user_failed");
    }

    StatusCode resultCode;
    String debugMessage;
    String resultMessage;

    // Indicate if the user was removed
    if (rowsDeleted > 0) {
      resultCode = StatusCode.SUCCESS;
      debugMessage = String.format("User with email '%s' removed successfully", email);
      resultMessage = "remove_user_succeeded";
    } else {
      resultCode = StatusCode.ERROR;
      debugMessage = String.format("User with email '%s' was not removed", email);
      resultMessage = "remove_user_failed";
    }

    // TODO: Add audit logging
    Log.d("ExpensesApp.auth", debugMessage);

    return new Response(resultCode, resultMessage);
  }


  /**
   * Reset a user's password after they have requested a reset
   * @param email       Account email address
   * @param newPassword New user password
   * @return Whether password reset succeeded
   */
  public static BooleanResponse resetUserPassword(String email, String newPassword){
    AppDatabase database = AppDatabase.getDatabase();
    User requestedUser = null;

    // Find the requested user
    try {
      requestedUser = database.userDao().getUser(email);

      AppDatabase.destroyInstance();
    } catch (Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error finding user with email '%s' to reset password", email));

      return new BooleanResponse(StatusCode.FAILURE, "reset_password_failure", false);
    }

    // Only update the password if the user exists
    if (requestedUser == null) {
      Log.d("ExpensesApp.auth", String.format("No user with email '%s' was found to reset password", email));

      return new BooleanResponse(StatusCode.ERROR, "reset_password_user_not_found", false);
    }

    // Update the user's password
    requestedUser.password = newPassword;

    database.userDao().updateUser(requestedUser);

    Log.d("ExpensesApp.auth", String.format("Password updated to '%s' for user with email '%s'", newPassword, email));

    return new BooleanResponse(StatusCode.ERROR, "update_password_successful", true);
  }
}
