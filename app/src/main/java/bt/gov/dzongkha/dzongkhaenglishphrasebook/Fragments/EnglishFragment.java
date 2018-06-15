package bt.gov.dzongkha.dzongkhaenglishphrasebook.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.EnglishPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCatID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoriesIcon;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoriesTitle;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnglishFragment extends Fragment {

    /*start array list to store the title,id and icon of the categories*/

    ArrayList<String> en_titleArrayList;
    ArrayList<Integer> en_title_idArrayList;
    ArrayList<String> en_title_iconArrayList;
    /*end*/

    RecyclerView recyclerView;
    EnglishPhrasesAdapter englishPhrasesAdapter;

    List<GetCategoriesTitle> getCategoriesTitleList;
    List<GetCatID>getIDList;

    // List to get the icon of each category
    List<GetCategoriesIcon>getCategoriesIconList;

    public EnglishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_english, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        listEnglishCategories(view);

    }
    public void listEnglishCategories(View view){
        MyDatabase mdb = null;
        try {
            mdb = new MyDatabase(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = mdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Categories",null);

        en_titleArrayList=new ArrayList<String>();
        en_title_idArrayList=new ArrayList<Integer>();
        en_title_iconArrayList=new ArrayList<String>();

        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    en_titleArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("en_title")));
                    en_title_idArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("cat_id")));
                    en_title_iconArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("icon")));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        getCategoriesTitleList = new ArrayList<>();
        getIDList= new ArrayList<>();
        getCategoriesIconList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),3));

        /*method to add the category title to the list*/
        for (int i = 0; i < en_titleArrayList.size(); i++) {
            getCategoriesTitleList.add(
                    new GetCategoriesTitle(
                            en_titleArrayList.get(i)
                    ));
        }
         /*method to add the category id to the list*/
        for (int i = 0; i < en_title_idArrayList.size(); i++) {
            getIDList.add(
                    new GetCatID(
                            en_title_idArrayList.get(i)
                    ));
        }

        /*method to add the category icon to the list*/
        for (int i = 0; i < en_title_iconArrayList.size(); i++) {
            getCategoriesIconList.add(
                    new GetCategoriesIcon(
                            en_title_iconArrayList.get(i)
                    ));
        }
        englishPhrasesAdapter = new  EnglishPhrasesAdapter(view.getContext(), getCategoriesTitleList,getIDList,getCategoriesIconList);
        recyclerView.setAdapter(englishPhrasesAdapter);
    }
}
