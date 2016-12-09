package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable allDisposables;
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
        allDisposables = new CompositeDisposable();

        Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> displayStuff(i.toString()));
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
        allDisposables.clear();
    }
}
