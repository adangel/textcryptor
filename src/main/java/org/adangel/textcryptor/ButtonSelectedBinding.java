package org.adangel.textcryptor;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Supplier;

import javax.swing.AbstractButton;

public class ButtonSelectedBinding implements Subscriber<Data> {
    protected final AbstractButton button;
    protected final Supplier<Boolean> supplier;

    public ButtonSelectedBinding(AbstractButton button, Supplier<Boolean> supplier) {
        this.button = button;
        this.supplier = supplier;
    }

    @Override
    public void onNext(Data item) {
        if (button.isSelected() != supplier.get()) {
            button.setSelected(supplier.get());
        }
    }

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
