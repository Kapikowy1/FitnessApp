package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

public class MainActivity extends AppCompatActivity { //klasa MainActivityJava rozszerza klase Appcompatactivity czyli jest jej podklasą

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

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
        recyclerView=(RecyclerView) findViewById(R.id.rv);//inicjuje obiekt klasy RecyclerView o nazwie recyclerView
        // i przypisuje do niego element interfejsu o id rv
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
    }
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
    private void  txtSearch(String str) {  //metoda TxtSearch o atrybucie str bedacym tekstem
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()//
                // tworze nowy obiekt FirebaseRecyclerOptions o nazwie options o wlasciwosciach klasy MainModel
                // a następnie wywołuje Builder odpowiedzialny za konfiguracje opcji w FirebaseRecyclerOptions
                .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("name")
                        //odwoluje sie do lokalizacji danych w bazie danych
                        .startAt(str).endAt(str+"\uf8ff"), MainModel.class) // filtruje metody
                .build(); //metoda kończącza proces konfigurowania opcji i zwracająca obiekt MainModel do FirebaseRecyclerOptions
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }


}