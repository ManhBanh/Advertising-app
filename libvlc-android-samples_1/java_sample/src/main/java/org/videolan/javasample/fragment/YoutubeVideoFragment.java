package org.videolan.javasample.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.videolan.javasample.R;

public class YoutubeVideoFragment extends Fragment {

    Activity mainActivity = getActivity();
    private WebView wvYoutubeVideo;
    private String youtubeVideo = "<html><body>" +
            "<iframe width=\"743\" height=\"418\" src=\"https://www.youtube.com/embed/Zb4H5jCocW0\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>" +
            "</body></html";

    public static YoutubeVideoFragment newInstance() {

        Bundle args = new Bundle();

        YoutubeVideoFragment fragment = new YoutubeVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_video, container, false);
        wvYoutubeVideo = view.findViewById(R.id.wvYoutubeVideo);
        wvYoutubeVideo.setWebViewClient(new MyWebViewClient());
        wvYoutubeVideo.setWebChromeClient(new MyWebChromeClient());
        wvYoutubeVideo.getSettings().setJavaScriptEnabled(true);
        wvYoutubeVideo.getSettings().setDomStorageEnabled(true);
        wvYoutubeVideo.getSettings().setLoadWithOverviewMode(true);
        wvYoutubeVideo.getSettings().setUseWideViewPort(true);
        wvYoutubeVideo.loadData(youtubeVideo, "text/html", "utf-8");
        return view;
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        private View customView;
        private WebChromeClient.CustomViewCallback customViewCallback;
        protected FrameLayout fullscreenContainer;
        private int originalOrientation;
        private int originalSystemUiVisibility;

        MyWebChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (customView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) mainActivity.getWindow().getDecorView()).removeView(this.customView);
            this.customView = null;
            mainActivity.getWindow().getDecorView().setSystemUiVisibility(this.originalSystemUiVisibility);
            mainActivity.setRequestedOrientation(this.originalOrientation);
            this.customViewCallback.onCustomViewHidden();
            this.customViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paracustomViewCallback) {
            if (this.customView != null) {
                onHideCustomView();
                return;
            }
            this.customView = paramView;
            this.originalSystemUiVisibility = mainActivity.getWindow().getDecorView().getSystemUiVisibility();
            this.originalOrientation = mainActivity.getRequestedOrientation();
            this.customViewCallback = paracustomViewCallback;
            ((FrameLayout) mainActivity.getWindow().getDecorView()).addView(this.customView, new FrameLayout.LayoutParams(-1, -1));
            mainActivity.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

}
