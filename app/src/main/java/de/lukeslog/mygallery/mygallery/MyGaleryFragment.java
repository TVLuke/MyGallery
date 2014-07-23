package de.lukeslog.mygallery.mygallery;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class MyGaleryFragment extends Fragment {

    private static final String TAG = Constants.TAG;
    File[] allFiles;
    Drawable[] icons;
    int selectedfileno = 0;
    View fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = inflater.inflate(R.layout.activity_my_galery_fragment, container, false);

        Bundle arg = getArguments();
        String foldername = arg.getString("folder");
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername + "/");
        allFiles = folder.listFiles();
        cleanFileListToOnlyIncludeImages();

        if (allFiles.length > 0) {
            pupulateTheImageScroller();


            if (allFiles == null) {

            } else {

                displayImage();


                ImageView left = (ImageView) fragment.findViewById(R.id.left);
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedfileno == 0) {
                            //do nothing
                        } else {
                            selectedfileno--;
                            displayImage();
                            centralizeScrollView();
                        }
                    }
                });

                ImageView right = (ImageView) fragment.findViewById(R.id.right);
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedfileno >= (allFiles.length - 1)) {
                            //do nothing
                        } else {
                            selectedfileno++;
                            displayImage();
                            centralizeScrollView();
                        }
                    }
                });
            }
        }
        return fragment;
    }

    private void centralizeScrollView() {
        HorizontalScrollView hsv = (HorizontalScrollView) fragment.findViewById(R.id.horizontalScrollView);

        ImageView iv = (ImageView) fragment.findViewById(R.id.imageView);
        int width = iv.getWidth() / 2;
        hsv.scrollTo((selectedfileno * 200) - width, 0);
    }

    private void displayImage() {
        File image = allFiles[selectedfileno].getAbsoluteFile();
        Drawable d = Drawable.createFromPath(image.getAbsolutePath());
        ImageView iv = (ImageView) fragment.findViewById(R.id.imageView);
        iv.setImageDrawable(d);
    }

    private void cleanFileListToOnlyIncludeImages() {
        ArrayList<File> files = new ArrayList<File>();
        for (File f : allFiles) {
            if ((f.getName().endsWith("png") || f.getName().endsWith("jpg")) && !f.getName().startsWith("newpic")) {
                files.add(f);
            }
        }
        Collections.reverse(files);
        allFiles = new File[files.size()];
        allFiles = files.toArray(allFiles);
    }

    //from http://stackoverflow.com/questions/7021578/resize-drawable-in-android
    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 200, 200, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    private void pupulateTheImageScroller() {
        //create drawables....
        Log.d(TAG, "create icons");
        new ResizeFilesTask().execute(null, null, null);

    }

    private void addNewIconToImageHome(int i) {
        Log.d(TAG, "add " + i);
        LinearLayout imagehome = (LinearLayout) fragment.findViewById(R.id.imagehome);
        ImageView iv = new ImageView(fragment.getContext());
        imagehome.addView(iv);
        iv.setImageDrawable(icons[i]);
        final int finalI = i;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedfileno = finalI;
                displayImage();
            }
        });
    }

    private class ResizeFilesTask extends AsyncTask<Object, Integer, Long> {
        protected Long doInBackground(Object[] objects) {
            Log.d(TAG, "do in background");
            icons = new Drawable[allFiles.length];
            for (int i = 0; i < allFiles.length; i++) {
                Log.d(TAG, "background--->" + i);
                final File f = allFiles[i];
                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                d = resize(d);
                icons[i] = d;
                publishProgress(i);
            }
            return Long.parseLong("" + allFiles.length);
        }

        protected void onProgressUpdate(Integer... i) {
            Log.d(TAG, "publish process " + i[0]);
            addNewIconToImageHome(i[0]);
        }

        protected void onPostExecute(Long result) {

        }
    }
}
