package com.day1assignment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.day1assignment.Card.SliderAdapter;
import com.day1assignment.CardSlider.CardSliderLayoutManager;
import com.day1assignment.CardSlider.CardSnapHelper;
import com.day1assignment.Interface.MainView;
import com.day1assignment.Model.CardModelClass;
import com.day1assignment.Presenter.MainPresenterImpl;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private SliderAdapter sliderAdapter;
    private final int[] pics = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5};
    private CardSliderLayoutManager layoutManger;
    private RecyclerView mRecyclerView;
    private MainPresenterImpl mMainPresenter;
    private int currentPosition;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;
    private List<CardModelClass> cardModelClassList;


    private TextView Catogry1TextView;
    private TextView Catogry2TextView;
    private int CatogryOffset1;
    private int CatogryOffset2;
    private long CatogryAnimDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
    }

    public void init() {
        placeSwitcher = findViewById(R.id.ts_title);
        clockSwitcher = findViewById(R.id.ts_clock);
        Catogry1TextView = findViewById(R.id.ts_cat_1);
        Catogry2TextView = findViewById(R.id.ts_cat_2);
        mRecyclerView = findViewById(R.id.recycler_view);
        descriptionsSwitcher = findViewById(R.id.ts_description);


    }

    private void initSwitchers(List<CardModelClass> list) {

        placeSwitcher.setFactory(new TextViewFactory(R.style.TitleTextView, false));
        placeSwitcher.setCurrentText(list.get(0).getTitle());

        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));
        clockSwitcher.setCurrentText(list.get(0).getPubdate());

        descriptionsSwitcher.setInAnimation(this, android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));
        descriptionsSwitcher.setCurrentText(list.get(0).getLink());


    }

    private void initCatogryText(List<CardModelClass> list) {
        CatogryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        CatogryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        CatogryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        Catogry1TextView.setX(CatogryOffset1);
        Catogry2TextView.setX(CatogryOffset2);
        Catogry1TextView.setText(list.get(0).getCat());
        Catogry2TextView.setAlpha(0f);
        Catogry1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
        Catogry2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
    }


    private class TextViewFactory implements ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(MainActivity.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainActivity.this, styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        mMainPresenter = new MainPresenterImpl(this);
        mMainPresenter.getDataForList(getApplicationContext());


    }

    private void initRecyclerView() {
        sliderAdapter = new SliderAdapter(pics, cardModelClassList.size(), new OnCardClickListener());

        mRecyclerView.setAdapter(sliderAdapter);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) mRecyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(mRecyclerView);

    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        setCatogryText(cardModelClassList.get(pos).getCat(), left2right);

        placeSwitcher.setInAnimation(MainActivity.this, animV[0]);
        placeSwitcher.setOutAnimation(MainActivity.this, animV[1]);
        placeSwitcher.setText(cardModelClassList.get(pos).getTitle());

        clockSwitcher.setInAnimation(MainActivity.this, animV[0]);
        clockSwitcher.setOutAnimation(MainActivity.this, animV[1]);
        clockSwitcher.setText(cardModelClassList.get(pos).getPubdate());

        descriptionsSwitcher.setText(cardModelClassList.get(pos).getLink());


        currentPosition = pos;
    }

    private void setCatogryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (Catogry1TextView.getAlpha() > Catogry2TextView.getAlpha()) {
            visibleText = Catogry1TextView;
            invisibleText = Catogry2TextView;
        } else {
            visibleText = Catogry2TextView;
            invisibleText = Catogry1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = CatogryOffset2;
        } else {
            invisibleText.setX(CatogryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", CatogryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(CatogryAnimDuration);
        animSet.start();
    }

    @Override
    public void onGetDataSuccess(List<CardModelClass> list) {
        cardModelClassList = list;
        initRecyclerView();
        initSwitchers(list);
        initCatogryText(list);

    }

    @Override
    public void onGetDataFailure(String message) {
        Log.d("Failure Message", message);
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm = (CardSliderLayoutManager) mRecyclerView.getLayoutManager();

            assert lm != null;
            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = mRecyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(MainActivity.this, SecondScreen.class);
                 intent.putExtra(SecondScreen.URLTAG, cardModelClassList.get(activeCardPosition).getLink());

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                    MainActivity.this.overridePendingTransition(0,0);
                } else {
                    final CardView cardView = (CardView) view;
                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                    final ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this, sharedView, "shared");
                    startActivity(intent, options.toBundle());
                    MainActivity.this.overridePendingTransition(0,0);
                }
            } else if (clickedPosition > activeCardPosition) {
                mRecyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

}
