package ca.kendallroth.expensesapp.utils;

import android.util.Log;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import ca.kendallroth.expensesapp.persistence.User;
import ca.kendallroth.expensesapp.utils.response.BooleanResponse;
import ca.kendallroth.expensesapp.utils.response.IntResponse;
import ca.kendallroth.expensesapp.utils.response.Response;
import ca.kendallroth.expensesapp.utils.response.StatusCode;

public abstract class Authorization {

  public static BooleanResponse authenticateUser(String email, String password) {
    AppDatabase database = AppDatabase.getDatabase();
    User requestedUser = null;

    // Find the requested user
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

    // Verify that the requested password matches the user's password
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
      rowsDeleted = database.userDao().removeUser(email);

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


  public static Response removeAllUsers() {
    AppDatabase database = AppDatabase.getDatabase();
    int rowsDeleted = 0;

    // Only delete all users if any exist
    IntResponse userCount = countAllUsers();

    if (userCount.getResult() <= 0) {
      Log.d("ExpensesApp.auth", "No users were found for deletion");

      return new Response(StatusCode.WARNING, "remove_all_users_found_none");
    }

    // Remove all user accounts
    try {
      rowsDeleted = database.userDao().removeAllUsers();

      AppDatabase.destroyInstance();
    } catch(Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", "Error removing all accounts");

      return new Response(StatusCode.FAILURE, "remove_all_users_failed");
    }

    StatusCode resultCode;
    String debugMessage;
    String resultMessage;

    // Indicate if the all users were removed
    if (rowsDeleted > 0) {
      resultCode = StatusCode.SUCCESS;
      debugMessage = "Deleting all users succeeded";
      resultMessage = "remove_all_users_succeeded";
    } else {
      resultCode = StatusCode.ERROR;
      debugMessage = "Deleting all users failed";
      resultMessage = "remove_all_users_failed";
    }

    // TODO: Add audit logging
    Log.d("ExpensesApp.auth", debugMessage);


    return new Response(resultCode, resultMessage);
  }


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


  public static BooleanResponse updateUserPassword(String email, String oldPassword, String newPassword){
    AppDatabase database = AppDatabase.getDatabase();
    User requestedUser = null;

    // Find the requested user
    try {
      requestedUser = database.userDao().getUser(email);

      AppDatabase.destroyInstance();
    } catch (Exception e) {
      AppDatabase.destroyInstance();

      Log.e("ExpensesApp.auth", String.format("Error finding user with email '%s' to update password", email));

      return new BooleanResponse(StatusCode.FAILURE, "update_password_failure", false);
    }

    // Only update the password if the user exists
    if (requestedUser == null) {
      Log.d("ExpensesApp.auth", String.format("No user with email '%s' was found to update password", email));

      return new BooleanResponse(StatusCode.ERROR, "update_password_user_not_found", false);
    }

    // Verify that the old password matches the user's current password
    boolean isValidOldPassword = requestedUser.password.equals(oldPassword);

    if (!isValidOldPassword) {
      Log.d("ExpensesApp.auth", String.format("Password '%s' does not match the current password for user with email '%s'", oldPassword, email));

      return new BooleanResponse(StatusCode.ERROR, "update_password_incorrect_old_password", false);
    }

    // Update the user's password
    requestedUser.password = newPassword;

    database.userDao().updateUser(requestedUser);

    Log.d("ExpensesApp.auth", String.format("Password updated to '%s' for user with email '%s'", newPassword, email));

    return new BooleanResponse(StatusCode.ERROR, "update_password_successful", true);
  }
}
