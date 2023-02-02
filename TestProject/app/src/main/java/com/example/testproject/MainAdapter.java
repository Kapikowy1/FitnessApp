package com.example.testproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }
    // ta linijka definiuje konstruktor klasyMainAdapter jako rozszerzenie klasy FbRecyclerAdapter
    // przyjmuje on jeden argument, obiekt FirebaseRecyclerOptions zawierajacy opcje konfiguracji
    // wywolujemy rowniez metode super odpowiedzialna do wywolania nadpisanej metody w klasie nadrzędnej
    // umozliwia to korzystanie z funkcjonalnosci klasy nadrzednej w klasie podrzednej

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName()); // ustawian text zmiennych na wartosc funkcji getName z klasy Mainmodel
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext()) //uzywamy biblioteki glide do załadowania obrazu do imageview

                .load(model.getTurl())// laduje obraz z URL zwracany przez metodegetTurl
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)//
                // ustawia obraz placeholderu, ktory odpowiedzialny jest wyswietlany gdy obraz wlasciwy
                // sie laduje
                .circleCrop() //przeksztalca obraz na okrągly
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)//
                // jezeli wystapi blad we wczytywaniu obrazu ustawia taki obraz
                .into(holder.img);//laduje obraz do imageview o id img

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // viewgroup parent definiuje ulozenie nowego widoku na parent
        // implementuje metode z RecyclerView.Adapter class o nazwie onCreateViewHolder
        // ,ktora jest wywolywana gdy potrzebujemy nowy viewholder zeby pokazac przedmiot w recyclerview

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);//
        // tworrze nowy obiekt typu widok o nazwie view i definiuje go za pomoca LayoutInflater class
        // odpowiedzialnej za konwertowanie plikow typu XML na objekt typu View ktory mozemy zobaczyc na ekranie
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{ // tworze klase ,ktora jest podklasa do klasy RecyclerView
        // ViewHolder odpowiedzialnej za przetrzymywanie pojedynczych elementów View mojego RecyclerView
        CircleImageView img;
        TextView name,course,email;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img= (CircleImageView)itemView.findViewById(R.id.img1);
            name= (TextView) itemView.findViewById(R.id.nametxt);
            course=(TextView) itemView.findViewById(R.id.coursetext);
            email=(TextView) itemView.findViewById(R.id.emailtext);
        }
    }
}
