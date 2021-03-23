package com.shatha;


import android.widget.TextView;
import com.qcri.farasa.pos.FarasaPOSTagger;
import com.qcri.farasa.pos.Sentence;
import com.qcri.farasa.pos.Clitic;
import com.qcri.farasa.segmenter.Farasa;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


    public class TryingFarasaPOS {
        public void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException,
                UnsupportedEncodingException, InterruptedException, Exception {


            Farasa farasa = new Farasa();
            FarasaPOSTagger farasaPOS = new FarasaPOSTagger(farasa);

            ArrayList<String> segOutput = farasa.segmentLine("افتح الكامرا");

            Sentence sentence = farasaPOS.tagLine(segOutput);
            for (Clitic w : sentence.clitics)
                txtview.setText(w.surface + "/" + w.guessPOS + ((w.genderNumber != "") ? "-" + w.genderNumber : "") + "");
        }
    }
}

public class Frasanlp {

}
