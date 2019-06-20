package lk.connectbench.payment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ResultListnerModel extends ViewModel {
    private MutableLiveData<String> currentName;

    public MutableLiveData<String> getCurrentName(){
        if(currentName == null){
            currentName = new MutableLiveData<>();
        }
        return currentName;
    }
}
