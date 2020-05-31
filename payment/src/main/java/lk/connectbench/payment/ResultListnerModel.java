package lk.connectbench.payment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
