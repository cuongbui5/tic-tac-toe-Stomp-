package com.example.gameonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gameonline.activity.GameActivity;
import com.example.gameonline.api.ApiService;
import com.example.gameonline.model.Game;
import com.example.gameonline.model.Player;
import com.example.gameonline.util.Data;
import com.example.gameonline.util.Helper;
import com.example.gameonline.util.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etPlayerName;
    private Button btnCreateRoom;
    private Button btnLoadGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPlayerName=findViewById(R.id.etPlayerName);
        btnCreateRoom=findViewById(R.id.btnCreateRoom);
        btnLoadGame=findViewById(R.id.btnLoadGame);
        ApiService apiService= RetrofitClient.getInstance().getRetrofit().create(ApiService.class);
        btnCreateRoom.setOnClickListener(v -> {
            String playerName=etPlayerName.getText().toString().trim();
            if(playerName.equals("")){
                Toast.makeText(MainActivity.this,"Please enter your name!",Toast.LENGTH_SHORT).show();
                return;
            }
            Data.isMaster=true;

            apiService.start(new Player(playerName)).enqueue(new Callback<Game>() {
                @Override
                public void onResponse(Call<Game> call, Response<Game> response) {
                    if(response.isSuccessful()){
                        Game game=response.body();
                        Intent intent=new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("game_data",game);
                        startActivity(intent);

                    }else {
                        //Helper.handlerErrorResponse(response,MainActivity.this);
                        Toast.makeText(MainActivity.this,response.message().toString(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Game> call, Throwable t) {
                    Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
                }
            });


        });

        btnLoadGame.setOnClickListener(v -> {
            String playerName=etPlayerName.getText().toString().trim();
            if(playerName.equals("")){
                Toast.makeText(MainActivity.this,"Please enter your name!",Toast.LENGTH_SHORT).show();
                return;
            }
            Data.isMaster=false;
            apiService.connectRandom(new Player(playerName)).enqueue(new Callback<Game>() {
                @Override
                public void onResponse(Call<Game> call, Response<Game> response) {
                    if(response.isSuccessful()){
                        Game game=response.body();
                        Intent intent=new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("game_data",game);
                        startActivity(intent);
                    }else {

                    }
                }

                @Override
                public void onFailure(Call<Game> call, Throwable t) {

                }
            });
        });


    }


}