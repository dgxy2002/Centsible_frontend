package com.example.andyapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andyapp.DataSubject;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.R;
import com.example.andyapp.RecyclerViewSpacingDecorator;
import com.example.andyapp.adapters.GroupsRecyclerViewAdapter;
import com.example.andyapp.models.GroupsModel;
import com.example.andyapp.models.GroupsModels;
import com.example.andyapp.queries.GroupsService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


public class GroupsFragment extends Fragment {
    interface GetConnections{
        @GET("users/{userId}/connections")
        Call<String[]>getConnections(@Header("Authorization") String token, @Path("userId") String userId);
    }
    TextView nameView;
    RecyclerView groupsRecycler;
    GroupsModels groupsModels;
    GroupsRecyclerViewAdapter adapter;
    ItemTouchHelper.SimpleCallback simpleCallback;
    RecyclerViewSpacingDecorator spacingDecorator;
    String userId;
    String token;
    String username;
    String imageUrl;
    SharedPreferences mypref;
    Button btnConnect;
    EditText usernameEditText;
    ImageView avatarView;
    GroupsService groupsService;
    DataSubject<GroupsModels> subject;
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
        //Initialise Views and Models
        btnConnect = view.findViewById(R.id.btnConnect);
        usernameEditText = view.findViewById(R.id.addFriendEditText);
        groupsRecycler = view.findViewById(R.id.groupsRecycler);
        nameView = view.findViewById(R.id.nameView);
        avatarView = view.findViewById(R.id.avatarView);
        spacingDecorator = new RecyclerViewSpacingDecorator(40);
        subject = new DataSubject<>();
        groupsRecycler.addItemDecoration(spacingDecorator);
        //Shared Preferences
        mypref = requireContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mypref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        token = mypref.getString(LoginActivity.TOKENKEY, LoginActivity.DEFAULT_USERID);
        username = mypref.getString(LoginActivity.USERNAMEKEY, LoginActivity.DEFAULT_USERNAME);
        imageUrl = mypref.getString(LoginActivity.USERIMAGEKEY, LoginActivity.DEFAULT_IMAGE);
//        Log.d(TAG, String.format("USERID IN GROUPS %s", userId));
//        Log.d(TAG, String.format("TOKEN IN GROUPS %s", token));
        //Set Up GroupsService for model querying
        groupsService = new GroupsService(requireContext());
        //Set Up Views
        nameView.setText(String.format("%s%s",username.substring(0, 1).toUpperCase(),username.substring(1)));
        Glide.with(requireContext()).load(imageUrl).circleCrop().into(avatarView);
        //Set Up Buttons
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inviteeUsername = usernameEditText.getText().toString();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, username);
                        Log.d(TAG, inviteeUsername);
                        groupsService.addConnection(username, inviteeUsername);
                    }
                });
                //TODO Finish setting up the get friend, and option to view get friend request.
            }
        });
        //Set up Recycler View
        setUpGroupsModels(); //Get data
        adapter = new GroupsRecyclerViewAdapter(new ArrayList<GroupsModel>(), view.getContext());
        subject.registerObserver(adapter);
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
                ArrayList<GroupsModel> models = groupsModels.getGroupsModels();
                String name = models.get(position).getName();
                String connectionId = models.get(position).getUserId();
                builder.setTitle(String.format("Delete connection with %s", name));
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        models.remove(position);
                        adapter.notifyItemRemoved(position);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                groupsService.deleteConnection(userId, connectionId);
                            }
                        });
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

    private void setUpGroupsModels(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                groupsModels = groupsService.getConnections(userId, handler, subject);
            }
        });
    }

}