package com.example.andyapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.andyapp.adapters.ActivityViewPagerAdapter;
import com.example.andyapp.fragments.AlertsFragment;
import com.example.andyapp.fragments.TransactionsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class ActivityActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // to change the font of the Activity header, toolbar cannot change directly in xml
        //Typeface typeface = ResourcesCompat.getFont(this, R.font.fredoka_medium);
//        for (int i = 0; i < toolbar.getChildCount(); i++) {
//            View view = toolbar.getChildAt(i);
//            if (view instanceof TextView) {
//                TextView tv = (TextView) view;
//                if (tv.getText().equals(toolbar.getTitle())) {
//                    tv.setTypeface(typeface);
//                    break;
//                }
//            }
//        }


        toolbar.setNavigationOnClickListener(view -> {
            Log.d("LOGCAT", "IM here");
            Intent intent = new Intent(ActivityActivity.this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish();
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ActivityViewPagerAdapter adapter = new ActivityViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("TRANSACTIONS");
            else tab.setText("ALERTS");
        }).attach();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // make sure you have ic_share icon
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            int currentTab = viewPager.getCurrentItem();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + currentTab);

            if (fragment instanceof TransactionsFragment) {
                List<String> lines = ((TransactionsFragment) fragment).getShareableLines();
                exportToPdf("Transactions", lines);
            } else if (fragment instanceof AlertsFragment) {
                List<String> lines = ((AlertsFragment) fragment).getShareableLines();
                exportToPdf("Alerts", lines);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportToPdf(String title, List<String> lines) {
        PdfDocument pdfDoc = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
        PdfDocument.Page page = pdfDoc.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        titlePaint.setTextSize(18f);
        titlePaint.setFakeBoldText(true);

        canvas.drawText(title, 40, 50, titlePaint);

        paint.setTextSize(14f);
        int y = 80;

        for (String line : lines) {
            canvas.drawText(line, 40, y, paint);
            y += 25;
            if (y > 800) break; // limit to 1 page (optional)
        }

        pdfDoc.finishPage(page);

        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (dir != null && !dir.exists()) dir.mkdirs();
        File pdfFile = new File(dir, title + "_export.pdf");

        try {
            pdfDoc.writeTo(new FileOutputStream(pdfFile));
            pdfDoc.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdfFile),
                    "application/pdf"
            );
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (IOException e) {
            StyleableToast.makeText(this, "Error writing PDF: " + e.getMessage(), R.style.custom_toast).show();
            e.printStackTrace();
        }
    }
}
