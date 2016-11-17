package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

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
    private CompositeSubscription allSubscriptions;
    @BindView(R.id.stuff_text)
    TextView stuffText;
    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;
    @BindView(R.id.edit_text)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createObservables();
    }

    private void createObservables() {
        allSubscriptions = new CompositeSubscription();

        Subscription sub1 = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> displayStuff(i.toString()));

        Subscription sub2 = RxView.clicks(fab)
                .subscribe(call -> displayStuff("CLICK CLICK"),
                        (Throwable t) -> Timber.d(t.getMessage()),
                        this::onCompleted);

        Subscription sub3 = RxTextView.textChanges(editText)
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(this::displayStuff,
                        (Throwable t) -> Timber.d(t.getMessage()),
                        this::onCompleted);

        allSubscriptions.addAll(sub1, sub2, sub3);
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
        allSubscriptions.unsubscribe();
    }
}
