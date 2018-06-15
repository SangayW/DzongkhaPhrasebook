package bt.gov.dzongkha.dzongkhaenglishphrasebook.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCatID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.DzongkhaPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoriesIcon;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoriesTitle;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DzongkhaFragment extends Fragment {

    ArrayList<String> dzo_titleArrayList;
    ArrayList<Integer> dzo_title_idArrayList;
    ArrayList<String>dzo_title_iconArrayList;

    RecyclerView recyclerView;
    DzongkhaPhrasesAdapter dzongkhaPhrasesAdapter;

    List<GetCategoriesTitle> getCategoriesTitleList;
    List<GetCatID>getIDList;
    List<GetCategoriesIcon>getCategoriesIconList;

    SQLiteDatabase sqLiteDatabase=null;

    public DzongkhaFragment() {
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
        listDzongkhaCategories(view);
    }
    public void listDzongkhaCategories(View view){
        MyDatabase helper= null;
        try {
            helper = new MyDatabase(view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqLiteDatabase = helper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from Categories",null);
        dzo_titleArrayList=new ArrayList<String>();
        dzo_title_idArrayList=new ArrayList<Integer>();
        dzo_title_iconArrayList=new ArrayList<>();

        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    dzo_titleArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("dzo_title")));
                    dzo_title_idArrayList.add(cursor.getInt(cursor.getColumnIndexOrThrow("cat_id")));
                    dzo_title_iconArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("icon")));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        sqLiteDatabase.close();

        getCategoriesTitleList = new ArrayList<>();
        getIDList= new ArrayList<>();
        getCategoriesIconList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),3));

        for (int i = 0; i < dzo_titleArrayList.size(); i++) {
            getCategoriesTitleList.add(
                    new GetCategoriesTitle(
                            dzo_titleArrayList.get(i)
                    ));
        }
        for (int i = 0; i < dzo_title_idArrayList.size(); i++) {
            getIDList.add(
                    new GetCatID(
                            dzo_title_idArrayList.get(i)
                    ));
        }
        for (int i = 0; i < dzo_title_iconArrayList.size(); i++) {
            getCategoriesIconList.add(
                    new GetCategoriesIcon(
                            dzo_title_iconArrayList.get(i)
                    ));
        }
        dzongkhaPhrasesAdapter = new DzongkhaPhrasesAdapter(view.getContext(), getCategoriesTitleList,getIDList,getCategoriesIconList);
        recyclerView.setAdapter(dzongkhaPhrasesAdapter);
    }
}
