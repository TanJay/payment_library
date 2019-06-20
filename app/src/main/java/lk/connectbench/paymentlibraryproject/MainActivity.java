package lk.connectbench.paymentlibraryproject;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lk.connectbench.payment.GeniePayment;


public class MainActivity extends AppCompatActivity {
    Button btnPay;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPay = findViewById(R.id.button);
        text = findViewById(R.id.editText);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GeniePayment.Process(MainActivity.this, text.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        GeniePayment.getListner(this).getCurrentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String name) {
                Log.d("Genie - Result", "name");
                if(name.equals("1")){
                    GeniePayment.dismiss();
                }
            }
        });

    }
}
