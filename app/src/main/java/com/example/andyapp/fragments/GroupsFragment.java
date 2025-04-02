package com.example.andyapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.MainActivity;
import com.example.andyapp.R;
import com.example.andyapp.RecyclerViewSpacingDecorator;
import com.example.andyapp.adapters.Groups_RecyclerViewAdapter;
import com.example.andyapp.models.GroupsModel;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.Connections;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


public class GroupsFragment extends Fragment {
    interface GetConnections{
        @GET("users/{userId}/connections")
        Call<String[]>getConnections(@Header("Authorization Bearer: ") String token, @Path("userId") String userId);
    }
    RecyclerView groupsRecycler;
    ArrayList<GroupsModel> groupsModels;
    Groups_RecyclerViewAdapter adapter;
    ItemTouchHelper.SimpleCallback simpleCallback;
    RecyclerViewSpacingDecorator spacingDecorator;
    Bundle bundle;
    String userId;
    String token;
    private final String TAG = "LOGCAT";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupsRecycler = view.findViewById(R.id.groupsRecycler);
        spacingDecorator = new RecyclerViewSpacingDecorator(40);
        groupsModels = new ArrayList<>();
        groupsRecycler.addItemDecoration(spacingDecorator);
        bundle = getArguments();
        if (bundle!=null){
            userId = bundle.getString(LoginActivity.USERKEY);
            token = bundle.getString(LoginActivity.TOKENKEY);
        }
        Log.d(TAG, String.format("USERID IN GROUPS %s", userId));
        Log.d(TAG, String.format("TOKEN IN GROUPS %s", token));
        setUpGroupsModels("67ea78b67a03cd48a9aa3313");
        adapter = new Groups_RecyclerViewAdapter(groupsModels, view.getContext());
        groupsRecycler.setAdapter(adapter);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        simpleCallback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                int position = viewHolder.getBindingAdapterPosition();
                String name = groupsModels.get(position).getName();
                builder.setTitle(String.format("Delete connection with %s", name));
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        groupsModels.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyItemChanged(position);
                    }
                });
                builder.show();
            }
            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.delete_red))
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(groupsRecycler);
    }

    private void setUpGroupsModels(String userID){
        /*String[] connections = {"Herbert", "Philbert", "Jacob"};
        for (String connection: connections){
            groupsModels.add(new GroupsModel(connection, R.drawable.avatar));
        }*/
        GetConnections connectionsService = RetrofitClient.getRetrofit().create(GetConnections.class);
        connectionsService.getConnections(token, userId).enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                if (response.body() != null){
                    for (String connection: response.body()){
                        Log.d(TAG, connection);
                        groupsModels.add(new GroupsModel(connection, R.drawable.avatar));
                    }
                }else{
                    try {
                        String error = response.errorBody().string();
                        Log.e(TAG, "Response code: " + response.code());
                        Log.e(TAG, "Error body: " + error);
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                }
            }
            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                if (t.getMessage() != null) {
                    Log.d(TAG, t.getMessage());
                }
            }
        });

    }
}