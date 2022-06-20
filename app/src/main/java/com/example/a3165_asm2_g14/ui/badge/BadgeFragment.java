package com.example.a3165_asm2_g14.ui.badge;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a3165_asm2_g14.DatabaseHelper;
import com.example.a3165_asm2_g14.R;
import com.example.a3165_asm2_g14.databinding.FragmentBadgeBinding;

import java.util.ArrayList;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.os.Handler;


public class BadgeFragment extends Fragment {

    //BadgeFragment components
    private TextView tv_trophy_NotOwned1, tv_trophy_NotOwned2, tv_trophy_NotOwned3, tv_trophy_NotOwned4, tv_trophy_NotOwned5, tv_trophy_NotOwned6,
            tv_trophy_Owned1, tv_trophy_Owned2, tv_trophy_Owned3, tv_trophy_Owned4, tv_trophy_Owned5, tv_trophy_Owned6,
            tv_badge_not_owned, tv_badge_owned;
    private ImageView img_trophy_NotOwned1, img_trophy_NotOwned2, img_trophy_NotOwned3, img_trophy_NotOwned4, img_trophy_NotOwned5, img_trophy_NotOwned6,
            img_trophy_Owned1, img_trophy_Owned2, img_trophy_Owned3, img_trophy_Owned4, img_trophy_Owned5, img_trophy_Owned6;
    private View ll_trophy_NotOwned1, ll_trophy_NotOwned2, ll_trophy_NotOwned3, ll_trophy_NotOwned4, ll_trophy_NotOwned5, ll_trophy_NotOwned6,
            ll_trophy_Owned1, ll_trophy_Owned2, ll_trophy_Owned3, ll_trophy_Owned4, ll_trophy_Owned5, ll_trophy_Owned6;
    private ImageView iv_congratulation;
    private boolean flag=false;
    public static final int animation_time = 1000;
    public static int ScreenHeight, ScreenWidth=2000;
    private DatabaseHelper recordDB;
    private ArrayList<String> record_id, record_date, record_type, record_amount, record_category, record_method, record_note;


    //define if trophy is unlocked
    public boolean trophy1 = false;
    public boolean trophy2 = false;
    public boolean trophy3 = false;
    public boolean trophy4 = false;
    public boolean trophy5 = false;
    public boolean trophy6 = false;

    public float incomeSum;
    public float expenseSum;
    public float Esum;
    public boolean Used = false;

    private BadgeViewModel badgeViewModel;
    private FragmentBadgeBinding binding;

    private String shareText = "I have unlocked  in my money saving app: ";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BadgeViewModel badgeViewModel =
                new ViewModelProvider(this).get(BadgeViewModel.class);

        binding = FragmentBadgeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        iv_congratulation=root.findViewById(R.id.iv_congratulations);

        //Database
        recordDB = new DatabaseHelper(getContext());
        record_id = new ArrayList<>();
        record_date = new ArrayList<>();
        record_type = new ArrayList<>();
        record_amount = new ArrayList<>();
        record_category = new ArrayList<>();
        record_method = new ArrayList<>();
        record_note = new ArrayList<>();
        getDataInDB();

        tv_trophy_NotOwned1 = root.findViewById(R.id.tv_trophy_NotOwned1);
        tv_trophy_NotOwned2 = root.findViewById(R.id.tv_trophy_NotOwned2);
        tv_trophy_NotOwned3 = root.findViewById(R.id.tv_trophy_NotOwned3);
        tv_trophy_NotOwned4 = root.findViewById(R.id.tv_trophy_NotOwned4);
        tv_trophy_NotOwned5 = root.findViewById(R.id.tv_trophy_NotOwned5);
        tv_trophy_NotOwned6 = root.findViewById(R.id.tv_trophy_NotOwned6);

        tv_trophy_Owned1 = root.findViewById(R.id.tv_trophy_Owned1);
        tv_trophy_Owned2 = root.findViewById(R.id.tv_trophy_Owned2);
        tv_trophy_Owned3 = root.findViewById(R.id.tv_trophy_Owned3);
        tv_trophy_Owned4 = root.findViewById(R.id.tv_trophy_Owned4);
        tv_trophy_Owned5 = root.findViewById(R.id.tv_trophy_Owned5);
        tv_trophy_Owned6 = root.findViewById(R.id.tv_trophy_Owned6);

        tv_badge_not_owned = root.findViewById(R.id.tv_badge_not_owned);
        tv_badge_owned = root.findViewById(R.id.tv_badge_owned);

        img_trophy_NotOwned1 = root.findViewById(R.id.img_trophy_NotOwned1);
        img_trophy_NotOwned2 = root.findViewById(R.id.img_trophy_NotOwned2);
        img_trophy_NotOwned3 = root.findViewById(R.id.img_trophy_NotOwned3);
        img_trophy_NotOwned4 = root.findViewById(R.id.img_trophy_NotOwned4);
        img_trophy_NotOwned5 = root.findViewById(R.id.img_trophy_NotOwned5);
        img_trophy_NotOwned6 = root.findViewById(R.id.img_trophy_NotOwned6);

        img_trophy_Owned1 = root.findViewById(R.id.img_trophy_Owned1);
        img_trophy_Owned2 = root.findViewById(R.id.img_trophy_Owned2);
        img_trophy_Owned3 = root.findViewById(R.id.img_trophy_Owned3);
        img_trophy_Owned4 = root.findViewById(R.id.img_trophy_Owned4);
        img_trophy_Owned5 = root.findViewById(R.id.img_trophy_Owned5);
        img_trophy_Owned6 = root.findViewById(R.id.img_trophy_Owned6);

        ll_trophy_NotOwned1 = root.findViewById(R.id.ll_trophy_NotOwned1);
        ll_trophy_NotOwned2 = root.findViewById(R.id.ll_trophy_NotOwned2);
        ll_trophy_NotOwned3 = root.findViewById(R.id.ll_trophy_NotOwned3);
        ll_trophy_NotOwned4 = root.findViewById(R.id.ll_trophy_NotOwned4);
        ll_trophy_NotOwned5 = root.findViewById(R.id.ll_trophy_NotOwned5);
        ll_trophy_NotOwned6 = root.findViewById(R.id.ll_trophy_NotOwned6);

        ll_trophy_Owned1 = root.findViewById(R.id.ll_trophy_Owned1);
        ll_trophy_Owned2 = root.findViewById(R.id.ll_trophy_Owned2);
        ll_trophy_Owned3 = root.findViewById(R.id.ll_trophy_Owned3);
        ll_trophy_Owned4 = root.findViewById(R.id.ll_trophy_Owned4);
        ll_trophy_Owned5 = root.findViewById(R.id.ll_trophy_Owned5);
        ll_trophy_Owned6 = root.findViewById(R.id.ll_trophy_Owned6);

        for (int i = 0; i < record_id.size(); i++) {
            if (record_type.get(i).equals("Income")) {
                trophy1 = true;
                incomeSum += Float.parseFloat(record_amount.get(i));

            }
            if (record_type.get(i).equals("Expense")) {
                trophy2 = true;
                expenseSum += Float.parseFloat((record_amount.get(i)));
                if (record_method.get(i).equals("E-Payment")) {
                    Esum += Float.parseFloat((record_amount.get(i)));
                }
            }

            if (i == 4) {
                Used = true;
            }

            if (i == 19) {
                trophy6 = true;
            }

        }

        if ((incomeSum - expenseSum) >= 1000.0) {
            trophy3 = true;
        }

        if ((incomeSum != 0) && (incomeSum / 2.0 >= expenseSum) && Used) {
            trophy4 = true;
        }

        if ((Esum != 0) && (Esum >= expenseSum / 2.0) && Used) {
            trophy5 = true;
        }


        if (trophy1 || trophy2 || trophy3 || trophy4 || trophy5 || trophy6) {
            tv_badge_owned.setVisibility(View.VISIBLE);
            flag=true;
        }

        if (trophy1 && trophy2 && trophy3 && trophy4 && trophy5 && trophy6) {
            tv_badge_not_owned.setVisibility(View.GONE);
        }

        if (trophy1) {
            ll_trophy_NotOwned1.setVisibility(View.GONE);
            ll_trophy_Owned1.setVisibility(View.VISIBLE);
            shareText += "\nFirst income ";
        }

        if (trophy2) {
            ll_trophy_NotOwned2.setVisibility(View.GONE);
            ll_trophy_Owned2.setVisibility(View.VISIBLE);
            shareText += "\nFirst expense ";
        }

        if (trophy3) {
            ll_trophy_NotOwned3.setVisibility(View.GONE);
            ll_trophy_Owned3.setVisibility(View.VISIBLE);
            shareText += "\nSaving $1000 ";
        }

        if (trophy4) {
            ll_trophy_NotOwned4.setVisibility(View.GONE);
            ll_trophy_Owned4.setVisibility(View.VISIBLE);
            shareText += "\nSaving 50% income ";
        }

        if (trophy5) {
            ll_trophy_NotOwned5.setVisibility(View.GONE);
            ll_trophy_Owned5.setVisibility(View.VISIBLE);
            shareText += "\nE-Payment master ";
        }

        if (trophy6) {
            ll_trophy_NotOwned6.setVisibility(View.GONE);
            ll_trophy_Owned6.setVisibility(View.VISIBLE);
            shareText += "\nGood saving habit ";
        }

        while(flag){
            AnimateSlideInOut(animation_time/2,
                    iv_congratulation,true,animation_time/2,
                    ScreenWidth);
            AnimateSlideInOut(animation_time*2,
                    iv_congratulation,false,animation_time/2,
                   ScreenWidth);
            flag=false;

        }
        check_display();


        Button btn_share = root.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent;
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share with"));
            }

        });
        return root;
    }

    public static void AnimateSlideInOut(final int DELAY, final ImageView ivDH,
                                         final Boolean IsIn, final int AnimDuration, final int dx) {
        if (DELAY == 0) {
            if (IsIn) {
                ivDH.setVisibility(View.VISIBLE);
            } else {
                ivDH.setVisibility(View.INVISIBLE);
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (IsIn) {
                        ivDH.setVisibility(View.VISIBLE);
                        Animation transAnimation = new TranslateAnimation(
                                Animation.ABSOLUTE+dx, Animation.ABSOLUTE,
                                Animation.ABSOLUTE, Animation.ABSOLUTE);
                        transAnimation.setDuration(AnimDuration);
                        AnimationSet animationSet1 = new AnimationSet(true);
                        animationSet1.addAnimation(transAnimation);
                        animationSet1.setDuration(500);
                        animationSet1
                                .setInterpolator(new DecelerateInterpolator());
                        ivDH.startAnimation(animationSet1);
                    } else {
                        ivDH.setVisibility(View.VISIBLE);
                        Animation transAnimation = new TranslateAnimation(
                                Animation.ABSOLUTE, Animation.ABSOLUTE + dx,
                                Animation.ABSOLUTE, Animation.ABSOLUTE);
                        transAnimation.setDuration(AnimDuration);
                        AnimationSet animationSet1 = new AnimationSet(true);
                        animationSet1.addAnimation(transAnimation);
                        animationSet1.setDuration(500);
                        animationSet1
                                .setInterpolator(new DecelerateInterpolator());
                        ivDH.startAnimation(animationSet1);
                        ivDH.setVisibility(View.INVISIBLE);
                        ivDH.setVisibility(View.GONE);
                    }
                }
            }, DELAY);
        }
    }
    public void check_display() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ScreenHeight = dm.heightPixels;
        ScreenWidth = dm.widthPixels;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        shareText = "I have unlocked achievements: ";
    }

    public void getDataInDB() {
        Cursor cursor = recordDB.readAllData();
        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
                record_id.add(cursor.getString(0));
                record_date.add(cursor.getString(1));
                record_type.add(cursor.getString(2));
                record_amount.add(cursor.getString(3));
                record_category.add(cursor.getString(4));
                record_method.add(cursor.getString(5));
                record_note.add(cursor.getString(6));
            }
        }
    }
}