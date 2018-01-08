# Articles

A list of articles or online answers that have directly contributed to certain portions of the app.

### Overriding App "Startup" class
Creating a custom Application class to run methods on app start, such as authentication file checking.

- Taken from [Stack Overflow - How can I execute something just once per application start](https://stackoverflow.com/questions/7360846/how-can-i-execute-something-just-once-per-application-start)

### Adding underline to TabLayout
Add a bottom-only line to a TabLayout background by drawing a shape and moving all other sides of the "rectangle" out of the drawing boundaries.

- Taken from: [Stack Overflow - Android TabLayout how to make two underlines](https://stackoverflow.com/questions/37676014/android-tablayout-how-to-make-two-underlines)

### Screen Utility Functions
 Several various Screen utility functions for determining current size, orientation, etc.

 - Taken from: [AlvinAlexander - How to determine Android screen sizes/dimensions/orientation](https://alvinalexander.com/android/how-to-determine-android-screen-size-dimensions-orientation)

### Access fragments in a TabLayout
Enable accessing specific fragments in a TabLayout (since they are created programmatically) in order to execute their (_public_) functions.

- Taken from: [Stack Overflow - Calling a Fragment method from an Activity Android tabs](https://stackoverflow.com/questions/25629042/calling-a-fragment-method-from-an-activity-android-tabs)

### Access Fragment XML elements from Fragment
In order to use `findViewById()` from inside a Fragment, a reference to a View is needed (ie. the parent View). This View reference can be found (and stored) inside the constructor, and then used like `parentView.findViewById()`.

### Create Snackbar in a Fragment
Snackbars need a view (ie. a context or anchor) to attach to, but fragments will occasionally disappear. In these cases, or other cases where the Snackbar should attach to the "app" as a whole, use a provided `id`.

```
View snackbarRoot = getActivity().findViewById(android.R.id.content);
```

### Hide the Soft Keyboard
The Soft Keyboard can be hidden by first checking for the currently focused view and then hiding the keyboard (using the `InputMethodManager` class).

- Taken from [Stack Overflow - Close/Hide the Android soft keyboard](https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard)

### Access the Android shell
Viewing the app's private files is possible through the use of the Android shell (ADB). Navigate to the project directory and run `adb shell` in a terminal. Inside the shell, type `run-as ca.kendallroth.expensesapp` to set the shell permission, the list the directory files (`ls files/`).

### Pass data between Activities
Passing data to a new Activity is as simple as properly using `intent.putExtras()` when creating the Intent - passing data back from the same Activity is also quite simple. Rather than call `startActivity` we call `startActivityForResult()` which will call the class's overriden `onActivityResult()` method when the Activity returns.

- Taken from : [Stack Overflow - How to pass data from 2nd activity to 1st activity when pressed back android](https://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android)

### Navigation Drawer
A Navigation Drawer typically uses a main content view with several fragments that are displayed based on the selected menu options.

- Taken from [Android Sliding Menu using Navigation Drawer](https://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/)

### Room DB Callbacks
Room exposes two callback methods that can be used to populate or respond to database operations, `onCreate` and `onOpen`. These methods can be used to populate with initial seed data or respond each time the database is opened.

- Taken from [Room DB Callbacks](https://medium.com/@srinuraop/database-create-and-open-callbacks-in-room-7ca98c3286ab)

### ViewModels and RecycleAdapter
Android recently released a set of architecture components structure to make managing app data and lifecycle easier, including `ViewModel`s and an Observable object `LiveData` that the Room database can return. This requires implementing an adapter with a corresponding ViewModel that can react to changes in the persistence layer exposed by Room.

- Ideas from [Andriod Architecture Components Blog](http://blog.iamsuleiman.com/android-architecture-components-tutorial-room-livedata-viewmodel/)

### Dynamically Finding Drawables
Drawables can be dynamically found and used by getting the resource identifier based on the resource name and type. For instance, to find a category icon can be expressed as follows: `getResources().getIdentifier("resource_name", "drawable", getPackageName());`;

- Taken from [Stack Overflow - How to get Images dynamically](https://stackoverflow.com/questions/9156698/how-to-get-images-dynamically-from-drawable-folder)

### Tinting ImageViews
`ImageView` components can be tinted be setting the `colorFilter` property with `imageView.setColourFilter(Color.parseColor("..."));`

- Taken from [Stack Overflow - How to set tint for Image View](https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android)

### Dagger 2
Dagger 2 is a Google library developed to simplify the process of injecting dependencies.

- [Dagger 2 Android Tutorial](https://causeyourestuck.io/2017/02/18/dagger2-android-tutorial/)
- [Dagger 2 - The Simple Approach](https://android.jlelse.eu/dagger-2-the-simplest-approach-3e23502c4cab)

### Download File
Downloading a file (without using `DownloadManager`) involves an `InputStream` and opening a storage location (see **Permissions**).

- [Android Hive - Download File](https://www.androidhive.info/2012/04/android-downloading-file-by-showing-progress-bar/)
- [Stack Overflow - Download File](https://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog)

### Requesting Permission
Saving a file to the Downloads folder requires requesting permission from the user and handling either outcome.

- [Android - Request Permissions](https://developer.android.com/training/permissions/requesting.html)
- [Stack Overflow - Check permissions in fRagment](https://stackoverflow.com/questions/40760625/how-to-check-permission-in-fragment)
- [Stack Overflow - Read file from URI](https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed)

### Firebase Notifications
Responding to a Firebase message when the app is open requires creating a Firebase messaging service and creating a notification channel within the app.

- [Firebase Messaging](https://firebase.google.com/docs/cloud-messaging/android/first-message?authuser=1)
- [Stack Overflow - Notification Channel](https://stackoverflow.com/questions/45462666/notificationcompat-builder-deprecated-in-android-o)
- [Android - Notification Channel](https://developer.android.com/guide/topics/ui/notifiers/notifications.html)

### Send Email
Creating an email to send is as easy as creating an Intent.

- [Stack Overflow - Email with Intent](https://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application)
