package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuizFragment extends Fragment {

    private TextView questiontext;
    private Button AButton,BButton,CButton,DButton,submitQuestion;
    private QuestionGroup quiz;
    private int questioncount=0,correctanswercount=0,wronganswercount=0,maxQuestions;
    private boolean isclicked=false;
    String finalAnswer;
    private ProgressBar progressBarQuiz;
    public QuizFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        questiontext = view.findViewById(R.id.questionTextView);
        AButton = view.findViewById(R.id.aButton);
        BButton = view.findViewById(R.id.bButton);
        CButton = view.findViewById(R.id.cButton);
        DButton = view.findViewById(R.id.dButton);
        submitQuestion=view.findViewById(R.id.submitButtonQuiz);
        progressBarQuiz = view.findViewById(R.id.progressBarQuiz);

        quiz = new QuestionGroup();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionsRef = database.getReference("questions");

        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String questionText = questionSnapshot.child("qtext").getValue(String.class);
                    String answerText = questionSnapshot.child("answer").getValue(String.class);
                    String optionAText = questionSnapshot.child("optionA").getValue(String.class);
                    String optionBText = questionSnapshot.child("optionB").getValue(String.class);
                    String optionCText = questionSnapshot.child("optionC").getValue(String.class);
                    String optionDText = questionSnapshot.child("optionD").getValue(String.class);

                    Question question = new Question(questionText, answerText,optionAText,optionBText,optionCText,optionDText);

                    quiz.addQuestion(question);
                }
                // wylosuj pierwsze pytanie
                Question question = quiz.getNextQuestion();
                maxQuestions = (int) dataSnapshot.getChildrenCount(); // pobierz ilość pytań
                questiontext.setText(question.getQuestionText());
                AButton.setText(question.getOptionA());
                BButton.setText(question.getOptionB());
                CButton.setText(question.getOptionC());
                DButton.setText(question.getOptionD());
                // można teraz wykorzystać to pytanie w interfejsie użytkownika (UI)
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("1", "loadQuestions:onCancelled", databaseError.toException());
            }
        });
        View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = ((Button) v).getText().toString();
                finalAnswer = selectedAnswer;
                AButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                BButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                CButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                DButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                v.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.purple_200));
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
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    getContext().startActivity(intent);
                }else {
                    if (!isclicked) {
                        Toast.makeText(getContext(), "Wybierz odpowiedź", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // sprawdź czy odpowiedź jest poprawna
                    if (finalAnswer.equals(quiz.getCurrentQuestion().getCorrectAnswer())) {
                        Toast.makeText(getContext(), "Poprawna odpowiedź", Toast.LENGTH_SHORT).show();
                        questioncount++;
                        correctanswercount++;
                    } else {
                        Toast.makeText(getContext(), "Niepoprawna odpowiedź", Toast.LENGTH_SHORT).show();
                        questioncount++;
                        wronganswercount++;
                    }
                    // przejdź do kolejnego pytania
                    Question question = quiz.getNextQuestion();
                    if (question == null) {
                        //wszystkie pytania zostały zadane
                        Toast.makeText(getContext(), "Koniec quizu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressBarQuiz.setProgress(correctanswercount+wronganswercount);
                    progressBarQuiz.setMax(maxQuestions);
                    //aktualizuj widok z kolejnym pytaniem
                    questiontext.setText(question.getQuestionText());
                    AButton.setText(question.getOptionA());
                    BButton.setText(question.getOptionB());
                    CButton.setText(question.getOptionC());
                    DButton.setText(question.getOptionD());
                    //zresetuj wybór użytkownika
                    isclicked = false;
                    finalAnswer = "";
                    AButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                    BButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                    CButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                    DButton.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                }
                //sprawdź czy odpowiedź została wybrana
            }
        });
        return view;
    }
}