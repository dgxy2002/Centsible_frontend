package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.andyapp.managers.ScaleCenterItemLayoutManager;

import com.example.andyapp.adapters.LogBd_RecyclerViewAdapter;
import com.example.andyapp.models.CategoryAllocation;
import com.example.andyapp.models.LogBudgetModel;
import com.example.andyapp.models.LogBudgetModels;
import com.example.andyapp.models.PostCategoryAllocation;
import com.example.andyapp.queries.BudgetService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogBudget extends AppCompatActivity implements RecyclerViewOnClickInterface{
    LogBudgetModels logBudgetModels;
    LogBd_RecyclerViewAdapter recyclerViewAdapter;
    ImageButton btnBack;
    Button btnApply;
    RecyclerView recyclerView;
    EditText overallBdEditText;
    BudgetService budgetService;
    String TAG = "LOGCAT";
    static String CAT_KEY = "Category";
    static String BD_KEY = "Budget";
    static String ID_KEY = "ID";
    private String userId;
    private String token;
    static String MODEL_KEY = "Model";
    private SharedPreferences mPref;
    private DataSubject<LogBudgetModels> subject;

    final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getData() !=null){
                Bundle b = result.getData().getExtras();
                int id = b.getInt(ID_KEY);
                String budget = b.getString(BD_KEY);
                logBudgetModels.updateBudgetModel(id, budget);
                recyclerViewAdapter.notifyItemChanged(id);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //SharedPreferences Permissions
        mPref = getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        token = mPref.getString(LoginActivity.TOKENKEY, LoginActivity.DEFAULT_USERID);

        //set up subject, model and service
        subject = new DataSubject<>();
        logBudgetModels = new LogBudgetModels();
        budgetService = new BudgetService(LogBudget.this);

        //Initalize Overall Budget EditText
        overallBdEditText = findViewById(R.id.overallEditText);
        overallBdEditText.addTextChangedListener(new TextWatcher() {
            boolean isEditing = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isEditing) return;
                isEditing = true;
                String input = editable.toString();
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    if (index + 3 < input.length()) {
                        input = input.substring(0, index + 3); // limit to 2dp
                    }
                }
                if (input.startsWith(".")) {
                    input = "0" + input; //Edge case whr if you start with a .
                }
                input = input.replace("$", "");
                overallBdEditText.setText(String.format("$%s",input));
                overallBdEditText.setSelection(overallBdEditText.getText().length());
                isEditing = false;
            }
        });
        btnApply = findViewById(R.id.btnApplyLogBudget);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Save new budget to db, check for invalid budget input, go to new screen
                Toast.makeText(LogBudget.this, "Applying Changes", Toast.LENGTH_SHORT).show();
                ArrayList<LogBudgetModel> models = logBudgetModels.getLogBudgetModels();
                ArrayList<PostCategoryAllocation> postCategoryAllocations = new ArrayList<>();
                for(LogBudgetModel model : models){
                    Log.d(TAG, "Category " + model.getCategory() + " Amount " + model.getBudget());
                    postCategoryAllocations.add(new PostCategoryAllocation(userId, model.getCategory(), Double.parseDouble(model.getBudget())));
                }
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        budgetService.updateBudgetData(postCategoryAllocations);
                    }
                });
            }
        });

        btnBack = findViewById(R.id.logBdBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogBudget.this, NavigationDrawerActivity.class);
                startActivity(intent);
            }
        });
        recyclerViewAdapter = new LogBd_RecyclerViewAdapter(LogBudget.this, logBudgetModels.getLogBudgetModels(), this);
        recyclerView = findViewById(R.id.logBdRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        ScaleCenterItemLayoutManager layoutManager = new ScaleCenterItemLayoutManager (this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        subject.registerObserver(recyclerViewAdapter);
        setUpLogBudgetModels();
    }

    private void setUpLogBudgetModels(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                logBudgetModels = budgetService.getLogBudgetCategories(userId, handler, subject);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        LogBudgetModel model = logBudgetModels.getLogBudgetModels().get(position);
        String category = model.getCategory();
        String budget = model.getBudget();
        int id = model.getId();
        Intent intent = new Intent(LogBudget.this, LogBudgetCategoryActivity.class);
        intent.putExtra(CAT_KEY, category);
        intent.putExtra(BD_KEY, "$" + budget);
        intent.putExtra(ID_KEY, id);
        launcher.launch(intent);
    }
}