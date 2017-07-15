package com.example.admin.avoidq;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TableActivity extends AppCompatActivity {
    int str =0;
    String st;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RelativeLayout relativeLayout = new RelativeLayout(this);

        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        MyDBHandler cartDBHandler = new MyDBHandler(this,null,null,12);

        Intent intent = getIntent();
         str = intent.getIntExtra("finalbillid",-1);
        Cart cart[] = cartDBHandler.databaseToArray(str);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        st = str + formattedDate ;
        String[] column = { "PRODUCT NAME", "PRICE" };
        int rl=cart.length; int cl=column.length;
        String[] row = new String[rl];
        //int cnt = cart.length;



        for (int i = 0; i<rl;i++ ) {
            row[i] = String.valueOf(i+1) ;
        }

        Log.d("--", "R-Lenght--"+rl+"   "+"C-Lenght--"+cl);

        ScrollView sv = new ScrollView(this);
        final TableLayout tableLayout = createTableLayout(row, column,rl, cl);
       // setHeaderTitle(tableLayout,0,0);
                //HorizontalScrollView hsv = new HorizontalScrollView(this);

        //hsv.addView(tableLayout);
        tableLayout.setId(R.id.layout2);
        sv.addView(tableLayout);
        relativeLayout.addView(sv);
        relativeLayout.setId(R.id.layout1);

        RelativeLayout relativeLayout1 = new RelativeLayout(this);
        relativeLayout1.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        final Button button = new Button(this);
        button.setLayoutParams(new RelativeLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText("Generate PDF");
        button.setGravity(Gravity.CENTER_HORIZONTAL);
        relativeLayout.addView(button);
        relativeLayout1.addView(relativeLayout);

        setContentView(relativeLayout1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewTreeObserver vto = relativeLayout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Toast.makeText(TableActivity.this, relativeLayout.getWidth() + " x " + relativeLayout.getHeight(), Toast.LENGTH_LONG).show();

                        try {
                            File file1 = new File("/mnt/sdcard/AvoidQ/");
                            if(!file1.exists()){
                                file1.mkdirs();
                            }

                            File file = new File("/mnt/sdcard/AvoidQ/", st+".pdf");
                            PrintAttributes printAttrs = new PrintAttributes.Builder().
                                    setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                                    setMediaSize(PrintAttributes.MediaSize.ISO_A4.asPortrait()).
                                    setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 720, 1232)).
                                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                                    build();
                            PdfDocument document = new PrintedPdfDocument(TableActivity.this, printAttrs);
                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(720, 1232, 1).create();
                            PdfDocument.Page page = document.startPage(pageInfo);

                            if (page != null) {

                               final View view = findViewById(R.id.layout2);//getContentView();

                                view.layout(0, 0, 720,
                                        1232);
                                Log.i("draw view", " content size: "+view.getWidth()+" / "+view.getHeight());
                                view.draw(page.getCanvas());
                                // Move the canvas for the next view.
                                page.getCanvas().translate(0, view.getHeight());
                            }

                            document.finishPage(page);
                            OutputStream
                                    os = new FileOutputStream(file);
                            document.writeTo(os);
                            document.close();
                            os.close();
                            Log.i("done", file.getAbsolutePath().toString());

                        } catch (IOException e) {
                            throw new RuntimeException("Error generating file", e);
                        }

                        relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });

            }

        });

    }

    public void makeCellEmpty(TableLayout tableLayout, int rowIndex, int columnIndex) {
        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);


        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        // make it black
        textView.setBackgroundColor(Color.BLACK);
    }
    public void setHeaderTitle(TableLayout tableLayout, int rowIndex, int columnIndex){

        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        textView.setText("Bill");
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private TableLayout createTableLayout(String [] rv, String [] cv,int rowCount, int columnCount) {
        // 1) Create a tableLayout and its params


        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tableLayout.setBackgroundColor(Color.BLACK);



        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        MyDBHandler cartDBHandler = new MyDBHandler(this,null,null,12);
        Cart cart[] = cartDBHandler.databaseToArray(str);
        int rl = cart.length;
        String row1[] = new String[rl];
        String column1[] = new String[rl];

        int p=0;
        for (Cart c : cart) {
            row1[p] = c.getItemname();
            column1[p]= String.valueOf(c.getPrice());
            p++;

        }

        for (int i = 0; i <= rowCount; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            tableRow.setBackgroundColor(Color.BLACK);

            Cart c;
            if(i == 0) {
                c = cart[i];
            }else {
                c = cart[i-1];
            }
            for (int j = 0; j <= columnCount; j++) {
                // 4) create textView

                TextView textView = new TextView(this);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);


                if (i == 0 && j == 0) {
                    textView.setText("SR. NO.");

                } else if (i == 0) {
                    Log.d("TAAG", "set Column Headers");
                    textView.setText(cv[j-1]);
                } else if (j == 0) {
                    Log.d("TAAG", "Set Row Headers");
                    textView.setText(rv[i-1]);
                } else {

                    if (j == 1) {
                        textView.setText(c.getItemname());
                        Log.i("getItemname",c.getItemname());

                    } else if (j == 2) {
                        textView.setText(String.valueOf(c.getPrice()));
                        //Log.i("getPrice",c.getPrice());
                    }

                }
                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams);
            }
            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }
        return tableLayout;
    }
}
