package org.videolan.javasample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.videolan.javasample.R;

public class WebFragment extends Fragment {

    WebView wvWeb;

    public static WebFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);

        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        wvWeb = view.findViewById(R.id.wvWeb);
        wvWeb.setWebViewClient(new myWebViewClient());
        wvWeb.getSettings().setJavaScriptEnabled(true);
        wvWeb.getSettings().setLoadsImagesAutomatically(true);
        wvWeb.loadUrl(getArguments().getString("url"));
        wvWeb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        autoScroll();
        return view;
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            //Mở trên một app khác, không mở trên webview của mình
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            return true;
        }
    }

    public void autoScroll(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int height = (int) Math.floor(wvWeb.getContentHeight() * wvWeb.getScale());
                int webViewHeight = wvWeb.getMeasuredHeight();
                if (wvWeb.getScrollY() + webViewHeight >= height) {
                    wvWeb.scrollBy(0, 0);
                    wvWeb.scrollTo(0, 0);
                } else {
                    wvWeb.scrollBy(wvWeb.getTop(), wvWeb.getBottom());
                }
                handler.postDelayed(this, 5000);
            }
        }, 500);
    }
}