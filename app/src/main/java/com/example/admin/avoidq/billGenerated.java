package com.example.admin.avoidq;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.pdf.BaseFont;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.itextpdf.license.LicenseKey;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


import android.support.multidex.MultiDexApplication;


public class billGenerated extends Activity {
    Timer timer;
    int images[] = {R.drawable.back1, R.drawable.back4, R.drawable.back5};
    RelativeLayout relativeLayout;
    private static final String LOG_TAG = "GeneratePDF";

    private File pdfFile;
    private String filename = "Sample.pdf";
    private String filepath = "AvoidQ";

    private BaseFont bfBold;
    Customer customer = new Customer();
     Cart carts[];

    Bitmap bitmap11 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generated);

        relativeLayout=(RelativeLayout)findViewById(R.id.parents);
//at the beginning background color is red and it will keep changing every second
        // relativeLayout.setBackgroundColor(Color.RED);

        relativeLayout.setBackgroundResource(R.drawable.back3);

        timer=new Timer();

        MyTimerTask myTimerTask=new MyTimerTask();
//schedule to change background color every second
        timer.schedule(myTimerTask,1500,2500);
        Button logout = (Button)findViewById(R.id.logout);
        TextView finalbillid = (TextView)findViewById(R.id.finalbillid);
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        TextView tv = (TextView)findViewById(R.id.textView13);
        Button generate_pdf = (Button) findViewById(R.id.generate_pdf);
        Intent intent = getIntent();
        final String str = intent.getStringExtra("finalbillid");
        final String cust_id = intent.getStringExtra("cust_id");
        final String ipaddress = intent.getStringExtra("ipaddress");
        final String mobile_no = intent.getStringExtra("mobile_no");
        final String email = intent.getStringExtra("email");
        final String total_price = intent.getStringExtra("total_price");
        List<Cart> challenge = intent.getExtras().getParcelableArrayList("numbers");
         carts = challenge.toArray(new Cart[challenge.size()]);

        CustomerDetailsHandler handler = new CustomerDetailsHandler(getApplicationContext(),null,null,12);
        customer =  handler.databaseToCustomer();
        Log.i("customer",customer.getName()+" "+customer.getSurname());

        //CartDBHandler cartDBHandler = new CartDBHandler(getApplicationContext(), null, null, 12);
        //int count = cartDBHandler.count();
        //carts = new Cart[count];
         //carts = cartDBHandler.databaseToArray();
        for (Cart cd : carts) {
            Log.i("carts1","carts1111111 = "+cd.getBarcode()+" "+cd.getItemname()+" "+String.valueOf(cd.getPrice()));
        }
         //cartDBHandler.drop();
  /*      Cart[] c1;
        c1 = new Cart[carts.length];
        Log.i("length","length = "+String.valueOf(carts.length));
        c1[0] = carts[0];
        int j=0,count=1;
        for(int i=1;i<carts.length;i++) {

            if((carts[i-1].getBarcode()).equals(carts[i].getBarcode()) ) {
                count++;
                c1[j].setQuantity(count);
                Log.i("count", String.valueOf(count));
            }
            else {

                Log.i("object",String.valueOf(c1[j].getQuantity()));
                j++;
                count=1;
                c1[j] = carts[i];
            }
        }
        c1 = Arrays.copyOf(c1,j+1);
        for(Cart cc : c1) {
            Log.i("cc","cc = "+cc.getItemname()+" "+cc.getQuantity());
        }
        carts = null;
        carts = c1;

*/

        for(Cart c : carts) {
            Log.i("carts",c.getBarcode()+" "+c.getItemname()+" "+String.valueOf(c.getPrice()));
        }
        filename = str + ".pdf";

        ///Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        //String formattedDate = df.format(c.getTime());
        //formattedDate = str + formattedDate ;

// barcode data
        String barcode_data = "123456";
        barcode_data = String.valueOf(str);

        // barcode image

       // ImageView iv = new ImageView(this);


        try {

            bitmap11 = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 176, 127);
            iv.setImageBitmap(bitmap11);

        } catch (WriterException e) {
            e.printStackTrace();
        }

       // l.addView(iv);

        //barcode text

        tv.setText(barcode_data);


        finalbillid.setText("Your bill id is "+barcode_data);
        TextView payonline = (TextView)findViewById(R.id.payonline);


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        payonline.startAnimation(anim);

        payonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MerchantActivity.class);
                intent.putExtra("finalbillid", str);
                intent.putExtra("cust_id", cust_id);
                intent.putExtra("ipaddress", ipaddress);
                intent.putExtra("mobile_no", mobile_no);
                intent.putExtra("email", email);
                intent.putExtra("total_price", total_price);

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();

                startActivity(intent, bndlanimation);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                CustomerDetailsHandler handler = new CustomerDetailsHandler(context,null,null,12);
                handler.drop();
                Intent intent = new Intent(context, start.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);
            }
        });

        /////////////////////////////////////////////
        //need to load license from the raw resources for iText
        //skip this if you are going to use droidText
        InputStream license = this.getResources().openRawResource(R.raw.itextkey);
        LicenseKey.loadLicenseFile(license);

        //check if external storage is available so that we can dump our PDF file there
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.v(LOG_TAG, "External Storage not available or you don't have permission to write");
        }
        else {
            //path for the PDF file in the external storage
            pdfFile = new File(getExternalFilesDir(filepath), filename);
            try {
                Log.i("path",pdfFile.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        generate_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    //start the process of creating the PDF and then print it
                    case R.id.generate_pdf:
                        String personName = "harshal";//preparedBy.getText().toString();
                        generatePDF(personName);
                        break;

                }
            }
        });

    }
    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
//Since we want to change something which is on hte UI
//so we have to run on UI thread..
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {

                    Random random=new Random();//this is random generator
                    //  for(int i=0;i<500;i++) {
                    //System.out.println("***"+i);
                    relativeLayout.setBackgroundResource(images[random.nextInt(3)]);
                    //System.out.println("***"+((random.nextInt(1))%1000000000)%3);
                    //System.out.println("no"+random.nextInt(3));
                    // }
                    //  relativeLayout.setBackgroundColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//you have to stop the timer when is your activity has stopped
//otherwise it will keep running in the background
        timer.cancel();
    }
    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     *
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    private void generatePDF(final String personName){ new Thread(new Runnable() {
        public void run() {
            // a potentially  time consuming task

            //create a new document
            Document document = new Document();

            try {

                PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                document.open();


                PdfContentByte cb = docWriter.getDirectContent();
                //initialize fonts for text printing
                initializeFonts();

                //the company logo is stored in the assets which is read only
                //get the logo and print on the document
            /*
            try {

            bitmap11 = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap11);

        } catch (WriterException e) {
            e.printStackTrace();
        }
            * */
                InputStream inputStream = getAssets().open("sale.png");

                Bitmap bmp = null;//BitmapFactory.decodeStream(inputStream);
                bmp = bitmap11;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                Image companyLogo = Image.getInstance(stream.toByteArray());
                companyLogo.setAbsolutePosition(25,700);
                companyLogo.scalePercent(65);
                document.add(companyLogo);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df1.format(calendar.getTime());

                //creating a sample invoice with some customer data
                createHeadings(cb,400,780,"Date :");
                createHeadings(cb,430,780,formattedDate);

                String fname = customer.getName();
                String lname = customer.getSurname();
                createHeadings(cb,400,765,"Customer :");
                createHeadings(cb,445,765,fname +" "+lname);

                String mob = customer.getMobile_no();
                createHeadings(cb,400,750,"Mobile No :");
                createHeadings(cb,445,750,mob);

                createHeadings(cb,400,735,"Shop :");
                createHeadings(cb,430,735,"Empress Mall");

                createHeadings(cb,400,720,"City :");
                createHeadings(cb,430,720,"Nagpur");
                // createHeadings(cb,400,720,"Country");

                //list all the products sold to the customer
                float[] columnWidths = {1.5f, 3f, 2.5f, 2.5f,2f,2f};
                //create PDF table with the given widths
                PdfPTable table = new PdfPTable(columnWidths);
                // set table width a percentage of the page width
                table.setTotalWidth(500f);

                PdfPCell cell = new PdfPCell(new Phrase("S.NO"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Item Name"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Price"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Quantity"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Discount"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Amount"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                table.setHeaderRows(1);

            /*DecimalFormat df = new DecimalFormat("0.000");
            for(int i=0; i < 10; i++ ){
                double price = Double.valueOf(df.format(Math.random() * 10));
                double extPrice = price * (i+1) ;
                table.addCell(String.valueOf(i+1));
                table.addCell("ITEM" + String.valueOf(i+1));
                table.addCell(String.valueOf(5*i));
                table.addCell(String.valueOf(1));
                table.addCell(String.valueOf(i)+ " %");
                table.addCell(df.format(extPrice));
            }*/
                int sr = 0;
                DecimalFormat df = new DecimalFormat("0.0");
                double total_price =0;
                //Log.i("c0","c0 = "+carts[0].getItemname()+" "+String.valueOf(carts[0].getPrice()));
               // Log.i("c1","c1 = "+carts[1].getItemname()+" "+String.valueOf(carts[1].getPrice()));
                for (Cart cd : carts) {
                    Log.i("cartscheck","carts check = "+cd.getBarcode()+" "+cd.getItemname()+" "+String.valueOf(cd.getPrice()));
                }
                for(Cart c : carts) {
                    double price = c.getPrice();
                    int quantity = c.getQuantity();
                    price =  (price/quantity);
                    double discont = c.getDiscount();
                    sr++;
                    table.addCell(String.valueOf(sr));
                    table.addCell(c.getItemname());
                    table.addCell(df.format(price));
                    table.addCell(String.valueOf(quantity));
                    table.addCell(df.format(discont)+ " %");

                    double total = (price*quantity);
                    total_price = total_price + total;
                    Log.i("total","total in for loop = "+String.valueOf(total));
                    table.addCell(df.format(total));

                }
                float headerHeight= table.getHeaderHeight();
                Log.i("headerHeight",String.valueOf(headerHeight));
                //absolute location to print the PDF table from
                table.writeSelectedRows(0, -1, document.leftMargin(), 650, docWriter.getDirectContent());
                float total_height = table.getTotalHeight();
                Log.i("total_height",String.valueOf(total_height));
                createHeadings(cb,420,750f - 125f - total_height,"Total Price = ");
                createHeadings(cb,475,750f - 125f - total_height,String.valueOf(total_price));


                //print the signature image along with the persons name
                inputStream = getAssets().open("user.png");
                bmp = BitmapFactory.decodeStream(inputStream);
                stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image signature = Image.getInstance(stream.toByteArray());
                signature.setAbsolutePosition(400f, 750f - 125f - total_height-60f);
                signature.scalePercent(35f);
                document.add(signature);

                createHeadings(cb,450,750f - 125f - total_height-80f,fname+" "+lname);

                document.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            //PDF file is now ready to be sent to the bluetooth printer using PrintShare
            Intent i = new Intent(Intent.ACTION_VIEW);
            //   i.setPackage("com.dynamixsoftware.printershare");
            i.setDataAndType(Uri.fromFile(pdfFile),"application/pdf");
            startActivity(i);

        }
    }).start();


    }

    private void createHeadings(PdfContentByte cb, float x, float y, String text){

        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //    getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
