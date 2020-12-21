/*
 * Copyright 2020 Andreas Dangel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
