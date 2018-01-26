package bt.gov.dzongkha.dzongkhaenglishphrasebook.FeedBackActivity;

/**
 * Created by sangay on 1/24/2018.
 */

import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

public class FeedBackActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        return inflater.inflate(R.layout.activity_feed_back, container, false);
    }
}
