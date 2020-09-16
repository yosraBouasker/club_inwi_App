package com.inwi.clubinwi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.inwi.clubinwi.achoura.AchouraUtils;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {

    // private final String gcPdfUrl = "http://rct.club.inwi.ma/sites/default/files/reglement.pdf";
    private final String gcPdfUrl = "http://rct.club.inwi.ma/sites/default/files/R%C3%A8glement%20du%20jeu%20-Achoura.pdf";

    private final int STORAGE_REQUEST_CODE = 12;

    private PDFView pdfView;
    private ProgressBar progressBar;

    private String viewType;

    // *****************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                loadAgreement();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // *****************

    private void init() {
        pdfView = findViewById(R.id.pdf_viewer);
        progressBar = findViewById(R.id.progress_bar);

        viewType = getIntent().getStringExtra("ViewType");

        checkConnectivity();
    }

    private void checkConnectivity() {
        if (AchouraUtils.isNetworkAvailable(this)) {
            checkStorageAccess();
        } else {
            Snackbar.make(pdfView, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction(R.string.action_retry, v -> checkConnectivity())
                    .show();
        }
    }

    private void checkStorageAccess() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadAgreement();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }
    }

    private void loadAgreement() {
        if (viewType.equalsIgnoreCase("internet")) {
            progressBar.setVisibility(View.VISIBLE);
            FileLoader.with(this)
                    .load(gcPdfUrl)
                    .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
                    .asFile(new FileRequestListener<File>() {
                        @Override
                        public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                            progressBar.setVisibility(View.GONE);
                            File pdfFile = response.getBody();
                            pdfView.fromFile(pdfFile)
                                    .password(null)
                                    .defaultPage(0)
                                    .enableSwipe(true)
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .onTap(e -> true)
                                    .onRender((nbPages, pageWidth, pageHeight) -> pdfView.fitToWidth())
                                    .enableAnnotationRendering(true)
                                    .invalidPageColor(Color.WHITE)
                                    .load();
                        }

                        @Override
                        public void onError(FileLoadRequest request, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(PdfViewerActivity.this, "Erreur!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
