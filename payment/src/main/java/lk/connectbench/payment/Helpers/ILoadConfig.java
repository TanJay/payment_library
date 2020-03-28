package lk.connectbench.payment.Helpers;

import android.content.Context;

import lk.connectbench.payment.DTOs.AppConfig;

public interface ILoadConfig<T> {

    T load(Context context, String amount);

}
