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

        UserPreferences.getInstance().Initialize(getApplicationContext());

        if (UserPreferences.getInstance().getPreference("FIREBASE_URL") == null) {
            Log.d("MinukuTutorialApp", "Setting firebase url in preferences.");
            UserPreferences.getInstance().writePreference("FIREBASE_URL", "https://minukututorial.firebaseio.com");
            Constants.getInstance().setFirebaseUrl(UserPreferences.getInstance().getPreference("FIREBASE_URL"));
        }

        Config mConfig = new Config();
        mConfig.setPersistenceEnabled(true);
        long cacheSizeOfHundredMB = 100 * 1024 * 1024;
        mConfig.setPersistenceCacheSizeBytes(cacheSizeOfHundredMB);
        mConfig.setPersistenceEnabled(true);
        mConfig.setAndroidContext(this);
        Log.d("MinukuTutorialApp", "initializing firebase");
        Firebase.setDefaultConfig(mConfig);

        Bugfender.init(this, "N7pdXEGbmKhK9k8YtpFPyXORtsAwgZa5", false);
        Bugfender.setForceEnabled(true);
    }
}
