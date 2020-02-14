package com.app.efarmers.farmers.crops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.efarmers.R;

public class CropsActivity extends AppCompatActivity {

    CardView cardInfo1,cardInfo2,cardInfo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops);

        cardInfo1=findViewById(R.id.card_info1);
        cardInfo2=findViewById(R.id.card_info2);
        cardInfo3=findViewById(R.id.card_info3);


        cardInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CropsActivity.this,BijerJotnoActivity.class);
                startActivity(intent);

            }
        });

        cardInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CropsActivity.this,CardInfo2Activity.class);
                startActivity(intent);

            }
        });

        cardInfo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CropsActivity.this,CardInfo3Activity.class);
                startActivity(intent);

            }
        });
    }
}
