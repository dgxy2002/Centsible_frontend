//package com.example.andyapp.fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.Fragment;
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.example.andyapp.LogBudget;
//import com.example.andyapp.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link LogBudgetCategoryFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class LogBudgetCategoryFragment extends Fragment {
//
//    ImageButton btnBack;
//    ImageButton btnIncrease;
//    ImageButton btnDecrease;
//    EditText budgetEditText;
//    TextView categoryTextView;
//    Intent intent;
//    String categoryText;
//    String budgetAmount;
//    int id;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_log_budget_category, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        intent = requireActivity().getIntent();
//        if(intent!=null){
//            categoryText = intent.getStringExtra(LogBudget.CAT_KEY);
//            budgetAmount = intent.getStringExtra(LogBudget.BD_KEY);
//            id = intent.getIntExtra(LogBudget.ID_KEY, 0);
////            logBudgetModels = intent.getParcelableArrayListExtra(LogBudget.MODEL_KEY);
//        }else{
//            categoryText = "Category";
//            budgetAmount = "0.00";
//            id = 0;
//        }
//        btnBack = view.findViewById(R.id.logBdCategoryBtnBack);
//        btnIncrease = view.findViewById(R.id.logBdBtnIncrease);
//        btnDecrease = view.findViewById(R.id.LogBdBtnDecrease);
//        budgetEditText = view.findViewById(R.id.editTextNumberDecimal);
//        categoryTextView = view.findViewById(R.id.logBudgetCategoryNameTV);
//        categoryTextView.setText(String.format("For: %s", categoryText));
//        budgetEditText.setText(budgetAmount);
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                budgetAmount = budgetEditText.getText().toString().substring(1);
//                if (budgetAmount.isEmpty()){
//                    budgetAmount = "0.00";
//                }
//                intent.putExtra(LogBudget.BD_KEY, budgetAmount);
//                intent.putExtra(LogBudget.ID_KEY, id);
//                setResult(LogBudget.RESULT_OK,intent);
//                finish();
//            }
//        });
//
//        budgetEditText.addTextChangedListener(new TextWatcher() {
//            boolean isEditing = false;
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (isEditing) return;
//                isEditing = true;
//                String input = s.toString();
//                if (input.contains(".")) {
//                    int index = input.indexOf(".");
//                    if (index + 3 < input.length()) {
//                        input = input.substring(0, index + 3); // limit to 2dp
//                    }
//                }
//                if (input.startsWith(".")) {
//                    input = "0" + input; //Edge case whr if you start with a .
//                }
//                input = input.replace("$", "");
//                budgetEditText.setText(String.format("$%s",input));
//                budgetEditText.setSelection(budgetEditText.getText().length());
//                isEditing = false;
//            }
//
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
//        });
//
//
//    }
//
//    }
//}