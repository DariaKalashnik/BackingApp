package com.example.kalas.backingapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPE_KEY;
import static com.example.kalas.backingapp.utils.BuildConfig.SELECTED_STEP_KEY;

public class DetailsFragment extends Fragment implements Player.EventListener {


    private static final String TAG = DetailsFragment.class.getSimpleName();
    private Context mContext;
    private FragmentDetailsBinding mBinding;
    private ArrayList<Recipe> mRecipes;
    private Recipe mRecipe;
    private int mSelectedStepId;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Step mStep;
    private Unbinder mUnbinder;
    private long mPlayerPosition = 0;


    @BindDrawable(R.drawable.recipe_book)
    Drawable mNoMediaDescription;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static DetailsFragment newInstance(ArrayList<Recipe> recipes, int selectedStepId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(RECIPE_KEY, recipes);
        args.putInt(SELECTED_STEP_KEY, selectedStepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipes = getArguments().getParcelableArrayList(RECIPE_KEY);
            mSelectedStepId = getArguments().getInt(SELECTED_STEP_KEY);
        }
//            else
//            mRecipes = getActivity().getIntent().getParcelableArrayListExtra(RECIPE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        View view = mBinding.getRoot();
        mUnbinder = ButterKnife.bind(this, view);
        for (int i = 0; i < mRecipes.size(); i++) {
            mRecipe = mRecipes.get(i);

        }

        displayStepDescription();
        displayMediaDescription();

        return view;

    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
                // Create an instance of the ExoPlayer
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
                mBinding.exoplayer.setPlayer(mExoPlayer);

                mExoPlayer.addListener(this);

                // Prepare the MediaSource
                String userAgent = Util.getUserAgent(mContext, TAG);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mStep.getVideoURL()));
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
                mExoPlayer.seekTo(mPlayerPosition);
        }
    }

// https://codelabs.developers.google.com/codelabs/exoplayer-intro/index.html?index=..%2F..%2Findex#2
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
        if (!(mStep.getVideoURL() == null || mStep.getVideoURL().equals(""))) {
            initializeMediaSession();
            initializePlayer();

        } else {
            if (!(mStep.getThumbnailURL() == null)) {
                mBinding.exoplayer.setVisibility(View.GONE);
                mBinding.imgStepDescription.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(mStep.getThumbnailURL())
                        .into(mBinding.imgStepDescription);
            } else {
                mBinding.imgStepDescription.setImageDrawable(mNoMediaDescription);
            }
        }
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
        mUnbinder.unbind();
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

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mContext, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

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
