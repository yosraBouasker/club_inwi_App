package com.inwi.clubinwi.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.inwi.clubinwi.LoginActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.inwi.clubinwi.Utils.Utils.getResizedBitmap;

public class InscriptionStep2 extends BaseFragment {

    protected static final String TAG = "InscriptionStep2";
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_TAKE_IMG = 2;
    private Context mContext;
    private View mView;
    private CircularImageView avatar;
    private EditText cin, nom, prenom, email, password;
    private TextView valider;
    private CheckBox homme, femme;
    private Checkable cgu, cgu_inwi;
    private Boolean valid = true;
    private String imageBase64 = "";
    private ProgressDialog progressDialog;
    private String preinscription = "";
    private String telephone = "";
    private ImageView close;

    /**
     * camera and gallery
     */
    public static String PATH_CAMERA_PHOTOS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ClubInwi/";

    static final int REQUEST_IMAGE_GALLERY = 10;
    static final int REQUEST_IMAGE_CAPTURE = 11;

    static final int REQUEST_PERMISSION_CAMERA = 100;
    static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 101;

    private String currentCameraImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_inscription_step2, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(R.string.subscribe);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);
        close = mView.findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((Activity) mContext).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                ((LoginActivity) mContext).onBackPressed();
            }
        });

        cin = mView.findViewById(R.id.cin);
        nom = mView.findViewById(R.id.nom);
        prenom = mView.findViewById(R.id.prenom);
        homme = mView.findViewById(R.id.checkBoxHomme);
        femme = mView.findViewById(R.id.checkBoxFemme);
        email = mView.findViewById(R.id.email);
        password = mView.findViewById(R.id.password);
        avatar = mView.findViewById(R.id.avatar);
        cgu = mView.findViewById(R.id.cgu);
        cgu_inwi = mView.findViewById(R.id.cgu_inwi);
        valider = mView.findViewById(R.id.valider);

        Bundle args = getArguments();
        if (args != null) {
            if (args.getString("result") != null)
                preinscription = args.getString("result");
            if (args.getString("telephone") != null)
                telephone = args.getString("telephone");
        }
        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                validator();
                if (valid) {
                    progressDialog.show();
                    View view = ((Activity) mContext).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    signUp();
                }
            }
        });

        avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureDialog();

            }
        });

        homme.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    hommeChecked();
                else
                    homme.setTextColor(getResources().getColor(R.color.gray));
            }
        });
        femme.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    femmeChecked();
                else
                    femme.setTextColor(getResources().getColor(R.color.gray));
            }
        });
    }

    private void hommeChecked() {
        homme.setChecked(true);
        homme.setTextColor(getResources().getColor(R.color.pink));
        femme.setChecked(false);
        femme.setTextColor(getResources().getColor(R.color.gray));
    }

    private void femmeChecked() {
        homme.setChecked(false);
        homme.setTextColor(getResources().getColor(R.color.gray));
        femme.setChecked(true);
        femme.setTextColor(getResources().getColor(R.color.pink));
    }



    private void validator() {
        if (cin.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (nom.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (prenom.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (!homme.isChecked() && !femme.isChecked()) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (email.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (password.getText().toString().length() == 0) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            valid = false;
            return;
        }

        if (!Utils.emailValidator(email.getText().toString())) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.email_format));
            valid = false;
            return;
        }

        if (password.getText().toString().length() < 6) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.length_password));
            valid = false;
            return;
        }

        if (!cgu.isChecked()) {
            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.cgu_unchecked));
            valid = false;
            return;
        }

        if (preinscription.equals("")) {
            Utils.showToastOnUiTread(mContext, "Token invalid");
            valid = false;
            return;
        }

        if (telephone.equals("")) {
            Utils.showToastOnUiTread(mContext, "N° Téléphone invalide");
            valid = false;
        }

    }

    private Map<String, String> createParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (cgu_inwi.isChecked()) {
            params.put("accept_condition", "" + 1);
        } else {
            params.put("accept_condition", "" + 0);
        }
        if (cgu.isChecked()) {
            params.put("accept_inwi_partager_mes_donnees", "" + 1);
        } else {
            params.put("accept_inwi_partager_mes_donnees", "" + 0);
        }
        if (homme.isChecked())
            params.put("gender_id", "" + 1);
        else
            params.put("gender_id", "" + 2);

        params.put("cin", cin.getText().toString());
        params.put("password", password.getText().toString());
        params.put("password_again", password.getText().toString());
        params.put("avatar", imageBase64);
        params.put("first_name", prenom.getText().toString());
        params.put("last_name", nom.getText().toString());
        params.put("email_address", email.getText().toString());
        params.put("preinscription", preinscription);
        params.put("telephone", telephone);
        params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
        Log.i("params", params.toString());
        return params;
    }

    private void signUp() {

        String url = Constants.URL_SIGNUP;
        MyLog.e("URL", url);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog!=null && progressDialog.isShowing() && isAdded())
                progressDialog.dismiss();
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String header = resultJson.getString("header");
                    if (header.equals("OK")) {
                        JSONObject js = resultJson.getJSONObject("result");
                        Utils.saveToSharedPreferences(mContext, Constants.USER_PHONE, js.getString("telephone"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FULLNAME, js.getString("full_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FIRST_NAME, js.getString("first_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_LAST_NAME, js.getString("last_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_TOKEN, js.getString("token"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_AVATAR, js.getString("avatar"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_EMAIL, js.getString("email_address"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS, js.getJSONObject("filleuls").getJSONArray("list").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS_COUNT, String.valueOf(js.getJSONObject("filleuls").getInt("count")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS, js.getJSONObject("forfaits").getJSONArray("list").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF, js.getJSONObject("forfaits").getJSONArray("actif").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT, String.valueOf(js.getJSONObject("forfaits").getInt("count")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL, String.valueOf(js.getJSONObject("level").getInt("num")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_CADEAUX, String.valueOf(js.getInt("cadeaux")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_DATE, String.valueOf(js.getString("created_at")));
                        if (js.getJSONObject("level").getInt("num") == 7)
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, " Club inwi");
                        else if (js.getJSONObject("level").getInt("num") == 8)
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi Premium");
                        Fragment fragment = new InscriptionStep3();
                        Bundle args = new Bundle();
                        args.putString("telephone", js.getString("telephone"));
                        fragment.setArguments(args);
                        Utils.switchFragment((FragmentActivity) mContext, fragment, InscriptionStep3.class.toString(), R.id.content_login, true, true, true);
                    } else
                        Utils.showToastOnUiTread(mContext, resultJson.getString("message"));

                } catch (JSONException e) {
                    Utils.showToastOnUiTread(mContext, "Merci de resséyez à nouveau");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToastOnUiTread(mContext, "Merci de resséyez à nouveau");
                MyLog.e("ERROR INSERT DATA USER", "" + error.toString());
                signUp();
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
               // params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };
        int socketTimeout = 30000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, url);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public AlertDialog getPictureDialog() {
        final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        dialogLang.setContentView(R.layout.dialog_picture);

        dialogLang.findViewById(R.id.myTextViewOk).setOnClickListener(v -> dialogLang.dismiss());

        dialogLang.findViewById(R.id.myTextViewCamera).setOnClickListener(v -> {
            checkPermission();
            dialogLang.dismiss();
        });

        dialogLang.findViewById(R.id.myTextViewGallery).setOnClickListener(v -> {
            Log.e("myTextViewGallery", "myTextViewGallery");
            chooseImageFromGallery();
            /*Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG);*/
            dialogLang.dismiss();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
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
            photoUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", theCapturedImageFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        currentCameraImagePath = "file:" + theCapturedImageFile.getAbsolutePath();

        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
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
        Dexter.withActivity((Activity) mContext)
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


    /*private void showPhoto(Uri imageUri) {
        Bitmap mBitmap = null;
        try {

            File mCapturedFile = new File(Utils.getAbsolutePath(imageUri, mContext));

            mBitmap = Utils.decodeFile(getActivity(), mCapturedFile);

        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (mBitmap == null) {
            return;
        }
        avatar.setImageBitmap(mBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.e("IMAGE BASE64", "AZEAZE");
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK) {
            MyLog.e("RESULT_LOAD_IMG", "RESULT_LOAD_IMG");
            if (data.getData() == null) {
                MyLog.e("RESULT_LOAD_IMG", "RESULT_LOAD_IMG2");
                return;
            }
            imageBase64 = Utils.getImageBase64(data.getData(), mContext);
            showPhoto(data.getData());
            MyLog.e("RESULT_LOAD_IMG", imageBase64);
        } else if (requestCode == RESULT_TAKE_IMG && resultCode == Activity.RESULT_OK) {
            Log.e("RESULT_TAKE_IMG", "take picture from camera");
            if (data.getData() == null) {
                MyLog.e("RESULT_TAKE_IMG", "RESULT_TAKE_IMGW>" + data.getData());
                return;
            }
            imageBase64 = Utils.getImageBase64(data.getData(), mContext);
            showPhoto(data.getData());
            MyLog.e("RESULT_TAKE_IMG", imageBase64);
        } else {
        }
    }

    public void takePicture(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }

    public void checkPermission(){
        Dexter.withActivity((Activity) mContext)
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
                    public void onPermissionRationaleShouldBeShown(Current<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }*/

}