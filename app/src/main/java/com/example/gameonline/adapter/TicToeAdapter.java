package com.example.gameonline.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameonline.R;
import com.example.gameonline.listener.TicToeClickListener;
import com.example.gameonline.model.Game;
import com.example.gameonline.model.GamePlay;
import com.example.gameonline.model.TicToe;
import com.example.gameonline.util.Data;

public class TicToeAdapter extends RecyclerView.Adapter<TicToeAdapter.TicToeViewHolder> {
    private Game game;
    private int[][] board;
    private TicToeClickListener ticToeClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setGame(Game game) {
        this.game = game;
        this.board=game.getBoard();
        notifyDataSetChanged();
    }

    public TicToeAdapter(Game game, TicToeClickListener ticToeClickListener) {
        this.game = game;
        board=game.getBoard();
        this.ticToeClickListener=ticToeClickListener;
    }

    @NonNull
    @Override
    public TicToeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicToeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tictoe_item, parent, false));
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void onBindViewHolder(@NonNull TicToeViewHolder holder, int position) {
        int rows = board.length;
        int cols = board[0].length;

        int row = position / cols;
        int col = position % cols;
        TicToe type;
        int data=board[row][col];
        if(data==0){
            type=null;
        } else if (data==1) {
            type=TicToe.X;
        }else {
            type=TicToe.O;
        }
        holder.textView.setText(type != null ? type.name() : "");
        holder.container.setOnClickListener(v -> {
            Log.d("p","row:"+row+" col:"+col);
            TicToe ticToe;
            if(Data.isMaster){
                ticToe=TicToe.X;
            }else {
                ticToe=TicToe.O;
            }
            GamePlay gamePlay=new GamePlay(ticToe,row,col,game.getGameId());
            ticToeClickListener.onClick(gamePlay);
        });

    }

    @Override
    public int getItemCount() {
        return  board.length * board[0].length;
    }

    public static class TicToeViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ConstraintLayout container;

        public TicToeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tvTicToe);
            container=itemView.findViewById(R.id.clContainer);
        }
    }
}
