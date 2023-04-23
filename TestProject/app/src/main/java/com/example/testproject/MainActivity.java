package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity { //klasa MainActivityJava rozszerza klase Appcompatactivity czyli jest jej podklasą

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Button sortButton1,sortButton2,sortButton3,sortButton4;


    private boolean isSorted1=false;
    private boolean isSorted2=false;
    private boolean isSorted3=false;
    private boolean isSorted4=false;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //Metoda oncreate jest uruchamiana na starcie activit
        // obiektBundle przekazywany jest jako argument metody onCreate nazwany savedInstanceState
        // służy on do przechowywania stanu aplikacji podczas jej zamykania i obracania ekranem
        // gdy zamykamy aplikacje jej stan jest zapisywany w Bundle aby móc pozniej go przywrocic
        super.onCreate(savedInstanceState);//słowo kluczowe super odpowiada za odwołoanie do metody nadrzednej
        // czyli tutaj jest to metoda onCreate
        // wywołanie tej metody z argumentem savedInstanceState pozwala na inicjalizacje mechanizmu obsługi
        // motywów i stylów, które są używane przez aplikacje oraz innych niezbednych do jej dzialania elementow
        setContentView(R.layout.activity_main);//setContentView jest metodą, która ustawia widok dla danej
        // activity . R jest klasą generowaną automatycznie przez android studio przechowuje odwolania do
        // zasobów natomiast layout odwoluje sie do folderu layout a activity main do pliku xml w tym folderze

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        recyclerView=(RecyclerView) findViewById(R.id.rv);//inicjuje obiekt klasy RecyclerView o nazwie recyclerView i przypisuje do niego element interfejsu o id rv
        sortButton1 = findViewById(R.id.SortB);
        sortButton2=findViewById(R.id.SortC);
        sortButton3=findViewById(R.id.SortD);
        sortButton4=findViewById(R.id.SortG);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.diet_plan:
                        Intent intent = new Intent(MainActivity.this, Dietplan_activity.class);
                        MainActivity.this.startActivity(intent);

                }
                return false;
            }
        });






        recyclerView.setLayoutManager(new LinearLayoutManager(this));//LayoutManager jest odpowiedzialny za
        // pozycjonowanie elementów w RecyclerView oraz zarządzanie ich rozmiarem i przewijaniem
        // LinearLayoutManager pozycjonuje elementy  linearnie
        // element this jest konstruktorem oraz odwoluje sie do bieżącego activity
        FirebaseRecyclerOptions<MainModel> options=             //Ta linia kodu tworzy obiekt FirebaseRecyclerOptions<MainModel(Odwolanie do klasy Mainmodel)> o nazwie options, obiekt ten używany jest do określania źródła i typu danych
                new FirebaseRecyclerOptions.Builder<MainModel>()  //Ta linia tworzy nowy obiekt typu Builder,który pozwala na konfiguracje opcji dla FireBaseRecyclerOptions
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"),MainModel.class)  //metoda ta ustawia zapytanie do bazy danych Firebase,które pobiera dane z dziecka teachers z bazy danych,MainModel.class jest klasą ktorej mapowane dane beda pobrane z bazy danych
                        .build();//metoda kończąca proces konfigurowania opcji i zwraca obiekt do FirebaseRecyclerOptions
        //W skrócie, ta linia kodu tworzy obiekt FirebaseRecyclerOptions, który pozwala na pobranie danych z dziecka "teachers" w bazie danych Firebase i mapowanie ich na obiekty typu MainModel, które będą wykorzystywane przez adapter RecyclerView.


        mainAdapter=new MainAdapter(options);// tworzy nowy obiekt MainAdapter przekazujac do niego obiekt options z wyżej i przypisuje go do zmiennej
        // ustawia utworzony obiekt jako adapter dla RecyclerView
        recyclerView.setAdapter(mainAdapter); //ustawia adapter,który jest odpowiedzialny za wyświetlenie
        // robi to za pomocą utworzonego wczesniej obiektu mainAdapter
        // adapter odpowiada za laczenie danych z interfejsem uzytkownika i przekazywanie ich do
        // RecyclerView, który wyświetla je na ekranie



        //SORTOWANIE
        sortButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSorted1) {
                    setButtonColors(false, sortButton1);
                    isSorted1 = false;
                } else {
                    setButtonColors(true, sortButton1);
                    isSorted2=false;
                    isSorted3=false;
                    isSorted4=false;
                    txtSortSniad();
                    isSorted1 = true;
                }
            }
        });
        sortButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSorted2) {
                    setButtonColors(false, sortButton2);
                    unsortData();
                    isSorted2 = false;
                } else {
                    setButtonColors(true, sortButton2);
                    isSorted1=false;
                    isSorted3=false;
                    isSorted4=false;
                    txtSortObiad();
                    isSorted2 = true;
                }
            }
        });
        sortButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSorted3) {
                    setButtonColors(false, sortButton3);
                    unsortData();
                    isSorted3 = false;
                } else {
                    setButtonColors(true, sortButton3);
                    isSorted1=false;
                    isSorted2=false;
                    isSorted4=false;
                    txtSortKolacja();
                    isSorted3 = true;

                }
            }
        });
        sortButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSorted4) {
                    setButtonColors(false, sortButton4);
                    unsortData();
                    isSorted4 = false;
                } else {
                    setButtonColors(true, sortButton4);
                    isSorted1=false;
                    isSorted2=false;
                    isSorted3=false;
                    txtSortDeser();
                    isSorted4 = true;

                }
            }
        });
    }

    private void setButtonColors(boolean isSorted, Button button) {
        sortButton1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        sortButton2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        sortButton3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        sortButton4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        if (!isSorted) {
            unsortData();
        } else {
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.teal_200));
        }
    }


    private void unsortData() {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void  txtSearch(String str) {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("name").startAt(str).endAt(str+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }


    private void txtSortSniad() {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("course").startAt("Śniadanie").endAt("Śniadanie"+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void txtSortObiad() {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("course").startAt("Obiad").endAt("Obiad"+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void txtSortKolacja() {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("course").startAt("Kolacja").endAt("Kolacja"+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void txtSortDeser() {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("course").startAt("Deser").endAt("Deser"+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    //SORTOWANIE


    //WYSZUKIWANIE WYNIKOW
    @Override //notacja @Override jest używana po to aby oznaczyć metodę, która jest nadpisaniem klasy nadrzędnej
    // ma na celu kontynuacje implementacji z klasy nadrzędnej
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // metoda jest wywoływana gdy jest tworzone menu opcji
        // dla danego activity
        getMenuInflater().inflate(R.menu.search,menu);//ta linia odpowiada za umieszczenie elementów
        // z pliku xml o nazwie search xml do obiektu menu
        MenuItem item =menu.findItem(R.id.search);// pobiera element xml o nazwie search
        SearchView searchView=(SearchView) item.getActionView();//pobiera referencje do elementu
        // typu SearchView ktory jest widokiem dla elementu menu o id search
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//przypisuje
            // listener do searchView, który pozwala na reakcje miedzy tekstem a użytkownikiem
            @Override
            public boolean onQueryTextSubmit(String query) {//onQueryTextSubmit jest metoda,która jest
                // wywolywana gdy uzytkownik wciska przycisk search przyjmuje arg string ktory jest
                // tekstem wprowadzonym przez uzytkownika
                txtSearch(query); //metoda wywolywana z elementem query czyli wprowadzonym przez uzytkownika tekstem
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {// metoda onQueryTextChange jest
                // metoda ktora jest wywolywana gdy uzytkownik zmienia text w polu
                txtSearch(newText);
                return false; // odpowiada za to ,żebypo wykonanynm zdarzeniu przekazać informacje do kolejnych listenerow
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //WYSZUKIWANIE WYNIKOW






}