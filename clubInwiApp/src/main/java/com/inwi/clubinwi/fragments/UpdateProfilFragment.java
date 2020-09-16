package com.inwi.clubinwi.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.BuildConfig;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.views.CircularImageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.inwi.clubinwi.Utils.Utils.getResizedBitmap;

public class UpdateProfilFragment extends Fragment implements OnDateSetListener, OnClickListener {

    Context context;
    // Hold path to the file where the cam image is stored
    private View mView;
    private ProgressDialog progressDialog;
    private CircularImageView avatar;
    private EditText nom, prenom, cin, email, adresse, codePostal, ville;
    private TextView valider, dateNaissance;
    private String imageBase64 = "";
    private Boolean valid = true;
    private DatePickerDialog mDatePicker;
    private SimpleDateFormat dt1;
    private Calendar mcurrentDate;
    private ImageView icEditProfile;
    private int mYear, mMonth, mDay;

    /**
     * camera and gallery
     */
    public static String PATH_CAMERA_PHOTOS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ClubInwi/";

    static final int REQUEST_IMAGE_GALLERY = 10;
    static final int REQUEST_IMAGE_CAPTURE = 11;

    static final int REQUEST_PERMISSION_CAMERA = 100;
    static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 101;

    private String currentCameraImagePath;


    private Uri picUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mView = inflater.inflate(R.layout.fragment_update_profil, container, false);
        getActivity().getWindow().setFormat(PixelFormat.RGBA_8888);
        init(mView);

        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("picUri", picUri);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            picUri = savedInstanceState.getParcelable("picUri");
        }
    }

    @SuppressLint("NewApi")
    private void init(View mView) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);

        avatar = mView.findViewById(R.id.avatar);
        cin = mView.findViewById(R.id.cin);
        nom = mView.findViewById(R.id.nom);
        prenom = mView.findViewById(R.id.prenom);
        email = mView.findViewById(R.id.email);
        dateNaissance = mView.findViewById(R.id.dateNaissance);
        adresse = mView.findViewById(R.id.adresse);
        codePostal = mView.findViewById(R.id.codePostal);
        ville = mView.findViewById(R.id.ville);
        valider = mView.findViewById(R.id.valider);

        icEditProfile = mView.findViewById(R.id.ic_profile_edit);

        cin.setText(Utils.readFromSharedPreferences(context, Constants.USER_CNI));
        nom.setText(Utils.readFromSharedPreferences(context, Constants.USER_LAST_NAME));
        prenom.setText(Utils.readFromSharedPreferences(context, Constants.USER_FIRST_NAME));
        email.setText(Utils.readFromSharedPreferences(context, Constants.USER_EMAIL));
        dateNaissance.setText(Utils.readFromSharedPreferences(context, Constants.USER_BIRTHDAY));
        adresse.setText(Utils.readFromSharedPreferences(context, Constants.USER_ADDRESS));
        codePostal.setText(Utils.readFromSharedPreferences(context, Constants.USER_ZIPCODE));
        ville.setText(Utils.readFromSharedPreferences(context, Constants.USER_CITY));

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.image).showImageOnFail(R.drawable.image).build();
        if (Constants.imageLoader == null)
            Utils.initImageLoader(context);
        if (Utils.readFromSharedPreferences(context, Constants.USER_AVATAR) != null) {
            String avatarURL = Utils.readFromSharedPreferences(context, Constants.USER_AVATAR);
            Constants.imageLoader.displayImage(avatarURL, avatar, options);
        } else {
            avatar.setImageResource(R.drawable.profil);
        }

        dt1 = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        dateNaissance.setOnClickListener(this);

        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                validator();
                if (valid) {
                    View view = ((Activity) context).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    updateProfilData();
                }
            }
        });
        icEditProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                getPictureDialog();
            }
        });

    }

    private void validator() {

        if (nom.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(context, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (prenom.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(context, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (email.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(context, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (!Utils.emailValidator(email.getText().toString())) {
            Utils.showToastOnUiTread(context, getResources().getString(R.string.email_format));
            valid = false;
            return;
        }

    }


    private Map<String, String> createParams() {
        String currentPhoneNumbre = "";
        try {
            currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(context, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", prenom.getText().toString());
        params.put("last_name", nom.getText().toString());
        params.put("email_address", email.getText().toString());
        params.put("birthday", dateNaissance.getText().toString());
        params.put("address", adresse.getText().toString());
        params.put("zip_code", codePostal.getText().toString());
        params.put("city", ville.getText().toString());
        params.put("avatar", imageBase64);
        params.put("token", Utils.readFromSharedPreferences(context, Constants.USER_TOKEN));
        params.put("phone", currentPhoneNumbre);
        params.put("form", "user");
        params.put("cin", cin.getText().toString());
        params.put("lang", Utils.readFromSharedPreferences(context, Constants.USER_LANGUE));

        Log.i("params", params.toString());
        return params;
    }

    private void updateProfilData() {

        progressDialog.show();
        String url = Constants.URL_BASE + "/api/client/update/profile";
        MyLog.e("URL", url);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS UPDATE DATA USER", response);
                progressDialog.dismiss();
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String header = resultJson.getString("header");
                    Log.i("header", header);
                    if (header.equals("OK")) {
                        JSONObject js = new JSONObject(response).getJSONObject("result");
                        Utils.saveToSharedPreferences(context, Constants.USER_TOKEN, js.getString("token"));
                        Utils.saveToSharedPreferences(context, Constants.USER_FULLNAME, js.getString("full_name"));
                        Utils.saveToSharedPreferences(context, Constants.USER_FIRST_NAME, js.getString("first_name"));
                        Utils.saveToSharedPreferences(context, Constants.USER_LAST_NAME, js.getString("last_name"));
                        Utils.saveToSharedPreferences(context, Constants.USER_CNI, js.getString("cni"));
                        Utils.saveToSharedPreferences(context, Constants.USER_AVATAR, js.getString("avatar"));
                        Utils.saveToSharedPreferences(context, Constants.USER_EMAIL, js.getString("email_address"));
                        Utils.saveToSharedPreferences(context, Constants.USER_BIRTHDAY, String.valueOf(js.getString("birthdate")));
                        Utils.saveToSharedPreferences(context, Constants.USER_ZIPCODE, String.valueOf(js.getString("zipcode")));
                        Utils.saveToSharedPreferences(context, Constants.USER_ADDRESS, String.valueOf(js.getString("address")));
                        Utils.saveToSharedPreferences(context, Constants.USER_CITY, String.valueOf(js.getString("city")));
                        Utils.showToastOnUiTread(context, resultJson.getString("message"));
                        ((MainActivity) context).updateMenuImageAndName();
                    } else
                        Utils.showToastOnUiTread(context, resultJson.getString("message"));

                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR UPDATE DATA USER", "" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return createParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };
        int socketTimeout = 30000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, url);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        dateNaissance.setText(dt1.format(c.getTime()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateNaissance:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
                    mDatePicker = new DatePickerDialog(new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth), this, mYear, mMonth, mDay);
                else
                    mDatePicker = new DatePickerDialog(context, this, mYear, mMonth, mDay);

                mDatePicker.setTitle("Choisis une date");
                mDatePicker.show();
                break;
            case R.id.ic_profile_edit:
                getPictureDialog();
                break;
            default:
                break;
        }
    }
    public android.support.v7.app.AlertDialog getPictureDialog() {
        final android.support.v7.app.AlertDialog dialogLang = new android.support.v7.app.AlertDialog.Builder(context).show();
        dialogLang.setContentView(R.layout.dialog_picture);

        dialogLang.findViewById(R.id.myTextViewOk).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogLang.dismiss();
            }
        });

        dialogLang.findViewById(R.id.myTextViewCamera).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                checkPermission();
                dialogLang.dismiss();
            }
        });

        dialogLang.findViewById(R.id.myTextViewGallery).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("myTextViewGallery", "myTextViewGallery");
                chooseImageFromGallery();
                dialogLang.dismiss();
            }
        });

        // Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogLang.getWindow();
        lp.copyFrom(window.getAttributes());

        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialogLang.setCancelable(true);
        dialogLang.setCanceledOnTouchOutside(true);

        return dialogLang;
    }

    /**
     * Choose an image
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY) {
            imageUri = data.getData();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            imageUri = Uri.parse(currentCameraImagePath);
        }

        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                avatar.setImageBitmap(bitmap);
                //bitmap = rotateImageIfRequired(bitmap, imageUri);
                bitmap = getResizedBitmap(bitmap, 200);
                imageBase64 = Utils.convertBitmapToBase64(bitmap);

            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        } else if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseImageFromGallery();
        }
    }

    public void chooseImageFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, REQUEST_IMAGE_GALLERY);
    }


    /**
     * Take  pic
     */
    private void takePicture() {

        String imageFileName = generateUniqueImageName();
        File theCapturedImageFile = new File(getCamImagesDir(), imageFileName);
        Uri photoUri;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            photoUri = Uri.fromFile(theCapturedImageFile);
        } else {
            photoUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", theCapturedImageFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        currentCameraImagePath = "file:" + theCapturedImageFile.getAbsolutePath();

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public static String generateUniqueImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName + ".jpg";
    }

    public static File getCamImagesDir() {
        File DIR_PHOTOS = new File(PATH_CAMERA_PHOTOS_DIR);
        if (!DIR_PHOTOS.exists()) {
            DIR_PHOTOS.mkdirs();
        }
        return DIR_PHOTOS;
    }

    public void checkPermission(){
        Dexter.withActivity((Activity) context)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            takePicture();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }
}
