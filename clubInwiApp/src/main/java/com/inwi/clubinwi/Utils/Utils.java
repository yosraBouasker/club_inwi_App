package com.inwi.clubinwi.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.inwi.clubinwi.LoginActivity;
import com.inwi.clubinwi.R;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    private static final int IMAGE_MAX_SIZE = 2048;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String PATH_CAMERA_PHOTOS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/clubInwi/";



    public static boolean isOnline(Context ctx) {
        boolean var = false;
        if (ctx == null)
            return false;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            var = true;
        }
        return var;
    }

    public static void saveToSharedPreferences(Context mContext, String key, String value) {
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().putString(key, value).apply();
        }
    }

    public static void removeFromSharedPreferences(Context mContext, String key) {
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(key).apply();
        }
    }

    public static String readFromSharedPreferences(Context mContext, String key) {
        if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
            if (mSharedPreferences != null)
                return mSharedPreferences.getString(key, null);
        }
        return null;
    }

    public static void showToastOnUiTread(final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToastOnUiTread(final Context activity, final String text) {
        ((Activity) activity).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void switchContent(Context context, FragmentManager mContext, Fragment fragment, String mFragmentTag, int layoutID, boolean addToBackStack) {
        View view = ((Activity) context).getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        FragmentTransaction transaction = mContext.beginTransaction();
        if (mFragmentTag != null) {
            Fragment lastFragment = mContext.findFragmentByTag(mFragmentTag);
            if (lastFragment != null) {
                transaction.remove(lastFragment);
                //         transaction.commit(); before
                transaction.commitAllowingStateLoss();
                transaction = mContext.beginTransaction();
            }
        }
        transaction.replace(layoutID, fragment, mFragmentTag);
        if (addToBackStack)
            transaction.addToBackStack(mFragmentTag);
        //         transaction.commit(); before
        transaction.commitAllowingStateLoss();
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        // Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        // return _bmp;
        return output;
    }

    public static boolean isMaileValid(final String hex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void switchFragment(FragmentActivity mContext, Fragment mFragment, String mTag, int layoutID, boolean replace, boolean addToBackStack, boolean animate) {
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mTag != null) {
            Fragment lastFragment = fragmentManager.findFragmentByTag(mTag);
            if (lastFragment != null) {
                transaction.hide(lastFragment);
            }
        }

        if (animate)
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right);

        if (replace)
            transaction.replace(layoutID, mFragment, mTag);
        else
            transaction.add(layoutID, mFragment, mTag);

        if (!addToBackStack) {
            transaction.disallowAddToBackStack();
        } else {
            transaction.addToBackStack(null);
            // transaction.addToBackStack(mTag);
        }
        // transaction.commit(); before

        transaction.commitAllowingStateLoss();
    }

    public static String getImageBase64(Uri imageUri, Context mContext) {
        Log.e("getImageBase64", "IMG>" + imageUri.toString());
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            options.inDither = true;

            Bitmap bm = BitmapFactory.decodeFile(getAbsolutePath(imageUri, mContext), options);
//            bm = rotateImageIfNecessary(getExifOrientation(getAbsolutePath(imageUri, mContext)), bm);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap
            // object
            byte[] byteArrayImage = baos.toByteArray();
            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (OutOfMemoryError e) {
//			Log.e("getImageBase64",e.getMessage());
        }
        Log.e("getImageBase64", "getImageBase64");
        return "";
    }

    public static String getAbsolutePath(Uri uri, Context mContext) {
        if (Build.VERSION.SDK_INT >= 19) {
            String id = uri.getLastPathSegment();
            if (id.contains(":")) {
                id = id.split(":")[1];
            }
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;
            Uri tempUri = getUri();
            Cursor imageCursor = mContext.getContentResolver().query(tempUri, imageColumns, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
            if (imageCursor.moveToFirst()) {
                String path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imageCursor.close();
                return path;
            } else {
                return null;
            }
        } else {
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                return path;
            } else
                return null;
        }
    }

    public static Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public static Bitmap decodeFile(Context mContext, File f) {
        Bitmap b = null;
        int exifOrientation = 0;
        try {
            ExifInterface exif = new ExifInterface(f.getPath());
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
//            b = rotateImageIfNecessary(exifOrientation, b);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void initImageLoader(Context context) {
        Options decodingOptions = new Options();
        Constants.cacheDir = StorageUtils.getOwnCacheDirectory(context, "ClubInwi/Caches");
        Constants.imageLoader = ImageLoader.getInstance();
        // Create configuration for ImageLoader (all options are optional, use
        // only those you really want to customize)
        Constants.config = new ImageLoaderConfiguration.Builder(context).imageDownloader(new AuthImageDownloader(context, 10000, 10000)).memoryCacheExtraOptions(480, 800).threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).build();
        // Initialize ImageLoader with created configuration. Do it once on
        // Application start.
        Constants.imageLoader.init(Constants.config);

        // Creates display image options for custom display task (all options
        // are optional)
        Constants.options = new DisplayImageOptions.Builder().showImageForEmptyUri(0).cacheInMemory(true).decodingOptions(decodingOptions).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static void openWebURL(String inURL, Activity activity) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
        activity.startActivity(browse);
    }

    /**
     * when session is expired
     *
     * @param mContext
     * @param message
     */
    public static void expiredSession(Context mContext, String message) {
        Log.d("PREDEV", "expiredSession ; " + mContext);
        customToast(mContext, message);
        Intent intent = new Intent(mContext, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);

    }

    public static void customToast(Context mContext, String message) {
        final Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);

        new CountDownTimer(Math.max(3500, 1000), 1000) {
            @Override
            public void onFinish() {
                toast.show();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();
            }
        }.start();
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Intent getPickImageChooserIntent(Context mContext) {

        Uri outputFileUri = getCaptureImageOutputUri(mContext);

        File file = new File(getRealPathFromURI(mContext, outputFileUri));
        if (file.exists())
            file.delete();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = mContext.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Obter imagem");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    public static Uri getCaptureImageOutputUri(Context mContext) {
        Uri outputFileUri = null;
        File getImage = mContext.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(PATH_CAMERA_PHOTOS_DIR));
            Log.d("path",PATH_CAMERA_PHOTOS_DIR);
        }
        return outputFileUri;
    }

    public static Uri getPickImageResultUri(Intent data, Context mContext) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && MediaStore.ACTION_IMAGE_CAPTURE.equals(action);
        }
        return isCamera ? getCaptureImageOutputUri(mContext) : data.getData();
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static String generateUniqueImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName + ".jpg";
    }

    public static File getCamImagesDir() {

        File DIR_PHOTOS = new File(Utils.PATH_CAMERA_PHOTOS_DIR);
        if (!DIR_PHOTOS.exists()) {
            DIR_PHOTOS.mkdirs();
        }
        return DIR_PHOTOS;
    }


    /**
     * get the current day of the week
     *
     * @return
     */
    public String getCurrentDay() {

        String daysArray[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        return daysArray[day];

    }
}