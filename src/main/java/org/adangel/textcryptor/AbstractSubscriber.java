package org.adangel.textcryptor;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public abstract class AbstractSubscriber implements Subscriber<Data> {
    @Override
    public void onSubscribe(Subscription subscription) {
        // do nothing
    }

    @Override
    public void onError(Throwable throwable) {
        // do nothing
    }

    @Override
    public void onComplete() {
        // do nothing
    }

}
