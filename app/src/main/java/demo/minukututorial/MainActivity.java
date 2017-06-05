package demo.minukututorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.dao.LocationDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.streamgenerator.LocationStreamGenerator;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;

public class MainActivity extends AppCompatActivity {

    TextView latitude;
    TextView longitude;
    LocationDataRecord currentLocation;
    public static final String TAG = "MainActivity";

    private void initialize() {
        Log.d(TAG, "Initializing inside main activity.");
        MinukuDAOManager daoManager = MinukuDAOManager.getInstance();
        //For location register the DAO so that we can start pushing location data to firebase
        LocationDataRecordDAO locationDataRecordDAO = new LocationDataRecordDAO();
        daoManager.registerDaoFor(LocationDataRecord.class, locationDataRecordDAO);

        //initialize the stream generator so that it gets registered in the system
        LocationStreamGenerator locationStreamGenerator =
                new LocationStreamGenerator(getApplicationContext());
    }

    public static final String encodeEmail(String unencodedEmail) {
        if (unencodedEmail == null) return null;
        return unencodedEmail.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Constants.getInstance().hasFirebaseUrlBeenSet()) {
            Log.d(TAG, "Updating firebase url.");
            Constants.getInstance().setFirebaseUrl("https://minukututorial.firebaseio.com");
        }

        if (UserPreferences.getInstance().getPreference(Constants.ID_SHAREDPREF_EMAIL) == null) {

            String email = "coolluke@gmail.com";
            UserPreferences.getInstance().writePreference(Constants.ID_SHAREDPREF_EMAIL, email);
            UserPreferences.getInstance().writePreference(Constants.KEY_ENCODED_EMAIL,
                    encodeEmail(email));
        }


        Log.d(TAG, "MainActivity onCreate called..");
        setContentView(R.layout.activity_main);

        initialize();
        Log.d(TAG, "MainActivity initalized.");
        startService(new Intent(getBaseContext(), BackgroundService.class));
        Log.d(TAG, "Backgroundservice started.");
        Log.d(TAG, "Firebase URL is " + Constants.getInstance().getFirebaseUrl());

        latitude = (TextView) findViewById(R.id.current_latitude);
        longitude = (TextView) findViewById(R.id.current_longitude);

        refreshLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "activity resuming now");
        refreshLocation();
    }

    private void refreshLocation(){
        try {
            Log.d(TAG, "Trying to get current location.");
            currentLocation =
                    MinukuStreamManager.getInstance().getStreamFor(LocationDataRecord.class).getCurrentValue();
        } catch (StreamNotFoundException e) {
            e.printStackTrace();
        }

        if(currentLocation!= null) {
            Log.d(TAG, "Updating UI with current location.");
            latitude.setText(String.valueOf(currentLocation.getLatitude()));
            longitude.setText(String.valueOf(currentLocation.getLongitude()));
        }
        else {
            Log.d(TAG, "Current location is null.");
            latitude.setText("unknown");
            longitude.setText("unknown");
        }
    }
}
