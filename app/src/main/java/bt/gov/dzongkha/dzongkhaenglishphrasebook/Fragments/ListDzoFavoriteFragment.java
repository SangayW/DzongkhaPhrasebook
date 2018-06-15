package bt.gov.dzongkha.dzongkhaenglishphrasebook.Fragments;


import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.GetDzoPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoryDetailID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetDzongkhaPhrases;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.TextSize;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sangay on 3/23/2018.
 */

public class ListDzoFavoriteFragment extends Fragment {

    ArrayList<String> dzo_phrases_list;
    ArrayList<Integer> category_detail_id_list;

    RecyclerView recyclerView;
    GetDzoPhrasesAdapter getDzoPhrasesAdapter;

    List<GetDzongkhaPhrases> getDzongkhaPhrasesList;
    List<GetCategoryDetailID> getCategoryDetailIDList;

    private SharedPreferences prefs;
    private static final String FONT_SIZE_KEY="font size";
    TextSize ts;

    public ListDzoFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dzongkha, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

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
        listDzoFavorites(view);
        //ends
    }


    public void listDzoFavorites(View view){
        MyDatabase helper = null;
        try {
            helper = new MyDatabase(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from categoryDetails where favorite=1",null);
        dzo_phrases_list=new ArrayList<String>();
        category_detail_id_list=new ArrayList<Integer>();

        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    dzo_phrases_list.add(cursor.getString(cursor.getColumnIndexOrThrow("dzo_phrase")));
                    category_detail_id_list.add(cursor.getInt(cursor.getColumnIndexOrThrow("cat_details_id")));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        getDzongkhaPhrasesList=new ArrayList<>();
        getCategoryDetailIDList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        for (int i = 0; i < dzo_phrases_list.size(); i++) {
            getDzongkhaPhrasesList.add(
                    new GetDzongkhaPhrases(
                            dzo_phrases_list.get(i)
                    ));
        }
        for (int i = 0; i < category_detail_id_list.size(); i++) {
            getCategoryDetailIDList.add(
                    new GetCategoryDetailID(
                            category_detail_id_list.get(i)
                    ));
        }
        getDzoPhrasesAdapter = new GetDzoPhrasesAdapter(view.getContext(),getDzongkhaPhrasesList,getCategoryDetailIDList,ts.getTextSize(),ts.getDzo_size());
        recyclerView.setAdapter(getDzoPhrasesAdapter);
    }
}
