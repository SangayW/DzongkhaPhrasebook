package bt.gov.dzongkha.dzongkhaenglishphrasebook.FeedBackActivity;

/**
 * Created by sangay on 1/24/2018.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

public class FeedBackActivity extends Fragment {
    EditText message,name,email;
    Button sendBtn;
    String Message,Name,Email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        return inflater.inflate(R.layout.activity_feed_back, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Feedback");
        giveFeedBack(view);
    }

    public void giveFeedBack(View view){
        message=view.findViewById(R.id.message);
//        name=view.findViewById(R.id.name);
//        email=view.findViewById(R.id.email);
//
//        sendBtn=view.findViewById(R.id.feedback);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message=message.getText().toString();
                Name=name.getText().toString();
                Email=email.getText().toString();

                new SendMail().execute("");
            }
        });
    }
    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(), "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {

            Mail m = new Mail("sanglim2012@gmail.com", "Sangay@1995@16923882");

            String[] toArr = {"sanglim2012@gmail.com"};
            m.setTo(toArr);
            m.setFrom(Email);
            m.setSubject("FeedBack");
            m.setBody(Message);

            try {
                if(m.send()) {
                    Toast.makeText(getContext(), "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }

}
