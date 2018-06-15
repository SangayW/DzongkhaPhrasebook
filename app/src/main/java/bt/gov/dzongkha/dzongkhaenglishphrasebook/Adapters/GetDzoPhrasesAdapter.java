package bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoryDetailID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetDzongkhaPhrases;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

/**
 * Created by sangay on 3/2/2018.
 */

public class GetDzoPhrasesAdapter extends RecyclerView.Adapter<GetDzoPhrasesAdapter.CategoriesViewHolder> {

    private Context mCtx;
    private List<GetDzongkhaPhrases> getDzongkhaPhrasesList;
    private List<GetCategoryDetailID> getCategoryDetailIDList;
    private static ArrayList<String> getSound;

    GetDzongkhaPhrases getDzongkhaPhrases;
    GetCategoryDetailID getCategoryDetailID;

    //parameters for the animation
    private int expandedPosition = -1;
    int prev,temp = -1;
    boolean isExpanded=false;

    SQLiteDatabase sqLiteDatabase = null;
    MyDatabase helper;

    public int textSize,dzo_size;
    MediaPlayer md,md1;
    String [] phrases_detail;

    //for play all functionalities
    Timer timer;
    int i=0;
    int j=-1;
    int pos;
    ImageButton playAllBut;

    String outputFile="";
    MediaRecorder mediaRecorder;
    public static final int REQUEST_PERMISSION_CODE=1000;
    MediaPlayer remd;

    LinearLayoutManager lm;

    public GetDzoPhrasesAdapter(Context mCtx, List<GetDzongkhaPhrases> getDzongkhaPhrasesList,List<GetCategoryDetailID> getCategoryDetailIDS,int textSize,int dzo_size) {
        this.mCtx = mCtx;
        this.getDzongkhaPhrasesList = getDzongkhaPhrasesList;
        this.getCategoryDetailIDList=getCategoryDetailIDS;
        this.textSize=textSize;
        this.dzo_size=dzo_size;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mCtx);
        View view=layoutInflater.inflate(R.layout.list_english_phrases,null);
        GetDzoPhrasesAdapter.CategoriesViewHolder categoriesViewHolder=new GetDzoPhrasesAdapter.CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoriesViewHolder holder, final int position) {
        getDzongkhaPhrases=getDzongkhaPhrasesList.get(position);
        getCategoryDetailID=getCategoryDetailIDList.get(position);


            //Toast.makeText(mCtx,String.valueOf(Build.VERSION.SDK),Toast.LENGTH_SHORT).show();
        AssetManager am = mCtx.getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Wangdi_29.ttf"));
        holder.dzoPhrase.setTypeface(typeface);



        holder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    holder.record.setImageResource(R.drawable.ic_mic_none_black_24dp);
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/recording.3gp";
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.reset();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    mediaRecorder.setOutputFile(outputFile);
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    holder.record.setEnabled(false);
                    holder.stoprecord.setEnabled(true);
                    holder.playrecord.setEnabled(false);
                    Toast.makeText(mCtx, "Recording started", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(mCtx, "Error Occurred", Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.stoprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    holder.record.setImageResource(R.drawable.ic_mic_black_24dp);
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    holder.record.setEnabled(true);
                    holder.stoprecord.setEnabled(false);
                    holder.playrecord.setEnabled(true);
                    holder.stoprecord.setImageResource(R.drawable.ic_pause_black_24dp);
                    Toast.makeText(mCtx, "Recording completed!!!", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(mCtx, "Please record the audio first", Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.playrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    holder.stoprecord.setImageResource(R.drawable.ic_stop_black_24dp);
                    remd= new MediaPlayer();

                    try {
                        remd.setDataSource(outputFile);
                        remd.prepare();
                        remd.start();

                        Toast.makeText(mCtx, "Playing Recorded Audio", Toast.LENGTH_LONG).show();
                        holder.playrecord.setImageResource(R.drawable.ic_pause_black_24dp);
                        holder.record.setEnabled(true);
                        holder.stoprecord.setEnabled(false);
                        holder.playrecord.setEnabled(true);

                        remd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                holder.playrecord.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }catch(Exception e){
                    Toast.makeText(mCtx, "Record first to play the recorded audio", Toast.LENGTH_LONG).show();
                }

            }
        });


        holder.dzoPhrase.setText(getDzongkhaPhrases.getDzo_phrases());
        holder.dzoPhrase.setTextSize(TypedValue.COMPLEX_UNIT_SP, dzo_size);
        holder.cat_detail_ID.setText(String.valueOf(getCategoryDetailID.getCategory_detail_id()));

        //checking the value of favorite in category_details table
        try {
            helper= new MyDatabase(mCtx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sqLiteDatabase = helper.getWritableDatabase();

        String [] fav=new String[1];
        Cursor cursor1 = sqLiteDatabase.rawQuery("select favorite from categoryDetails where cat_details_id="+Integer.valueOf(holder.cat_detail_ID.getText().toString()),null);
        if (cursor1.getCount() != 0)
        {
            if (cursor1.moveToFirst())
            {
                do
                {
                    fav[0]=cursor1.getString(cursor1.getColumnIndexOrThrow("favorite"));
                } while (cursor1.moveToNext());
            }
        }
        cursor1.close();
        sqLiteDatabase.close();

        //Checking whether phrases are added to favorite or not
        if(Integer.valueOf(fav[0])==1){
            //phrases has been added to favorite
            holder.favorite.setImageResource(R.drawable.ic_favorite_red_24dp);
        }
        else
        {
            //phrases has been removed from favorite
            holder.favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        /*getting the roman and dzo phrase w.r.t eng_phrase*/
        try {
            helper= new MyDatabase(mCtx);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqLiteDatabase =helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select en_phrase,roman,sound from categoryDetails where cat_details_id="+Integer.valueOf(holder.cat_detail_ID.getText().toString()),null);

        phrases_detail=new String[3];
        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    phrases_detail[0]=cursor.getString(cursor.getColumnIndexOrThrow("en_phrase"));
                    phrases_detail[1]=cursor.getString(cursor.getColumnIndexOrThrow("roman"));
                    phrases_detail[2]=cursor.getString(cursor.getColumnIndexOrThrow("sound"));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        sqLiteDatabase.close();

        holder.eng_dzo.setText(phrases_detail[0]);
        holder.eng_dzo.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        holder.roman.setText(phrases_detail[1]);
        holder.roman.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        if (position == expandedPosition && isExpanded) {
            holder.cardView1.setVisibility(View.GONE);
            isExpanded=false;
            holder.cardView.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.colorWhite));

        }
        else if(position == expandedPosition){
            holder.cardView1.setVisibility(View.VISIBLE);
            isExpanded=true;

            //calling function to play audio
            playAudio();

            //play the audio when clicking on the sound button
            holder.playbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playAudioinside(holder);
                    holder.playbut.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            });

            //for playing slowly the sound
            holder.playslow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playAudioSlowly();
                }
            });

            if(temp == position)
            {
                holder.cardView.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.colorParent));
            }

            else
            {
                holder.cardView.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.colorWhite));
            }
            holder.cardView.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.colorParent));
            holder.cardView1.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.colorGray));


        }
        else {
            holder.cardView1.setVisibility(View.GONE);
            holder.cardView.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.colorWhite));
            isExpanded=false;
        }


        holder.dzoPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandedPosition >= 0) {
                    prev = expandedPosition;
                    temp=position;
                    notifyItemChanged(prev);

                }
                expandedPosition = position;
                notifyItemChanged(expandedPosition);

            }
        });

        Boolean clicked = new Boolean(false);
        holder.favorite.setTag(clicked); // wasn't clicked

        //on clicking the favorite button
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( ((Boolean)holder.favorite.getTag())==false ){
                    holder.favorite.setImageResource(R.drawable.ic_favorite_red_24dp);
                    try {
                        helper= new MyDatabase(mCtx);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sqLiteDatabase = helper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("favorite",1);
                    sqLiteDatabase.update("categoryDetails",contentValues,"cat_details_id="+Integer.valueOf(holder.cat_detail_ID.getText().toString()),null);
                    sqLiteDatabase.close();

                    holder.favorite.setTag(new Boolean(true));
                }
                else
                {
                    holder.favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    try {
                        helper= new MyDatabase(mCtx);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sqLiteDatabase = helper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("favorite",0);
                    sqLiteDatabase.update("categoryDetails",contentValues,"cat_details_id="+Integer.valueOf(holder.cat_detail_ID.getText().toString()),null);
                    sqLiteDatabase.close();

                    holder.favorite.setTag(new Boolean(false));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getDzongkhaPhrasesList.size();
    }

    public void changeTextSize(int textSize,int dzo_size){
        this.textSize=textSize;
        this.dzo_size=dzo_size;
        notifyDataSetChanged();

    }

    //getting the playall image button
    public void getPlayAll(final ImageButton playAll, final int category_id, final LinearLayoutManager linearLayoutManager){
        try{
            Boolean clicked = new Boolean(false);
            playAllBut=playAll;
            playAll.setTag(clicked); // wasn't clicked
            playAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( ((Boolean)playAll.getTag())==true ){
                        playAll.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        destroy();

                        playAll.setTag(new Boolean(false));

                    }
                    else
                    {
                        playAll.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                        playAll.setTag(new Boolean(true));
                        getSound=new ArrayList<>();
                        try {
                            helper= new MyDatabase(mCtx);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sqLiteDatabase =helper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select sound from categoryDetails where cat_id="+category_id,null);

                        phrases_detail=new String[1];
                        if (cursor.getCount() != 0)
                        {
                            if (cursor.moveToFirst())
                            {
                                do
                                {
                                    phrases_detail[0]=cursor.getString(cursor.getColumnIndexOrThrow("sound"));
                                    getSound.add(phrases_detail[0]);
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();
                        sqLiteDatabase.close();


                        Uri uri= Uri.parse("android.resource://"+ mCtx.getPackageName()+"/raw/"+getSound.get(i));
                        md = MediaPlayer.create(mCtx,uri);
                        md.start();

                        lm=linearLayoutManager;
                        pos=linearLayoutManager.findFirstVisibleItemPosition();

                        lm.findViewByPosition(pos).setBackgroundColor(Color.GRAY);

                        timer = new Timer();
                        if (getSound.size()>0){}
                        playNext();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
        }
    }

    //play the next sounds
    public void playNext(){
        try{
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    md.reset();
                    if(md.isPlaying())
                    {
                    }

                    Uri uri= Uri.parse("android.resource://"+ mCtx.getPackageName()+"/raw/"+getSound.get(++i));
                    md = MediaPlayer.create(mCtx,uri);
                    md.start();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    lm.findViewByPosition(i).setBackgroundColor(Color.GRAY);
                                    j=j+1;
                                }
                            }
                    );

                    if (getSound.size() > i+1) {
                        Handler handler1 = new Handler(Looper.getMainLooper());
                        handler1.post(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(j>=0){
                                            lm.findViewByPosition(j).setBackgroundColor(Color.WHITE);
                                        }


                                    }
                                }
                        );

                        playNext();
                    }
                    else{
                        handler.post(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        playAllBut.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                        playAllBut.setTag(new Boolean(false));
                                        //lm.findViewByPosition(i).setBackgroundColor(Color.WHITE);
                                        if(i==getSound.size()-1){
                                            lm.findViewByPosition(i-1).setBackgroundColor(Color.WHITE);
                                        }
                                        lm.findViewByPosition(getSound.size()-1).setBackgroundColor(Color.WHITE);
                                        i=0;
                                        j=-1;
                                    }
                                }
                        );
                    }
                }
            },md.getDuration()+100);
        }catch (Exception e){
            Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
        }


    }

    //destroy next sounds
    public void destroy(){
        try{
            if(md!=null){

                if (md != null && md.isPlaying())
                {
                    md.stop();
                    this.i=i;
                    md.release();
                }

                md.release();
                md=null;
                timer.cancel();
            }
        }catch (Exception e){
            Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
        }

    }
    public void destroyOnBack(){
        try{
            if(md!=null){
                try{
                    md.stop();
                }
                catch (IllegalStateException e){
                    System.out.print(e);
                }
                try{
                    timer.cancel();
                }
                catch (NullPointerException n){
                    System.out.print(n);
                }
            }
        }catch (Exception e){
            Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
        }

    }
    public void playAudioSlowly()
    {
       try{
           if(md1!=null)
           {
               md1.stop();
               md1.release();
               md1=null;
           }
           float speed = (float)0.6;
           Uri uri= Uri.parse("android.resource://"+ mCtx.getPackageName()+"/raw/"+phrases_detail[2]);
           md1 = MediaPlayer.create(mCtx,uri);

           if (md1.isPlaying()) {
               md1.pause();
           } else {
               md1.start();
           }
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               if (md1.isPlaying()) {
                   md1.setPlaybackParams(md1.getPlaybackParams().setSpeed(speed));
               } else {
                   md1.setPlaybackParams(md1.getPlaybackParams().setSpeed(speed));
                   md1.pause();
               }
           }
           md1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mediaPlayer) {
                   md1.stop();
                   md1.reset();
                   // md1.release();
               }
           });
       }catch (Exception e){
           Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
       }

    }

    public void playAudioinside(final GetDzoPhrasesAdapter.CategoriesViewHolder holder){
        //play sound functionalities
        try{
            if(md1!=null){
                md1.stop();
                md1.release();
                md1=null;
            }

            Uri uri= Uri.parse("android.resource://"+ mCtx.getPackageName()+"/raw/"+phrases_detail[2]);
            md1 = MediaPlayer.create(mCtx,uri);
            md1.start();
            /*this function is to free the resources occupied by media player*/

            md1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    holder.playbut.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    md1.stop();
                    md1.reset();
                    //md1 .release();
                }
            });
        }catch (Exception e){
            Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
        }
    }

        //for playing each audio onClick
    public void playAudio(){
        try{
            if(md1!=null){
                md1.stop();
                md1.release();
                md1=null;
            }


            //play sound functionalities
            Uri uri= Uri.parse("android.resource://"+ mCtx.getPackageName()+"/raw/"+phrases_detail[2]);
            md1 = MediaPlayer.create(mCtx,uri);
            md1.start();

            /*this function is to free the resources occupied by media player*/
            md1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    md1.stop();
                    md1.reset();
                    // md1.release();
                }
            });
        }catch (Exception e){
            Toast.makeText(mCtx,"Error occurred",Toast.LENGTH_SHORT).show();
        }
    }
    class CategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView dzoPhrase,cat_detail_ID,eng_dzo,roman;
        CardView cardView,cardView1;
        ImageButton favorite,playbut,playslow,record,playrecord,stoprecord;;

        public CategoriesViewHolder(View itemView) {
            super(itemView);

            dzoPhrase=itemView.findViewById(R.id.eng_phrase);
            cardView1=itemView.findViewById(R.id.cardList1);
            cardView=itemView.findViewById(R.id.cardList);
            cat_detail_ID=itemView.findViewById(R.id.cat_detail_id);
            eng_dzo=itemView.findViewById(R.id.eng_dzo);
            roman=itemView.findViewById(R.id.roman);
            favorite=itemView.findViewById(R.id.imageButton);
            playbut=itemView.findViewById(R.id.paly);
            playslow = itemView.findViewById(R.id.playSlow);
            record=itemView.findViewById(R.id.record);
            stoprecord = itemView.findViewById(R.id.stoprecord);
            playrecord=itemView.findViewById(R.id.playrecord);

        }
    }
}
