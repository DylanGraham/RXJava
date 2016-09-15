package org.dylangraham.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private StuffGenerator stuffGenerator;
    private Subscription stuffSubscription;

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
                .subscribe(new Observer<List<String>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(List<String> stuff) {
                                   displayStuff(stuff);
                               }
                           });
    }

    private void displayStuff(List<String> stuff) {
        TextView text = (TextView) findViewById(R.id.stuff_text);
        text.setText(stuff.toString());
    }

    private void configureLayout() {
        setContentView(R.layout.activity_main);
    }
}
