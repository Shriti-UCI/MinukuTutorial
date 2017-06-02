package demo.minukututorial;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bugfender.sdk.Bugfender;
import com.firebase.client.Config;
import com.firebase.client.Firebase;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;

/**
 * Created by shriti on 5/26/17.
 * https://guides.codepath.com/android/Understanding-the-Android-Application-Class
 * Read above what the android application does.
 * This is the first entity that runs before
 * anything else happens at the time when the
 * application process starts.
 */

public class MinukuTutorialApp extends Application {
    private static MinukuTutorialApp instance;
    private static Context mContext;

    public static MinukuTutorialApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //UserPreferences is where you can write client specific configurations (email ID, notification settings, etc.)
        UserPreferences.getInstance().Initialize(getApplicationContext());

        //For this application instance, set the firebase URL where data will be stored
        //The URL is first written as a part of the UserPreference and then made a part of Constants
        if (UserPreferences.getInstance().getPreference("FIREBASE_URL") == null) {
            Log.d("MinukuTutorialApp", "Setting firebase url in preferences.");
            UserPreferences.getInstance().writePreference("FIREBASE_URL", "https://minukututorial.firebaseio.com");
            Constants.getInstance().setFirebaseUrl(UserPreferences.getInstance().getPreference("FIREBASE_URL"));
        }

        //Firebase configurations
        //https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/Config.html#setPersistenceCacheSizeBytes-long-
        Config mConfig = new Config();
        mConfig.setPersistenceEnabled(true);
        long cacheSizeOfHundredMB = 100 * 1024 * 1024;
        mConfig.setPersistenceCacheSizeBytes(cacheSizeOfHundredMB);
        mConfig.setPersistenceEnabled(true);
        mConfig.setAndroidContext(this);
        Log.d("MinukuTutorialApp", "initializing firebase");
        Firebase.setDefaultConfig(mConfig);

        //Third party logging and log monitoring mechanism.
        //https://bugfender.com/
        Bugfender.init(this, "N7pdXEGbmKhK9k8YtpFPyXORtsAwgZa5", false);
        Bugfender.setForceEnabled(true);
    }
}
