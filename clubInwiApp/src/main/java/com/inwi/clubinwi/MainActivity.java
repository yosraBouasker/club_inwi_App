package com.inwi.clubinwi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.TabFragmentAdapter;
import com.inwi.clubinwi.fragments.CKDOFragement;
import com.inwi.clubinwi.fragments.EnCoursFragment;
import com.inwi.clubinwi.fragments.HomeFragment;
import com.inwi.clubinwi.fragments.MenuFragment;
import com.inwi.clubinwi.fragments.ParrainFragment;
import com.inwi.clubinwi.fragments.UpdateProfilFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

import static android.view.Gravity.LEFT;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    public DrawerLayout drawer;
    private Fragment mContent,mCont;
    private MenuFragment menuFragment;
    private Context mContext;
    private TextView phone, text_ckdo,nbPoints;
    private View separator;
    private LinearLayout llPopup;
    private RelativeLayout rlPopGlobal, rl_profil, rl_ckdo, rl_home, myLayoutPub;
    private ImageView right, left, home, ckdo, profil, closePub, imagePub;
    private RelativeLayout footer;

    private boolean inUpdateProfil = false;
    private boolean inProfil = false;
    private boolean inAddParrain = false;

    //TabLayout mTabs;
    //View mIndicator;
    //ViewPager mViewPager;

    private int indicatorWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //selectlanguage(getApplicationContext());
        mContext =this;
        setContentView(R.layout.activity_main);

        //here
        getWindow().setFormat(PixelFormat.RGBA_8888);


        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                Log.d(TAG, "onFragmentViewCreated: " + f.getClass().getSimpleName());
            }
        }, true);
        init();
    }

    public void selectlanguage(Context context, String l) {

        Locale locale = new Locale(l);
        Locale.setDefault(locale);
        Resources res = context.getResources();

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
        //Utils.saveToSharedPreferences(context, Constants.USER_LANGUE, "fr");
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState( Bundle outState) {
        outState.putString("MyString", "Welcome back to Android");
        outState.putBoolean("inUpdateProfil",inUpdateProfil );
        outState.putBoolean("inProfil",inProfil );
        outState.putBoolean("inAddParrain",inAddParrain );
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState == null){
            Intent mLoginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(mLoginIntent);
            finish();
        }else {
            String myString = savedInstanceState.getString("MyString");
            inUpdateProfil = savedInstanceState.getBoolean("inUpdateProfil");
            inProfil = savedInstanceState.getBoolean("inProfil");
            inAddParrain = savedInstanceState.getBoolean("inAddParrain");
        }


    }

    @SuppressLint("NewApi")
    private void init() {
        llPopup = findViewById(R.id.popup);
        rlPopGlobal = findViewById(R.id.bg_popup);
        phone = findViewById(R.id.numero);
        nbPoints = findViewById(R.id.points);
        separator = findViewById(R.id.separator);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        footer = findViewById(R.id.footer);
        rl_profil = findViewById(R.id.rl_profil);
        rl_ckdo = findViewById(R.id.rl_ckdo);
        rl_home = findViewById(R.id.rl_home);
        home = findViewById(R.id.home);
        ckdo = findViewById(R.id.ckdo);
        profil = findViewById(R.id.profil);
        //text_ckdo = findViewById(R.id.text_ckdo);

        myLayoutPub = findViewById(R.id.myLayoutPub);
        closePub = findViewById(R.id.closePub);
        imagePub = findViewById(R.id.imagePub);

        closePub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                myLayoutPub.setVisibility(View.GONE);
            }
        });

        left.setOnClickListener(this);
        right.setOnClickListener(this);
        rlPopGlobal.setOnClickListener(this);
        llPopup.setOnClickListener(this);

        rl_profil.setOnClickListener(this);
        rl_ckdo.setOnClickListener(this);
        rl_home.setOnClickListener(this);

        ((TextView) findViewById(R.id.title)).setText(R.string.club_inwi);
        changePhoneTitle();

        //mContent = new HomeFragment();
       // mContent = new CKDOFragement();
        //mCont = new Timer_Fragment();
        mContent = new CKDOFragement();
        //mContent = new TombolaAnnifFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("first", true);
        mContent.setArguments(bundle);
        menuFragment = new MenuFragment();
        setHeader();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id != null && id.length() > 0) {

        } else {
            //getSupportFragmentManager().beginTransaction().replace(R.id.content, mCont).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
        }

        if (menuFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.menu_content, menuFragment).commit();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.pink));
        }
        drawer = findViewById(R.id.drawer_layout);

    }

    public void changePhoneTitle() {
        if (Utils.readFromSharedPreferences(mContext, Constants.USER_PHONE) != null) {
            phone.setText(Utils.readFromSharedPreferences(mContext,Constants.USER_PHONE));
            String pts = Utils.readFromSharedPreferences(mContext,Constants.USER_POINT);
            if (pts == null || pts.isEmpty() || pts.toLowerCase().contains("null")) {
                nbPoints.setText("0 " + getString(R.string.user_points));
            } else {
                nbPoints.setText(pts + " " + getString(R.string.user_points));
            }
        }
    }

    public void changeTitle(String title) {
        ((TextView) findViewById(R.id.title)).setText(title);
    }

    public void openDrawer() {

        drawer.openDrawer(LEFT);
    }

    public void closeDrawer() {
        drawer.closeDrawer(LEFT);
    }

    public void enableDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disableDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void updateInfo() {
        if (menuFragment != null)
            menuFragment.updateMenu();
    }

    public void updateMenuImageAndName() {
        if (menuFragment != null)
            menuFragment.addMenuImageName();
    }

    public void addParrain() {
        footer.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.title)).setText(R.string.parrainer_ami);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            right.setImageDrawable(getResources().getDrawable(R.drawable.close, this.getTheme()));
        else
            right.setImageDrawable(getResources().getDrawable(R.drawable.close));
        phone.setVisibility(View.GONE);
        left.setVisibility(View.GONE);
        inAddParrain = true;
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideAddParrain() {
        footer.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.title)).setText(R.string.parrainage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            right.setImageDrawable(getResources().getDrawable(R.drawable.phone, this.getTheme()));
        else
            right.setImageDrawable(getResources().getDrawable(R.drawable.phone));
        phone.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);
        inAddParrain = false;
    }

    public void showProfil() {
        footer.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.title)).setText(R.string.profil);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            right.setImageDrawable(getResources().getDrawable(R.drawable.edit, this.getTheme()));
        else
            right.setImageDrawable(getResources().getDrawable(R.drawable.edit));
        phone.setVisibility(View.GONE);
        nbPoints.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);
        left.setVisibility(View.VISIBLE);
        inProfil = true;
    }

    public void hideProfil() {
        footer.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new CKDOFragement()).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.content, new TombolaAnnifFragment()).commit();

        ((TextView) findViewById(R.id.title)).setText(R.string.cadeaux);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            right.setImageDrawable(getResources().getDrawable(R.drawable.edit, this.getTheme()));
        else
            right.setImageDrawable(getResources().getDrawable(R.drawable.edit));
        phone.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);
        inProfil = false;
    }

    public void showUpdateProfil() {
        footer.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.title)).setText(R.string.modifier_mon_profil);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            right.setImageDrawable(getResources().getDrawable(R.drawable.close, this.getTheme()));
        else
            right.setImageDrawable(getResources().getDrawable(R.drawable.close));
        phone.setVisibility(View.GONE);
        nbPoints.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);
        left.setVisibility(View.GONE);
        mContent = new UpdateProfilFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left).addToBackStack(null).commit();
        inProfil = false;
        inUpdateProfil = true;
    }

    public void hideUpdateProfil() {
        footer.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.title)).setText(R.string.profil);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            right.setImageDrawable(getResources().getDrawable(R.drawable.edit, this.getTheme()));
        else
            right.setImageDrawable(getResources().getDrawable(R.drawable.edit));
        phone.setVisibility(View.GONE);
        left.setVisibility(View.VISIBLE);
        inUpdateProfil = false;
        inProfil = true;
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showActionBar() {
        right.setImageDrawable(getResources().getDrawable(R.drawable.phone));
        phone.setVisibility(View.VISIBLE);
        nbPoints.setVisibility(View.VISIBLE);
        separator.setVisibility(View.VISIBLE);
        left.setVisibility(View.GONE);
        left.setVisibility(View.VISIBLE);
        inUpdateProfil = false;
        inProfil = false;
        inAddParrain = false;
    }
    @Override
    public void onBackPressed() {
        if (inProfil) {
            changeContent(R.id.rl_ckdo, false);

        } else if (inUpdateProfil) {
            changeContent(R.id.rl_profil, false);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                changeContent(R.id.rl_ckdo, false);
            }
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.left:
                View view = ((Activity) mContext).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                openDrawer();
                break;
            case R.id.right:
                if (inAddParrain)
                    onBackPressed();
                else if (inProfil)
                    showUpdateProfil();
                else if (inUpdateProfil)
                    onBackPressed();
                else
                    showShadow();

                break;
            case R.id.bg_popup:
                hidePopup();
                break;
            case R.id.rl_ckdo:
                changeContent(R.id.rl_ckdo, false);
                break;
            case R.id.rl_home:
                changeContent(R.id.rl_home, false);
                break;
            case R.id.rl_profil:
                changeContent(R.id.rl_profil, false);
                break;
        }
    }

    public void changeContent(int id, boolean fromMenu) {

        if (id == R.id.rl_profil) {
            home.setImageDrawable(getResources().getDrawable(R.drawable.footer_home_active));
            ckdo.setImageDrawable(getResources().getDrawable(R.drawable.traarija));
            profil.setImageDrawable(getResources().getDrawable(R.drawable.footer_profil));
            //text_ckdo.setTextColor(getResources().getColor(R.color.white));
            if (fromMenu == false) {
                mContent = new ParrainFragment();
                Bundle bundle = new Bundle();
                showProfil();
                bundle.putString("selectedItem", "profil");
                mContent.setArguments(bundle);
                this.getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
            }

        } else if (id == R.id.rl_home) {
            home.setImageDrawable(getResources().getDrawable(R.drawable.footer_home));
            ckdo.setImageDrawable(getResources().getDrawable(R.drawable.traarija));
            profil.setImageDrawable(getResources().getDrawable(R.drawable.footer_profil_active));
            //text_ckdo.setTextColor(getResources().getColor(R.color.white));
            if (!fromMenu) {
                mContent = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("selectedItem", 0);
                bundle.putBoolean("first", false);
                //bundle.putString("lang", "fr");
                mContent.setArguments(bundle);
                this.getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
            }
        } else if (id == R.id.rl_ckdo) {
            home.setImageDrawable(getResources().getDrawable(R.drawable.footer_home_active));
            ckdo.setImageDrawable(getResources().getDrawable(R.drawable.traarija));
            profil.setImageDrawable(getResources().getDrawable(R.drawable.footer_profil_active));
            //text_ckdo.setTextColor(getResources().getColor(R.color.pink));

            if (!fromMenu) {
                mContent = new CKDOFragement();
//                mContent = new TombolaAnnifFragment();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
            }
        } else {
            home.setImageDrawable(getResources().getDrawable(R.drawable.footer_home));
            ckdo.setImageDrawable(getResources().getDrawable(R.drawable.traarija));
            profil.setImageDrawable(getResources().getDrawable(R.drawable.footer_profil));
            text_ckdo.setTextColor(getResources().getColor(R.color.pink));
        }
    }

    @SuppressLint("NewApi")
    private void showShadow() {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            rlPopGlobal.setVisibility(View.VISIBLE);
            rlPopGlobal.setAlpha(0);
            rlPopGlobal.animate().alpha(1).setDuration(150).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        } else {
            rlPopGlobal.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NewApi")
    private void hidePopup() {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            rlPopGlobal.animate()

                    .alpha(0).setDuration(150).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rlPopGlobal.setVisibility(View.GONE);
                }
            });
        } else {
            rlPopGlobal.setVisibility(View.GONE);
        }

    }

    public void setHeader() {

        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        llPopup.removeAllViews();

        String listForfaits = Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS);
        String currentPhoneNumbre = "";
        try {
            JSONArray jsonArray = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF));
            if (jsonArray.length() > 0 && jsonArray.getJSONObject(0) != null && jsonArray.getJSONObject(0).has("id")) {
                currentPhoneNumbre = jsonArray.getJSONObject(0).getString("id");
            }
        } catch (JSONException e1) {
//            e1.printStackTrace();
        }
        View itemFiltre1 = mInflater.inflate(R.layout.popup_item_phone, null, false);
        ((TextView) itemFiltre1.findViewById(R.id.title)).setText(currentPhoneNumbre);
        itemFiltre1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToastOnUiTread(mContext, "Ce numéro de téléphone est déjà sélectionné");
            }
        });
        llPopup.addView(itemFiltre1);
        if (listForfaits != null && !listForfaits.equals("[]") && !listForfaits.equals("")) {
            try {
                final JSONArray listNum = new JSONArray(listForfaits);
                for (int i = 0; i < listNum.length(); i++) {
                    View itemFiltre = mInflater.inflate(R.layout.popup_item_phone, null, false);
                    String newPhone = listNum.getJSONObject(i).getString("id");
                    Log.d("newPhone", newPhone);
                    ((TextView) itemFiltre.findViewById(R.id.title)).setText(newPhone);
                    final int j = i + 1;
                    itemFiltre.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hidePopup();
                            if (menuFragment != null)
                                menuFragment.changeCurrentPhoneNumber(j);
                            setHeader();
                        }
                    });
                    llPopup.addView(itemFiltre);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }


}
