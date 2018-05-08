package com.example.diamond.reconrobot.Service;

import android.content.Context;

import com.example.diamond.reconrobot.Model.CommandModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diamond on 14/3/2018 AD.
 */

public class CMDService {
    private Context context;
    List<CommandModel> list;

    public CMDService(Context context)
    {
        this.context = context;
    }

    public List<CommandModel> getAllCommand()
    {
        
        list = new ArrayList<>();
        return list;
    }
}
