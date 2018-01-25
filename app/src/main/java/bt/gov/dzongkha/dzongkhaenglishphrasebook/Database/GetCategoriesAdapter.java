package bt.gov.dzongkha.dzongkhaenglishphrasebook.Database;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

/**
 * Created by sangay on 1/24/2018.
 */

public class GetCategoriesAdapter extends RecyclerView.Adapter<GetCategoriesAdapter.CategoriesViewHolder>   {
    private Context mCtx;
    private List<getTitle> getTitleList;

    getTitle getTitles;

    public GetCategoriesAdapter(Context mCtx, List<getTitle> getTitleList) {
        this.mCtx = mCtx;
        this.getTitleList = getTitleList;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mCtx);
        View view=layoutInflater.inflate(R.layout.list_titles,null);
        GetCategoriesAdapter.CategoriesViewHolder categoriesViewHolder=new GetCategoriesAdapter.CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        int[] getImageList = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t,R.drawable.u,R.drawable.v,R.drawable.w,R.drawable.x,R.drawable.y,R.drawable.z};

        getTitles=getTitleList.get(position);

        holder.category.setText(getTitles.getTitle());
        holder.imageView.setImageResource(getImageList[position]);

    }

    @Override
    public int getItemCount() {
        return getTitleList.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView category;
        ImageView imageView;

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            category=itemView.findViewById(R.id.title);
            imageView=itemView.findViewById(R.id.item_image);
        }


    }
}
