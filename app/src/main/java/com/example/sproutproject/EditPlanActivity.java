package com.example.sproutproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sproutproject.database_entity.PlanDisplay;
import com.example.sproutproject.databse.PlanDatabase;
import com.example.sproutproject.entity.Plan;
import com.example.sproutproject.utils.ThreadUtils;
import com.example.sproutproject.viewmodel.PlanViewModel;
import com.example.sproutproject.viewmodel.PlantViewModel;

import java.util.List;

public class EditPlanActivity extends AppCompatActivity {
    private ImageView iv_edit_back_button;
    private EditText et_change_name;
    private Button edit_delete, edit_save;
    PlanViewModel planViewModel;
    private Toast toast = null;
    int planidPass;
    PlanDatabase db = null;
    Plan plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);


        db = PlanDatabase.getInstance(this);

        iv_edit_back_button= findViewById(R.id.iv_edit_back_button);
        et_change_name = findViewById(R.id.et_change_name);
        edit_delete = findViewById(R.id.edit_delete);
        edit_save = findViewById(R.id.edit_save);
        plan = new Plan();

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        planViewModel.initalizeVars(getApplication());

        Intent intent = getIntent();
        final PlanDisplay planDisplay = (PlanDisplay) intent.getSerializableExtra("wholePlanDisplay");
        final String planNameChange = planDisplay.getPlanName();
        planidPass = planDisplay.getPlanId();
        et_change_name.setText(planNameChange);

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                plan = db.planDao().findByID(planidPass);
            }
        });


        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlanName = et_change_name.getText().toString().trim();
                plan.setPlanName(newPlanName);
                planDisplay.setPlanName(newPlanName);
                planViewModel.update(plan);
                showToast("Plan name change success");
                PlanDetailActivity.instance.finish();
                Intent intent = new Intent(EditPlanActivity.this, PlanDetailActivity.class);
                intent.putExtra("planDisplay", planDisplay);
                startActivity(intent);
                finish();
            }
        });

        edit_delete.setOnClickListener(new OnClickListenerImpl());

        iv_edit_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class OnClickListenerImpl implements View.OnClickListener {

        public void onClick(View v) {
            Dialog dialog = new AlertDialog.Builder(EditPlanActivity.this)
                    .setTitle("Delete plan")  // create title
                    .setMessage("Are you sure to delete the plan?")    //create content
                    .setIcon(R.drawable.ic_garbage) //set logo
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("Delete plan success");
                            planViewModel.delete(planidPass);
                            Intent intent = new Intent(EditPlanActivity.this, MainActivity.class);
                            intent.putExtra("pid",1);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();  //create dialog
            dialog.show();  //show dialog

        }

    }
}
