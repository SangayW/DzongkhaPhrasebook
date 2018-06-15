package bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.CategoryDetails.DzoPhrasesDetails;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCatID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoriesIcon;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoriesTitle;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

/**
 * Created by sangay on a/24/2018.
 */

public class DzongkhaPhrasesAdapter extends RecyclerView.Adapter<DzongkhaPhrasesAdapter.CategoriesViewHolder>   {
    private Context mCtx;
    private List<GetCategoriesTitle> getCategoriesTitleList;
    private List<GetCatID> getIDList;
    private List<GetCategoriesIcon>getCategoriesIconList;


    GetCategoriesTitle getCategoriesTitles;
    GetCatID getCatID;
    GetCategoriesIcon getCategoriesIcon;

    public DzongkhaPhrasesAdapter(Context mCtx, List<GetCategoriesTitle> getCategoriesTitleList, List<GetCatID> getIDList, List<GetCategoriesIcon>getCategoriesIconList) {
        this.mCtx = mCtx;
        this.getCategoriesTitleList = getCategoriesTitleList;
        this.getIDList=getIDList;
        this.getCategoriesIconList=getCategoriesIconList;

    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mCtx);
        View view=layoutInflater.inflate(R.layout.list_titles,null);
        DzongkhaPhrasesAdapter.CategoriesViewHolder categoriesViewHolder=new DzongkhaPhrasesAdapter.CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoriesViewHolder holder, int position) {

        getCategoriesTitles = getCategoriesTitleList.get(position);
        getCatID=getIDList.get(position);

        getCategoriesIcon=getCategoriesIconList.get(position);

        /*code to get uri of the image from the drawable and display on the image view based on the image name being stored in database*/
        Uri uri= Uri.parse("android.resource://"+ mCtx.getPackageName()+"/drawable/"+getCategoriesIcon.getIcon());
        InputStream inputStream = null;
        try {
            inputStream = mCtx.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap image = BitmapFactory.decodeStream(inputStream);
        holder.imageView.setImageBitmap(image);

        /*end of the set image functionalities*/


            //text font category
            AssetManager am = mCtx.getApplicationContext().getAssets();
            Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Wangdi_29.ttf"));
            holder.category.setTypeface(typeface);
            holder.category.setTextSize(30);

        holder.category.setText(getCategoriesTitles.getTitle());

        holder.catID.setText(String.valueOf(getCatID.getCatID()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(), DzoPhrasesDetails.class);
                intent.putExtra("cat_id",holder.catID.getText().toString());
                intent.putExtra("cat_name",holder.category.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(), DzoPhrasesDetails.class);
                intent.putExtra("cat_id",holder.catID.getText().toString());
                intent.putExtra("cat_name",holder.category.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getCategoriesTitleList.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView category,catID;
        ImageView imageView;
        CardView cardView;

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            category=itemView.findViewById(R.id.title);
            imageView=itemView.findViewById(R.id.item_image);
            cardView=itemView.findViewById(R.id.cardList);
            catID=itemView.findViewById(R.id.cat_id);
        }

    }
}

