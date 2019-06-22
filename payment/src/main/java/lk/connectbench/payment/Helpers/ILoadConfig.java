package lk.connectbench.payment.Helpers;

import android.content.Context;

import lk.connectbench.payment.DTOs.AppConfig;

public interface ILoadConfig {

    AppConfig load(Context context, String amount);

}
