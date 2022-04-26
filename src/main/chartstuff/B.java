//package chartstuff;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//
//public class B {
//    int val;
//
//    public B(int x) {
//        this.val = x;
//    }
//
//    public int add(B x, B y) {
//        this.val = this.val + x.val;
//        this.val = this.val + y.val;
//        return this.val;
//    }
//
//    public static void main(String[] args) {
//
//        try {
//            String id = "9780439358071";
//            String mylink = "https://www.bookfinder.com/search/?author=&title=&lang=en&isbn="
//                    + id
//                    + "&new_used=*&destination=ca&currency=CAD&mode=basic&st=sr&ac=qr";
//            Document document = Jsoup.connect(mylink).get();
//
//            // Get cover
//            String text = document.select("img[id^=coverImage]").attr("src");
//            System.out.println(text.toString());
//
//            //Title
//            Elements title = document.select("span[id^=describe-isbn-title]");
//            System.out.println(title.text());
//
//            //Author
//            Elements author = document.select("span[itemprop = author]");
//            System.out.println(author.text());
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
//
//
//
//// BACKUP SCAN ACTIVITY HERE
//
////package com.example.bookit;
////
////        import androidx.appcompat.app.AppCompatActivity;
////
////        import android.content.Intent;
////        import android.os.Bundle;
////        import android.os.StrictMode;
////        import android.widget.TextView;
////
////        import com.budiyev.android.codescanner.CodeScanner;
////        import com.budiyev.android.codescanner.CodeScannerView;
////
////        import org.jsoup.Jsoup;
////        import org.jsoup.nodes.Document;
////        import org.jsoup.select.Elements;
////
////        import java.io.IOException;
////
////public class ScanActivity extends AppCompatActivity {
////    private CodeScanner mCodeScanner;
////    private String urlSite = "https://openlibrary.org/isbn/";
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_scan);
////        CodeScannerView scannerView = findViewById(R.id.scanneview);
////        mCodeScanner = new CodeScanner(this, scannerView);
////
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////
////        StrictMode.setThreadPolicy(policy);
////
////        mCodeScanner.setDecodeCallback(result -> this.runOnUiThread(() -> {
////            TextView holder = findViewById(R.id.scanResults);
////            String ibsn = result.getText();
////            String[] bookDetails = getInfo(ibsn);
////            Intent i = new Intent(this, AddNew.class);
////            i.putExtra("data",bookDetails);
////            startActivity(i);
////        }));
////        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
////    }
////
////    private String[] getInfo(String i) {
////        String[] data = {"title", "author", "url"};
////        String bookPage = urlSite + i;
////
////        try {
////            Document document = Jsoup.connect(bookPage).get();
////
////            //Title
////            Elements title = document.select("h1[class^=work-title]");
////            data[0] = title.text();
////
////            //Author
////            Elements author = document.select("h2[class^=edition-byline]");
////            data[1] = author.text().substring(3);
////
////            // Get cover
////            Elements text = document.select("div[class^=SRPCover bookCover]");
////            Elements linker = text.select("a");
////            String val = linker.first().attr("href").toString().substring(2);
////            data[2] = "https://"+val;
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return data;
////    }
////
////
////}
