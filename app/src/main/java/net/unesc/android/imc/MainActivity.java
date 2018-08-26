package net.unesc.android.imc;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private EditText edtWeight;
    private EditText edtHeight;
    private Button btnCalc;
    private Button btnClear;
    private TextView lbResultWeight;
    private TextView lbResultImc;
    private TextView lbResultAbstract;

    private LinearLayout layResult;

    private Double heightCm;
    private Double weightKg;
    private Double heightM;
    private Double weightResult;
    private Double imc;
    private String lbAbstract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtWeight = findViewById(R.id.edWeight);
        edtHeight = findViewById(R.id.edHeight);
        btnCalc = findViewById(R.id.btCalc);
        btnClear = findViewById(R.id.btClear);
        lbResultWeight = findViewById(R.id.lbResultWeight);
        lbResultImc = findViewById(R.id.lbResultImc);
        lbResultAbstract = findViewById(R.id.lbResultAbstract);
        layResult = findViewById(R.id.layResult);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        lbResultWeight.setText(preferences.getString("lb_result_weight",""));
        lbResultImc.setText(preferences.getString("lb_result_imc",""));
        lbResultAbstract.setText(preferences.getString("lb_result_abstract",""));

        if(lbResultAbstract.getText().toString().isEmpty()) {
            layResult.setVisibility(View.INVISIBLE);
        }

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heightM = new Double(edtHeight.getText().toString());
                heightCm = heightM * 100;
                weightKg = new Double(edtWeight.getText().toString());

                weightResult = new Double((heightCm - 100) - ((heightCm - weightKg) / 4) * (5/100));
                imc = Math.ceil(weightKg / Math.pow(heightM, 2));

                lbAbstract = "";
                if(imc < 20){
                    lbAbstract = "Baixo peso";
                } else if(imc < 25){
                    lbAbstract = "Normal";
                } else if(imc >= 25 && imc <= 30){
                    lbAbstract = "Acima do peso";
                } else {
                    lbAbstract = "Obeso";
                }

                lbResultWeight.setText(weightResult.toString());
                lbResultImc.setText(imc.toString());
                lbResultAbstract.setText(lbAbstract.toString());

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor edit = preferences.edit();

                edit.putString("lb_result_weight", weightResult.toString());
                edit.putString("lb_result_imc", imc.toString());
                edit.putString("lb_result_abstract", lbAbstract.toString());

                edit.apply();

                layResult.setVisibility(View.VISIBLE);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lbResultAbstract.setText("");
                lbResultImc.setText("");
                lbResultWeight.setText("");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor edit = preferences.edit();

                edit.remove("lb_result_weight");
                edit.remove("lb_result_imc");
                edit.remove("lb_result_abstract");

                edit.apply();

                layResult.setVisibility(View.INVISIBLE);

            }
        });
    }
}
