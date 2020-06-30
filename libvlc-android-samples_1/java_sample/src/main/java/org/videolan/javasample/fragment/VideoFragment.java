package org.videolan.javasample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.videolan.javasample.R;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class VideoFragment extends Fragment {

    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String ASSET_FILENAME = "bbb.m4v";

    private VLCVideoLayout mVideoLayout = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    public static VideoFragment newInstance(String path) {

        Bundle args = new Bundle();
        args.putString("video_path", path);

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

//        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(R.dimen._398sdp, R.dimen._257sdp);
//        view.setLayoutParams(p);
//        view.requestLayout();
        //convert dip to pixels
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        final ArrayList<String> args = new ArrayList<>();
        args.add("--vout=android-display");
        args.add("-vvv");
        mLibVLC = new LibVLC(getContext(), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mVideoLayout = view.findViewById(R.id.video_layout);
        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);

        try {
            final Media media;
            if (getArguments().getString("video_path") == ASSET_FILENAME)
                media = new Media(mLibVLC, getContext().getAssets().openFd(ASSET_FILENAME));
            else
                media = new Media(mLibVLC, getArguments().getString("video_path"));
            mMediaPlayer.setMedia(media);
            media.release();
        } catch (Exception e) {
            throw new RuntimeException("Invalid folder");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMediaPlayer.play();
        videoLoop();
        mMediaPlayer.setAspectRatio("20:9");
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaPlayer.play();
        videoLoop();
    }

    @Override
    public void onResume() {
//        mMediaPlayer.play();
        videoLoop();
        super.onResume();
    }

    @Override
    public void onStop() {
        mMediaPlayer.stop();
        super.onStop();
    }

    public void videoLoop() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer.setTime(0);
//                mMediaPlayer.play();
//                long time = Long.parseLong(simpleDateFormat.format(mMediaPlayer.getLength()));
//                Toast.makeText(VideoFragment.this.getContext(), "" + mMediaPlayer.getLength(), Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, mMediaPlayer.getLength());
            }
        }, 100);
    }
}
