package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private StuffGenerator stuffGenerator;
    private Subscription stuffSubscription;
    @BindView(R.id.stuff_text)
    TextView stuffText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        stuffGenerator = new StuffGenerator(this);
        configureLayout();
        createObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void createObservable() {
        Observable<String> stuffObservable =
                Observable.fromCallable(() -> stuffGenerator.generateStuff());

        stuffSubscription = stuffObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::displayStuff,
                        (Throwable t) -> Timber.d(t.getMessage()),
                        () -> Timber.d("onCompleted()")
                );
    }

    private void displayStuff(String stuff) {
        stuffText.setText(stuff);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stuffSubscription != null && !stuffSubscription.isUnsubscribed()) {
            stuffSubscription.unsubscribe();
        }
    }
}
