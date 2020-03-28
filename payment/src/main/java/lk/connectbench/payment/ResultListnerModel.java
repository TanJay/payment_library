package lk.connectbench.payment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import lk.connectbench.payment.DTOs.TransactionResponse;

public class ResultListnerModel extends ViewModel {
    private MutableLiveData<TransactionResponse> currentName;

    public MutableLiveData<TransactionResponse> getCurrentName(){
        if(currentName == null){
            currentName = new MutableLiveData<>();
        }
        return currentName;
    }
}
