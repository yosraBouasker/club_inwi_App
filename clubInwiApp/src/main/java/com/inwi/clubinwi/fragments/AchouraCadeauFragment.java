package com.inwi.clubinwi.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.inwi.clubinwi.PdfActivity;
import com.inwi.clubinwi.PdfViewerActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.achoura.AchouraUtils;
import com.inwi.clubinwi.achoura.models.listing.CurrentCadeau;
import com.inwi.clubinwi.achoura.models.listing.Currents;
import com.inwi.clubinwi.achoura.models.listing.Luck;
import com.inwi.clubinwi.achoura.models.listing.Result;
import com.inwi.clubinwi.achoura.models.reserver.Reservation;
import com.inwi.clubinwi.achoura.rest.RestClient;
import com.inwi.clubinwi.views.MyTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubikstudio.library.LuckyWheelUtils;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class AchouraCadeauFragment extends Fragment {

    private static final String TAG = AchouraCadeauFragment.class.getSimpleName();

    List<LuckyItem> data = new ArrayList<>();

    private LuckyWheelView luckyWheelView;
    private TextView tvTitle, tvDescription, tvConditions, tvWinGift, ttt;
    private Button btnPlay;
    private AppCompatCheckBox cbAgb;
    private View viewWin, viewLoss;
    private ImageView ivCloseWin, ivCloseLoss, ivWinBg;
    private CircleImageView civCadeau;
    private View viewCountdown;

    // Countdown
    private MyTextView tvDays, tvHours, tvMinutes, tvSeconds;
    private RelativeLayout rlDays, rlMinutes, rlHours, rlSeconds;

    String generalConditionsFr = "<u><font color='#FF4081'> le règlement </font></u>";
    String generalConditionsAr = "<u><font color='#FF4081'> الشروط العامة </font></u>";

    // String generalConditionsFr = "J\'accepte <u><font color='#FF4081'> les conditions générales </font></u> de jeu";
    // String generalConditionsAr = "أنا أقبل <'u><font color='#FF4081> الشروط العامة <font></u/> لهذه اللعبة";

    private Luck luck;
    private CurrentCadeau cadeauToady;


    private String winTextPlaceHolderFr = "Vous avez gagné <font color='#C3027E'> %s </font> .";
    private String winTextPlaceHolderAr = "لقد فزت <font color='#C3027E'> %s </font> .";

    private int indexToAlternateWinAndLoss;

    private String dummyGiftAr = "الإنترنت";
    private String dummyGiftFr = "Internet";

    private String giftEndDate;

    private AchouraCountdown achouraCountdown;

    // ******************************************
    // ******************************************
    // ******************************************

    public static AchouraCadeauFragment newInstance() {
        Bundle bundle = new Bundle(1);

        AchouraCadeauFragment achouraCadeauFragment = new AchouraCadeauFragment();
        achouraCadeauFragment.setArguments(bundle);

        return achouraCadeauFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.achoura_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        getCadeauxList();

        // viewCountdown.postDelayed(() -> viewCountdown.setVisibility(View.VISIBLE), 3000);
    }

    @Override
    public void onPause() {
        disposeCountdown();
        super.onPause();
    }

    // ******************************************

    private void initLayout(View view) {
        /////////////////////

        luckyWheelView = view.findViewById(R.id.lucky_wheel);
        btnPlay = view.findViewById(R.id.btn_play);
        cbAgb = view.findViewById(R.id.cb_agb);
        tvTitle = view.findViewById(R.id.tv_title);
        tvDescription = view.findViewById(R.id.tv_description);
        tvConditions = view.findViewById(R.id.tv_conditions);
        tvWinGift = view.findViewById(R.id.tv_win_gift);
        viewWin = view.findViewById(R.id.v_win_included);
        viewLoss = view.findViewById(R.id.v_loss_included);
        ivCloseWin = view.findViewById(R.id.iv_close_win);
        ivCloseLoss = view.findViewById(R.id.iv_close_loss);
        ivWinBg = view.findViewById(R.id.iv_win_bg);
        civCadeau = view.findViewById(R.id.civ_cadeau);

        viewCountdown = view.findViewById(R.id.view_countdown);
        tvDays = viewCountdown.findViewById(R.id.tv_countdown_days);
        tvHours = viewCountdown.findViewById(R.id.tv_countdown_hours);
        tvMinutes = viewCountdown.findViewById(R.id.tv_countdown_minutes);
        tvSeconds = viewCountdown.findViewById(R.id.tv_countdown_seconds);
        rlDays = viewCountdown.findViewById(R.id.rl_countdown_days);
        rlHours = viewCountdown.findViewById(R.id.rl_countdown_hours);
        rlMinutes = viewCountdown.findViewById(R.id.rl_countdown_minutes);
        rlSeconds = viewCountdown.findViewById(R.id.rl_countdown_seconds);

        String gc = AchouraUtils.getAppLocale(getActivity()).equalsIgnoreCase("ar")
                ? generalConditionsAr
                : generalConditionsFr;
        tvConditions.setText(Html.fromHtml(gc));

        data = AchouraUtils.populateLuckyWheel(getActivity());
        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);
        luckyWheelView.setTouchEnabled(false);

        btnPlay.setOnClickListener(v -> {
            int index = LuckyWheelUtils.getRandomIndex(data);
            luckyWheelView.startLuckyWheelWithTargetIndex(index);
        });

        tvConditions.setOnClickListener(v -> showAgreementScreen());

        cbAgb.setOnCheckedChangeListener((buttonView, isChecked) -> btnPlay.setEnabled(isChecked));

        ivCloseWin.setOnClickListener(v -> viewWin.setVisibility(View.GONE));

        ivCloseLoss.setOnClickListener(v -> viewLoss.setVisibility(View.GONE));

        luckyWheelView.setLuckyRoundItemSelectedListener(index -> {
            reserver();

            /*String text = AchouraUtils.getAppLocale(getActivity()).equalsIgnoreCase("fr")
                    ? String.format(winTextPlaceHolderFr, dummyGiftFr)
                    : String.format(winTextPlaceHolderAr, dummyGiftAr);
            tvWinGift.setText(Html.fromHtml(text));
            Glide.with(getContext()).load(R.drawable.pass_rs).into(civCadeau);
            viewWin.setVisibility(View.VISIBLE);*/
        });

        // ******************

        Glide.with(this).load(R.drawable.cadre_congrats).into(ivWinBg);
    }

    private void init() {}

    // *****************

    private void getCadeauxList() {

        Log.d(TAG, "getCadeauxList: SESSION_USER_TOKEN: " + Utils.readFromSharedPreferences(getActivity(), Constants.USER_TOKEN));
        Log.d(TAG, "getCadeauxList: SESSION_USER_PHONE: " + Utils.readFromSharedPreferences(getActivity(), Constants.USER_PHONE));
        Log.d(TAG, "getCadeauxList: SESSION_USER_LANG: " + Utils.readFromSharedPreferences(getActivity(), Constants.USER_LANGUE));

        // Call<Luck> call = RestClient.newInstance().api().listCadeaux("0638523600", "03688070483370828377", "fr");
        if (AchouraUtils.isNetworkAvailable(getActivity())) {
            btnPlay.setText(getString(R.string.text_loading));
            Call<Luck> call = RestClient.newInstance().api().listCadeaux(Utils.readFromSharedPreferences(getActivity(), Constants.USER_PHONE),
                    Utils.readFromSharedPreferences(getActivity(), Constants.USER_TOKEN),
                    Utils.readFromSharedPreferences(getActivity(), Constants.USER_LANGUE));
            call.enqueue(new Callback<Luck>() {
                @Override
                public void onResponse(Call<Luck> call, Response<Luck> response) {
                    hideLoading(true);
                    parseResponse(response);
                    System.out.println("responseee: " + response.body());
                }

                @Override
                public void onFailure(Call<Luck> call, Throwable t) {
                    // Toast.makeText(getActivity(), "Network call failed!", Toast.LENGTH_LONG).show();
                    hideLoading(true);
                    snackRetryListing(getString(R.string.label_generic_failure));
                }
            });
        } else {
            hideLoading(false);
            snackRetryListing(getString(R.string.no_internet));
            // Toast.makeText(AchouraCadeauFragment.this.getActivity(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void parseResponse(Response<Luck> response) {
        Log.d(TAG, "parseResponse: Handling listing response");
        luck = response.body();
        if (luck == null) {
            snackRetryListing(getString(R.string.label_generic_failure));
        } else {
            try {
                long status = luck.getStatus();
                if (status != 200) {
                    Log.d(TAG, "parseResponse: msg: " + luck.getMessage());
                    Toast.makeText(getContext(), luck.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Result result = luck.getResult();
                    if (result != null) {
                        giftEndDate = result.getEndDate();
                        setupAndStartCountdown();

                        Currents currents = result.getCurrents();
                        if (currents != null) {
                            List<CurrentCadeau> list = currents.getCurrentCadeau();
                            if (list != null && !list.isEmpty()) {
                                cadeauToady = list.get(0);
                                if (cadeauToady != null) {
                                    System.out.println("TodayGift: " + cadeauToady.toString());
                                    Glide.with(getContext()).load(cadeauToady.getImage()).into(civCadeau);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "parseResponse: ", e);
            }

        }
    }

    private void snackRetryListing(String message) {
        Snackbar.make(btnPlay, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_retry, v -> getCadeauxList())
                .setActionTextColor(Color.WHITE)
                .show();
    }

    private void hideLoading(boolean enableCheckbox) {
        if (isAdded() && getContext() != null) {
            try {
                btnPlay.setText(getString(R.string.label_action_go));
                cbAgb.setEnabled(enableCheckbox);
            } catch (Exception e) {
                Log.e(TAG, "hideLoading: ", e);
            }
        }
    }

    private void reserver() {
        if (cadeauToady == null) {
            viewLoss.setVisibility(View.VISIBLE);
        } else {
            if (AchouraUtils.isNetworkAvailable(getContext()) && cadeauToady != null) {
                Call<Reservation> reservationCall = RestClient.newInstance()
                        .api()
                        .reserver(Utils.readFromSharedPreferences(getActivity(), Constants.USER_PHONE),
                                Utils.readFromSharedPreferences(getActivity(), Constants.USER_TOKEN),
                                Utils.readFromSharedPreferences(getActivity(), Constants.USER_LANGUE),
                                cadeauToady.getId(),
                                cadeauToady.getCompagnieId(),
                                cadeauToady.getNiveau(),
                                cadeauToady.getPoints());

                reservationCall.enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                        Log.d(TAG, "reserver::onResponse: isSuccess= " + response.isSuccessful() + ", code= " + response.code());
                        if (response.isSuccessful() && response.code() == 200) {
                            Reservation reservation = response.body();
                            Log.d(TAG, "reserver::onResponse : " + reservation.toString());
                            parseReservationResponse(reservation);
                        } else {
                            viewLoss.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                        Log.w(TAG, "reserver::onFailure: ", t);
                        viewLoss.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void parseReservationResponse(Reservation reservation) {
        if (reservation == null) {
            viewLoss.setVisibility(View.VISIBLE);
        } else {
            if (reservation.getStatus() == 200) {
                String text = AchouraUtils.getAppLocale(getActivity()).equalsIgnoreCase("fr")
                        ? String.format(winTextPlaceHolderFr, cadeauToady.getTitle())
                        : String.format(winTextPlaceHolderAr, cadeauToady.getTitle());
                tvWinGift.setText(Html.fromHtml(text));
                viewWin.setVisibility(View.VISIBLE);
            } else {
                viewLoss.setVisibility(View.VISIBLE);
            }
        }

    }

    private void showAgreementScreen() {
        // PdfActivity.start(getActivity(), "http://rct.club.inwi.ma/sites/default/files/reglement.pdf");

        Intent intent = new Intent(getActivity(), PdfViewerActivity.class);
        intent.putExtra("ViewType", "internet");
        startActivity(intent);
    }

    private void setupAndStartCountdown() {
        if (giftEndDate != null && !giftEndDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            // String oldTime = "19.02.2018, 12:00";//Timer date 1
            // String NewTime = "20.02.2018, 14:00";//Timer date 2
            try {
                Date endDate = formatter.parse(giftEndDate);
                // newDate = formatter.parse(NewTime);
                long endLong = endDate.getTime();
                long nowLong = new Date().getTime();
                long diff = endLong - nowLong;

                achouraCountdown = new AchouraCountdown(diff, 1000);
                achouraCountdown.start();

                if (viewCountdown.getVisibility() != View.VISIBLE) {
                    viewCountdown.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                Log.e(TAG, "setupAndStartCountdown: ", e);
            }
        } else if (viewCountdown.getVisibility() == View.VISIBLE) {
            viewCountdown.setVisibility(View.GONE);
        }
    }

    private void disposeCountdown() {
        if (achouraCountdown != null) {
            try {
                achouraCountdown.cancel();
            } catch (Exception e) {
                Log.e(TAG, "disposeCountdown: ", e);
            }
        }
    }

    // ******************************************

    public class AchouraCountdown extends CountDownTimer {

        AchouraCountdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            viewCountdown.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;

            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

            // System.out.println("d: " + days + "; hr: " + hours + "; mn: " + minutes + "; sec" + seconds);

            if (days > 0) {
                tvDays.setText(AchouraUtils.compensateWithZero(days));
            } else {
                rlDays.setVisibility(View.GONE);
            }

            if (hours > 0) {
                tvHours.setText(AchouraUtils.compensateWithZero(hours));
            } else {
                rlHours.setVisibility(View.GONE);
            }

            if (minutes >= 0) {
                tvMinutes.setText(AchouraUtils.compensateWithZero(minutes));
            } else {
                rlMinutes.setVisibility(View.GONE);
            }

            if (seconds >= 0) {
                tvSeconds.setText(AchouraUtils.compensateWithZero(seconds));
            } /*else {
                rlSeconds.setVisibility(View.GONE);
            }*/


            /*String hms = (TimeUnit.MILLISECONDS.toDays(millis)) + "Day "
                    + (TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)) + ":")
                    + (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)) + ":"
                    + (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
            txtNumber1.setText(hms);*/
        }
    }
}
