package com.example.kalas.backingapp.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.databinding.FragmentDetailsBinding;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.model.Step;
import com.example.kalas.backingapp.utils.Utils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.kalas.backingapp.activities.StepsListActivity.sTabLayout;
import static com.example.kalas.backingapp.utils.BuildConfig.RECIPES_KEY;
import static com.example.kalas.backingapp.utils.BuildConfig.SELECTED_STEP_KEY;

public class DetailsFragment extends Fragment implements Player.EventListener {


    private static final String TAG = DetailsFragment.class.getSimpleName();
    private FragmentDetailsBinding mBinding;
    private ArrayList<Recipe> mRecipes;
    private Recipe mRecipe;
    private int mSelectedStepId;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Step mStep;
    private long mPlayerPosition = 0;

    public DetailsFragment() {
        // Empty public constructor
    }

    public static DetailsFragment newInstance(ArrayList<Recipe> recipes, int selectedStepId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(RECIPES_KEY, recipes);
        args.putInt(SELECTED_STEP_KEY, selectedStepId);
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipes = getArguments().getParcelableArrayList(RECIPES_KEY);
            mSelectedStepId = getArguments().getInt(SELECTED_STEP_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        if (!sTabLayout) {
            mRecipes = getActivity().getIntent().getParcelableArrayListExtra(RECIPES_KEY);
            mSelectedStepId = getActivity().getIntent().getIntExtra(SELECTED_STEP_KEY, 0);
        }

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        View view = mBinding.getRoot();

        mRecipe = Utils.setRecipe(mRecipes);

        displayStepDescription();
        displayMediaDescription();

        return view;
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector);
            mBinding.exoplayer.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(this.getContext(), TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this.getContext(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mStep.getVideoURL()));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void displayStepDescription() {
        mStep = mRecipe.getSteps().get(mSelectedStepId);
        mBinding.setStepModel(mStep);
        mBinding.stepDetailedDescription.setText(mStep.getDescription());
    }

    private void displayMediaDescription() {
        Drawable noMediaDescription = getResources().getDrawable(R.drawable.question);
        boolean hasVideo = false;
        if (!(mStep.getVideoURL().equals(""))) {
            hasVideo = true;
            initializeMediaSession();
            initializePlayer();
        }

        if (!hasVideo && !mStep.getThumbnailURL().equals("")) {
            mBinding.imgStepDescription.setVisibility(View.VISIBLE);
            mBinding.exoplayer.setVisibility(View.GONE);
            Picasso.with(this.getContext())
                    .load(mStep.getThumbnailURL())
                    .error(noMediaDescription)
                    .into(mBinding.imgStepDescription);

        } else if (!hasVideo && mStep.getThumbnailURL().equals("")) {
            mBinding.imgStepDescription.setVisibility(View.VISIBLE);
            mBinding.exoplayer.setVisibility(View.GONE);
            mBinding.imgStepDescription.setImageDrawable(noMediaDescription);
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(this.getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active
        mMediaSession.setActive(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }
}
