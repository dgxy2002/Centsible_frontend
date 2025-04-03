package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.example.andyapp.models.LogBudgetModel;

import java.util.ArrayList;

public class LogBudget extends AppCompatActivity implements RecyclerViewOnClickInterface{
    ArrayList<LogBudgetModel> logBudgetModels;
    LogBd_RecyclerViewAdapter recyclerViewAdapter;
    ImageButton btnBack;
    Button btnApply;
    RecyclerView recyclerView;
    EditText overallBdEditText;
    String TAG = "LOGCAT";
    static String CAT_KEY = "Category";
    static String BD_KEY = "Budget";
    static String ID_KEY = "ID";
    static String MODEL_KEY = "Model";

    final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getData() !=null){
                Bundle b = result.getData().getExtras();
                int id = b.getInt(ID_KEY);
                String budget = b.getString(BD_KEY);
                logBudgetModels.get(id).setBudget(budget);
                recyclerViewAdapter.notifyItemChanged(id);
//                Log.d(TAG, logBudgetModels.get(id).getBudget());
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
        if (logBudgetModels == null) {
            logBudgetModels = new ArrayList<>();
            setUpLogBudgetModels();
        }
        recyclerViewAdapter = new LogBd_RecyclerViewAdapter(LogBudget.this, logBudgetModels, this);
        recyclerView = findViewById(R.id.logBdRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        ScaleCenterItemLayoutManager layoutManager = new ScaleCenterItemLayoutManager (this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private void setUpLogBudgetModels(){
        String[] categories = getResources().getStringArray(R.array.categories);

        for (int i = 0; i < categories.length; i++){
            String category = categories[i];
            logBudgetModels.add(new LogBudgetModel(i, category, R.drawable.othercategory, String.valueOf(i)));
        }
    }

    @Override
    public void onItemClick(int position) {
        String category = logBudgetModels.get(position).getCategory();
        String budget = logBudgetModels.get(position).getBudget();
        int id = logBudgetModels.get(position).getId();
        Intent intent = new Intent(LogBudget.this, LogBudgetCategoryActivity.class);
        intent.putExtra(CAT_KEY, category);
        intent.putExtra(BD_KEY, "$" + budget);
        intent.putExtra(ID_KEY, id);
        intent.putParcelableArrayListExtra(MODEL_KEY, logBudgetModels);
        launcher.launch(intent);
//        Log.d(TAG, category + budget);
    }
}