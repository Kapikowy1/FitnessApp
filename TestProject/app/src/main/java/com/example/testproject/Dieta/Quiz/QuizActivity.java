package com.example.testproject.Dieta.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testproject.Dieta.RecipeSearch.MainActivity;
import com.example.testproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizActivity extends AppCompatActivity {

    private TextView questiontext;
    private Button AButton;
    private Button BButton;
    private Button CButton;
    private Button DButton;
    private QuestionGroup quiz;
    private int questioncount=0,correctanswercount=0,wronganswercount=0,maxQuestions;
    private boolean isclicked=false;
    private String finalAnswer;
    private ProgressBar progressBarQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questiontext = findViewById(R.id.questionTextView);
        AButton = findViewById(R.id.aButton);
        BButton = findViewById(R.id.bButton);
        CButton = findViewById(R.id.cButton);
        DButton = findViewById(R.id.dButton);
        Button submitQuestion = findViewById(R.id.submitButtonQuiz);
        progressBarQuiz = findViewById(R.id.progressBarQuiz);

        resetQuizAnswers();
        quiz = new QuestionGroup();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionsRef = database.getReference("questions");

        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    fetchQuizData(questionSnapshot);
                }

                Question question = quiz.getNextQuestion();
                maxQuestions = (int) dataSnapshot.getChildrenCount();
                setTextOnQuizElements(question);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("1", "loadQuestions:onCancelled", databaseError.toException());
            }
        });
        View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = ((Button) v).getText().toString();
                finalAnswer = selectedAnswer;
                setButtonBlackColor();
                v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.purple_200));
                isclicked = !isclicked;
            }
        };
        AButton.setOnClickListener(answerButtonClickListener);
        BButton.setOnClickListener(answerButtonClickListener);
        CButton.setOnClickListener(answerButtonClickListener);
        DButton.setOnClickListener(answerButtonClickListener);

        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questioncount==maxQuestions-1){
                    checkAnswer();
                    buildResultDialog();
                }else {
                    if (!isclicked) {
                        Toast.makeText(getApplicationContext(), "Wybierz odpowiedź", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    checkAnswer();

                    Question question = quiz.getNextQuestion();
                    if (question == null) {
                        Toast.makeText(getApplicationContext(), "Koniec quizu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setProgressBar();
                    setTextOnQuizElements(question);
                    resetUserChoice();
                    setButtonBlackColor();
                }
            }
        });
    }
    private void setTextOnQuizElements(Question question){
        questiontext.setText(question.getQuestionText());
        AButton.setText(question.getOptionA());
        BButton.setText(question.getOptionB());
        CButton.setText(question.getOptionC());
        DButton.setText(question.getOptionD());
    }
    private void setButtonBlackColor(){
        AButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        BButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        CButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        DButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
    }

    private void fetchQuizData(DataSnapshot questionSnapshot){
        String questionText = questionSnapshot.child("qtext").getValue(String.class);
        String answerText = questionSnapshot.child("answer").getValue(String.class);
        String optionAText = questionSnapshot.child("optionA").getValue(String.class);
        String optionBText = questionSnapshot.child("optionB").getValue(String.class);
        String optionCText = questionSnapshot.child("optionC").getValue(String.class);
        String optionDText = questionSnapshot.child("optionD").getValue(String.class);

        Question question = new Question(questionText, answerText,optionAText,optionBText,optionCText,optionDText);

        quiz.addQuestion(question);
    }
    private void startMainIntent(){
        Intent intentMain = new Intent(QuizActivity.this, MainActivity.class);
        startActivity(intentMain);
    }
    private void setProgressBar(){
        progressBarQuiz.setProgress(correctanswercount+wronganswercount);
        progressBarQuiz.setMax(maxQuestions);
    }
    private void resetUserChoice(){
        isclicked = false;
        finalAnswer = "";
    }

    private void checkAnswer() {
        if (finalAnswer.equals(quiz.getCurrentQuestion().getCorrectAnswer())) {
            Toast.makeText(getApplicationContext(), "Poprawna odpowiedź", Toast.LENGTH_SHORT).show();
            questioncount++;
            correctanswercount++;
        } else {
            Toast.makeText(getApplicationContext(), "Niepoprawna odpowiedź", Toast.LENGTH_SHORT).show();
            questioncount++;
            wronganswercount++;
        }
    }
    private void buildResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wyniki");
        LinearLayout layout = createResultLayout();
        createPositiveAnswerTextView(layout);
        createNegativeAnswerTextView(layout);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMainIntent();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private LinearLayout createResultLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private void createPositiveAnswerTextView(LinearLayout layout) {
        TextView correctAnswerTextView = new TextView(this);
        correctAnswerTextView.setText("Poprawne odpowiedzi: " + correctanswercount);
        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_text_margin);
        correctAnswerTextView.setPadding(margin, margin, margin, margin);
        layout.addView(correctAnswerTextView);
    }

    private void createNegativeAnswerTextView(LinearLayout layout) {
        TextView wrongAnswerTextView = new TextView(this);
        wrongAnswerTextView.setText("Niepoprawne odpowiedzi: " + wronganswercount);
        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_text_margin);
        wrongAnswerTextView.setPadding(margin, margin, margin, margin);
        layout.addView(wrongAnswerTextView);
    }
    private void resetQuizAnswers(){
        questioncount=0;
        correctanswercount=0;
        wronganswercount=0;

    }

}


