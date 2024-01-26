package com.example.gameonline.util;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Response;

public class Helper {
    public static void handlerErrorResponse(Response response, Context context){
        Toast.makeText(context,response.message().toString(),Toast.LENGTH_SHORT);
    }
}
