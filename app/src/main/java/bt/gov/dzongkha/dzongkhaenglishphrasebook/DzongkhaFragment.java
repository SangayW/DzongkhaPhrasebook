package bt.gov.dzongkha.dzongkhaenglishphrasebook;


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

import java.util.ArrayList;
import java.util.List;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.GetCategoriesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.getTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class DzongkhaFragment extends Fragment {

    ArrayList<String> dzo_titleArrayList;
    RecyclerView recyclerView;
    GetCategoriesAdapter getCategoriesAdapter;
    List<getTitle> getTitleList;
   // ArrayList<Integer> getImageList;


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

        MyDatabase mdb = new MyDatabase(view.getContext());
        SQLiteDatabase db = mdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Categories",null);
        dzo_titleArrayList=new ArrayList<String>();
        //getImageList=new ArrayList<Integer>();

        //getImageList.add(R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t,R.drawable.u,R.drawable.v,R.drawable.w,R.drawable.x,R.drawable.y,R.drawable.z);


        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    dzo_titleArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow("dzo_title")));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        getTitleList= new ArrayList<>();
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),3));

        for (int i = 0; i < dzo_titleArrayList.size(); i++) {
            getTitleList.add(
                    new getTitle(
                            dzo_titleArrayList.get(i)
                    ));
        }
        getCategoriesAdapter = new GetCategoriesAdapter(view.getContext(),getTitleList);
        recyclerView.setAdapter(getCategoriesAdapter);
    }
}
