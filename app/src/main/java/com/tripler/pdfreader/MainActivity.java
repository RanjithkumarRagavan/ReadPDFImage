package com.tripler.pdfreader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private TextView tv;
    private Button savebtn;
    private GridView gv;

    private ArrayList<Bitmap> bmplist;
    private CustomAdapter adapter;

    File file;
    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GridView) findViewById(R.id.gv);
        tv = (TextView) findViewById(R.id.info);
        savebtn = (Button) findViewById(R.id.savebtn);

    }

    public void BrowserPDF(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    DisplayImage(FilePath);
                }
                break;
        }
    }

    public void DisplayImage(String FilePath) {
        bmplist = new ArrayList<Bitmap>();
        try {
            PdfReader reader;
            file = new File(FilePath);
            reader = new PdfReader(file.getAbsolutePath());
            for (int i = 0; i < reader.getXrefSize(); i++) {
                PdfObject pdfobj = reader.getPdfObject(i);
                if (pdfobj == null || !pdfobj.isStream()) {
                    continue;
                }
                PdfStream stream = (PdfStream) pdfobj;
                PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
                if (pdfsubtype != null
                        && pdfsubtype.toString().equals(
                        PdfName.IMAGE.toString())) {
                    byte[] img = PdfReader.getStreamBytesRaw((PRStream) stream);

                    Bitmap bmp = BitmapFactory.decodeByteArray(img, 0,
                            img.length);
                    bmplist.add(bmp);
                }
            } // for loop end

            tv.setText("This PDF File Contains " + bmplist.size() + " Images");
            adapter = new CustomAdapter(getApplicationContext(), bmplist);
            gv.setAdapter(adapter);

            if (bmplist.size() > 0) {
                savebtn.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Save Image To SDCard
    public void SaveImageToSDCard(View v) {

        for (int i = 0; i < bmplist.size(); i++) {
            try {
                String filepath = file.getParent() + "/img_" + i + ".jpeg";
                FileOutputStream fileOutputStream = new FileOutputStream(
                        filepath);
                BufferedOutputStream bos = new BufferedOutputStream(
                        fileOutputStream);
                // choose another format if JPEG doesn't suit you
                bmplist.get(i).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (Exception e) {

            }
        }
    }
}
