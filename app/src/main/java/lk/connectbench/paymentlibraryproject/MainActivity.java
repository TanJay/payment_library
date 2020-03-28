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
    Button btnPaywOTrans;
    Button btnPaywTrans;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPay = findViewById(R.id.button);
        btnPaywOTrans = findViewById(R.id.button3);
        btnPaywTrans = findViewById(R.id.button2);
        text = findViewById(R.id.editText);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GeniePayment.processPayment(MainActivity.this, text.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnPaywOTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GeniePayment.processSaveCard(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnPaywTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GeniePayment.processSaveCardWithInitialTransaction(MainActivity.this, text.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        GeniePayment.getListner(this).getCurrentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String name) {
                Log.d("Genie - Result", "name");
                Log.d("Genie - Result", "name");
                if(name.equals("1")){
                    GeniePayment.dismiss();
                }
            }
        });

    }
}
