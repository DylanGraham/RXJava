package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private StuffGenerator stuffGenerator;
    private Subscription stuffSubscription;
    @BindView(R.id.stuff_text) TextView stuffText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        stuffGenerator = new StuffGenerator(this);
        configureLayout();
        //createObservable();
        createNumbersObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void createObservable() {
        Observable<String> stuffObservable =
                Observable.fromCallable(() ->
                        stuffGenerator.generateStuff()
                );

        stuffSubscription = stuffObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::displayStuff,
                        (Throwable t) -> Timber.d(t.getMessage()),
                        this::onCompleted
                );
    }

    private void createNumbersObservable() {
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Long i) -> displayStuff(i.toString()));
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
        if (stuffSubscription != null && !stuffSubscription.isUnsubscribed()) {
            stuffSubscription.unsubscribe();
        }
    }
}
