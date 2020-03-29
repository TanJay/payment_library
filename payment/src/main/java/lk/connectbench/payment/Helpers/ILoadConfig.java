package lk.connectbench.payment.Helpers;

import android.content.Context;

public interface ILoadConfig<T> {

    T load(Context context, String amount);

}
