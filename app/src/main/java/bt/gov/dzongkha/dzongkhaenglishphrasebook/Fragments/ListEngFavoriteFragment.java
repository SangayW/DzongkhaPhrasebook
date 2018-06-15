package bt.gov.dzongkha.dzongkhaenglishphrasebook.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.GetEngPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoryDetailID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetEnglishPhrases;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.TextSize;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

import static android.content.Context.MODE_PRIVATE;

public class ListEngFavoriteFragment extends Fragment {

    ArrayList<String> eng_phrases_list;
    ArrayList<Integer> category_detail_id_list;

    public RecyclerView recyclerView;
    GetEngPhrasesAdapter getEngPhrasesAdapter;

    ArrayList<GetEnglishPhrases> getEnglishPhrasesList;
    List<GetCategoryDetailID> getCategoryDetailIDList;

    private SharedPreferences prefs;
    private static final String FONT_SIZE_KEY="font size";
    TextSize ts;

    public ListEngFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_eng_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different title

         /*begins setting the size of the text based on the value stored in the shared preference*/
        prefs=getActivity().getPreferences(MODE_PRIVATE);
        if(prefs.getString(FONT_SIZE_KEY,"").isEmpty()){
            ts=new TextSize(15,35);
        }
        else
        {
            if(prefs.getString(FONT_SIZE_KEY,"").equalsIgnoreCase("0")){
                ts=new TextSize(12,25);
            }
            else if(prefs.getString(FONT_SIZE_KEY,"").equalsIgnoreCase("1")){
                ts=new TextSize(15,35);
            }
            else if(prefs.getString(FONT_SIZE_KEY,"").equalsIgnoreCase("2")){
                ts=new TextSize(18,45);
            }
            else
                ts=new TextSize(21,55);
        }
        /*ends*/
        /*--calling list favorites function--*/
        listEngFavorites(view);
       // ends
    }
    public void listEngFavorites(View view){
        MyDatabase mdb = null;
        try {
            mdb = new MyDatabase(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = mdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from categoryDetails where favorite=1",null);

        eng_phrases_list=new ArrayList<String>();
        category_detail_id_list=new ArrayList<Integer>();
        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    eng_phrases_list.add(cursor.getString(cursor.getColumnIndexOrThrow("en_phrase")));
                    category_detail_id_list.add(cursor.getInt(cursor.getColumnIndexOrThrow("cat_details_id")));
                } while (cursor.moveToNext());
            }
        }

        else
        {
            Toast.makeText(getActivity(),"You don't have favorites",Toast.LENGTH_LONG).show();
        }
        cursor.close();
        db.close();

        getEnglishPhrasesList=new ArrayList<>();
        getCategoryDetailIDList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        for (int i = 0; i < eng_phrases_list.size(); i++) {
            getEnglishPhrasesList.add(
                    new GetEnglishPhrases(
                            eng_phrases_list.get(i)
                    ));
        }

        for (int i = 0; i < category_detail_id_list.size(); i++) {
            getCategoryDetailIDList.add(
                    new GetCategoryDetailID(
                            category_detail_id_list.get(i)
                    ));
        }
        getEngPhrasesAdapter = new GetEngPhrasesAdapter(view.getContext(),getEnglishPhrasesList,getCategoryDetailIDList,ts.getTextSize(),ts.getDzo_size());
        recyclerView.setAdapter(getEngPhrasesAdapter);
    }
}



