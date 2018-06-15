package bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass;

import android.widget.Toast;

/**
 * Created by sangay on 4/10/2018.
 */

public class TextSize {
    int textSize;
    int dzo_size;

    public TextSize(){

    }

    public TextSize(int textSize,int dzo_size) {
        this.textSize = textSize;
        this.dzo_size=dzo_size;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getDzo_size() {
        return dzo_size;
    }
}
