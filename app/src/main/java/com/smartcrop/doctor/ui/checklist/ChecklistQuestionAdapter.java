package com.smartcrop.doctor.ui.checklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.SymptomQuestion;
import com.smartcrop.doctor.reasoning.ChecklistObservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChecklistQuestionAdapter extends RecyclerView.Adapter<ChecklistQuestionAdapter.QuestionViewHolder> {

    private final List<SymptomQuestion> questions;
    private final Map<String, String> answers = new HashMap<>(); // questionId -> "YES", "NO", "NOT_SURE"

    public ChecklistQuestionAdapter(List<SymptomQuestion> questions, List<ChecklistObservation> existing) {
        this.questions = questions;
        if (existing != null) {
            for (ChecklistObservation obs : existing) {
                answers.put(obs.getQuestionId(), obs.getAnswer());
            }
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_symptom_question, parent, false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        SymptomQuestion q = questions.get(position);
        holder.tvNumber.setText("Question " + (position + 1) + " of " + questions.size());
        holder.tvText.setText(q.getQuestionText());
        holder.tvHelp.setText(q.getHelpExplanation());

        String currentAnswer = answers.get(q.getQuestionId());
        if ("YES".equalsIgnoreCase(currentAnswer)) {
            holder.rbYes.setChecked(true);
        } else if ("NO".equalsIgnoreCase(currentAnswer)) {
            holder.rbNo.setChecked(true);
        } else {
            holder.rbNotSure.setChecked(true);
        }

        holder.rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbYes) {
                answers.put(q.getQuestionId(), "YES");
            } else if (checkedId == R.id.rbNo) {
                answers.put(q.getQuestionId(), "NO");
            } else {
                answers.put(q.getQuestionId(), "NOT_SURE");
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    public List<ChecklistObservation> getObservations() {
        List<ChecklistObservation> result = new ArrayList<>();
        for (SymptomQuestion q : questions) {
            String ans = answers.get(q.getQuestionId());
            if (ans == null) ans = "NOT_SURE";
            result.add(new ChecklistObservation(q.getQuestionId(), q.getQuestionText(), ans, q.getTargetDiseaseId()));
        }
        return result;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber, tvText, tvHelp;
        RadioGroup rgOptions;
        RadioButton rbYes, rbNo, rbNotSure;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvText = itemView.findViewById(R.id.tvQuestionText);
            tvHelp = itemView.findViewById(R.id.tvHelpExplanation);
            rgOptions = itemView.findViewById(R.id.rgAnswerOptions);
            rbYes = itemView.findViewById(R.id.rbYes);
            rbNo = itemView.findViewById(R.id.rbNo);
            rbNotSure = itemView.findViewById(R.id.rbNotSure);
        }
    }
}
