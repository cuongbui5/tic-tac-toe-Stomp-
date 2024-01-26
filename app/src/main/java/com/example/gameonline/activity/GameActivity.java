package com.example.gameonline.activity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gameonline.R;
import com.example.gameonline.adapter.TicToeAdapter;
import com.example.gameonline.api.ApiService;
import com.example.gameonline.listener.TicToeClickListener;
import com.example.gameonline.model.Game;
import com.example.gameonline.model.GamePlay;
import com.example.gameonline.util.RetrofitClient;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;


public class GameActivity extends AppCompatActivity implements TicToeClickListener {
    private RecyclerView board;
    private TextView player1,player2;
    private Game game;
    private TextView tvMessage;
    private StompClient stompClient;
    private Button btnRematch;

    private Gson gson = new Gson();
    private Disposable disposable;
    private Disposable disposableSend;
    private TicToeAdapter ticToeAdapter;


    @SuppressLint("NotifyDataSetChanged")
    private void initViews(){
        board=findViewById(R.id.rvBoard);
        player1=findViewById(R.id.tvPlayer1);
        player2=findViewById(R.id.tvPlayer2);
        tvMessage=findViewById(R.id.tvMessage);
        btnRematch=findViewById(R.id.btnRematch);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        board.setLayoutManager(gridLayoutManager);
        btnRematch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_game);
        initViews();
        Intent intent=getIntent();
        if (intent != null &&android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            game=intent.getSerializableExtra("game_data", Game.class);

        }
        ticToeAdapter=new TicToeAdapter(game, this);

        board.setAdapter(ticToeAdapter);
        if(game!=null){
            player1.setText(game.getPlayer1().getName());
            if(game.getPlayer2()==null){
                player2.setText("await...");
            }else {
                player2.setText(game.getPlayer2().getName());
            }

        }
        //192.168.175.6



        stompClient= Stomp.over(Stomp.ConnectionProvider.JWS,"ws://192.168.175.6:8081/ws/websocket");
        stompClient.connect();
        disposable = stompClient.topic("/topic/playing/"+game.getGameId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    game=gson.fromJson(stompMessage.getPayload(), Game.class);
                    Log.d("game",stompMessage.getPayload());
                    Toast.makeText(GameActivity.this,"Hello",Toast.LENGTH_SHORT).show();

                    if(player1.getText()==""){
                        player1.setText(game.getPlayer1().getName());
                    }

                    if(player2.getText()=="await..."){
                        player2.setText(game.getPlayer2().getName());
                    }
                    if(ticToeAdapter==null){
                        ticToeAdapter=new TicToeAdapter(game,this);

                    }else {
                        ticToeAdapter.setGame(game);
                    }



                    board.setAdapter(ticToeAdapter);
                    if(game.getWinner()!=null){
                        tvMessage.setText(game.getWinner().toString()+"win!");
                        btnRematch.setVisibility(View.VISIBLE);


                    }
                }, throwable -> {
                    // Handle the error here
                    Log.e("StompError", "Error in subscription", throwable);
                });






    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
        if(disposableSend!=null&&!disposableSend.isDisposed()){
            disposableSend.dispose();
        }
    }



    @Override
    public void onClick(GamePlay gamePlay) {
        if(!stompClient.isConnected()){
            stompClient.connect();

        }
        if(gamePlay.getType()==game.getStatusPlayer()){
            disposableSend= stompClient.send("/app/gameplay",gson.toJson(gamePlay)).subscribe();
        }else {
            Toast.makeText(this,"Not my turn",Toast.LENGTH_SHORT).show();
        }


    }
}
