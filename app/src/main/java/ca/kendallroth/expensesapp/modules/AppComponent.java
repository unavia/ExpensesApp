package ca.kendallroth.expensesapp.modules;

import javax.inject.Singleton;

import ca.kendallroth.expensesapp.activities.AuthActivity;
// import ca.kendallroth.expensesapp.activities.MainActivity;
import dagger.Component;

@Component(modules = {
    AppModule.class
})
@Singleton
public interface AppComponent {
  // void inject(MainActivity mainActivity);
  void inject (AuthActivity authActivity);
}
