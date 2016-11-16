package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private CompositeSubscription allSubscriptions = new CompositeSubscription();
    @BindView(R.id.stuff_text)
    TextView stuffText;
    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createNumbersObservable();
    }

    private void createNumbersObservable() {
        Subscription subscription = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> displayStuff(i.toString()));

        allSubscriptions.add(subscription);
    }

    @OnClick(R.id.floating_action_button)
    public void clickFAB() {
        Observable.just("Clicked")
                .subscribe(this::displayStuff);
    }

    private void displayStuff(String stuff) {
        stuffText.setText(stuff);
    }

    private void onCompleted() {
        Timber.d("onCompleted()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (allSubscriptions.hasSubscriptions() && !allSubscriptions.isUnsubscribed()) {
            allSubscriptions.unsubscribe();
        }
    }
}
