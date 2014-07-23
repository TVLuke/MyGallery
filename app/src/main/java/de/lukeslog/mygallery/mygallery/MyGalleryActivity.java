package de.lukeslog.mygallery.mygallery;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lukas on 23.07.14.
 */
public class MyGalleryActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = new Bundle();
        arg.putString("folder", "SnapNowImages");
        MyGaleryFragment gallery = new MyGaleryFragment();
        gallery.setArguments(arg);
        getFragmentManager().beginTransaction().add(android.R.id.content, gallery).commit();
    }

}
