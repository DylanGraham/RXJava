package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private StuffGenerator stuffGenerator;
    private Subscription stuffSubscription;
    @BindView(R.id.stuff_text) TextView stuffText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stuffGenerator = new StuffGenerator(this);
        configureLayout();
        createObservable();
    }

    private void createObservable() {
        Observable<List<String>> stuffObservable =
                Observable.fromCallable(() -> stuffGenerator.generateStuff());

        stuffSubscription = stuffObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> displayStuff(s));
    }

    private void displayStuff(List<String> stuff) {
        stuffText.setText(stuff.toString());
    }

    private void configureLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stuffSubscription != null && ! stuffSubscription.isUnsubscribed()) {
            stuffSubscription.unsubscribe();
        }
    }
}
