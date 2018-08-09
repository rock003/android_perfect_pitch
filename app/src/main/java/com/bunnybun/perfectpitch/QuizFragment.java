package com.bunnybun.perfectpitch;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment implements View.OnClickListener {
    final static String LOG_TAG = "QuizFragment";
    final static String[] SOUNDS = AppCore.getSOUNDS();
    final static int MULTIPLE_CLICK_THRESHOLD = 1000;

    private long lastClickTime = 0;
    private long lastPlayTime = 0;

    // quiz related
    private boolean waitingForInput;
    private String questionNote;

    private AppCore appCore;

    MediaPlayer mediaPlayer;
    ImageButton playButton;
    TextView fileNameTextView;
    TextView resultTextView;
    TextView statTextView;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        playButton = (ImageButton) view.findViewById(R.id.playButton);
        fileNameTextView = (TextView) view.findViewById(R.id.fileNameText);
        resultTextView = (TextView) view.findViewById(R.id.resultText);
        statTextView = (TextView) view.findViewById(R.id.statText);

        waitingForInput = false;
        appCore = AppCore.getInstance();

        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - lastPlayTime < MULTIPLE_CLICK_THRESHOLD) {
                return;
            }

            lastPlayTime = SystemClock.elapsedRealtime();

            playButton.setEnabled(false);
            startQuiz();
            }
        });

        attachButtonClickListeners(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (waitingForInput == false) {
            return;
        }

        if (SystemClock.elapsedRealtime() - lastClickTime < MULTIPLE_CLICK_THRESHOLD) {
            return;
        }

        lastClickTime = SystemClock.elapsedRealtime();

        String answerNote;

        switch (v.getId()) {
            case R.id.btn_c:
                Log.v(LOG_TAG, "button c clicked");

                answerNote = "c";

                break;

            case R.id.btn_d:
                Log.v(LOG_TAG, "button d clicked");

                answerNote = "d";

                break;

            case R.id.btn_e:
                Log.v(LOG_TAG, "button e clicked");

                answerNote = "e";

                break;

            case R.id.btn_f:
                Log.v(LOG_TAG, "button f clicked");

                answerNote = "f";

                break;

            case R.id.btn_g:
                Log.v(LOG_TAG, "button g clicked");

                answerNote = "g";

                break;

            case R.id.btn_a:
                Log.v(LOG_TAG, "button a clicked");

                answerNote = "a";

                break;

            case R.id.btn_b:
                Log.v(LOG_TAG, "button b clicked");

                answerNote = "b";

                break;

            case R.id.btn_db:
                Log.v(LOG_TAG, "button db clicked");

                answerNote = "db";

                break;

            case R.id.btn_eb:
                Log.v(LOG_TAG, "button eb clicked");

                answerNote = "eb";

                break;

            case R.id.btn_gb:
                Log.v(LOG_TAG, "button gb clicked");

                answerNote = "gb";

                break;

            case R.id.btn_ab:
                Log.v(LOG_TAG, "button ab clicked");

                answerNote = "ab";

                break;

            case R.id.btn_bb:
                Log.v(LOG_TAG, "button bb clicked");

                answerNote = "bb";

                break;

            default:
                answerNote = null;

                break;
        }

        if (answerNote != null) {
            if (answerNote == questionNote) {
                resultTextView.setText(getResources().getString(getResources().getIdentifier("result_text_correct", "string", getActivity().getPackageName()), answerNote));

                appCore.updateNoteStat(answerNote, true);
            } else {
                resultTextView.setText(getResources().getString(getResources().getIdentifier("result_text_wrong", "string", getActivity().getPackageName()), questionNote, answerNote));

                appCore.updateNoteStat(questionNote, false);
            }
        } else {
            resultTextView.setText(getResources().getString(getResources().getIdentifier("result_text_error", "string", getActivity().getPackageName())));
        }

        endQuiz();
    }

    private void attachButtonClickListeners(View view) {
        Button c = (Button) view.findViewById(R.id.btn_c);
        Button d = (Button) view.findViewById(R.id.btn_d);
        Button e = (Button) view.findViewById(R.id.btn_e);
        Button f = (Button) view.findViewById(R.id.btn_f);
        Button g = (Button) view.findViewById(R.id.btn_g);
        Button a = (Button) view.findViewById(R.id.btn_a);
        Button b = (Button) view.findViewById(R.id.btn_b);
        Button db = (Button) view.findViewById(R.id.btn_db);
        Button eb = (Button) view.findViewById(R.id.btn_eb);
        Button gb = (Button) view.findViewById(R.id.btn_gb);
        Button ab = (Button) view.findViewById(R.id.btn_ab);
        Button bb = (Button) view.findViewById(R.id.btn_bb);

        c.setOnClickListener(this);
        d.setOnClickListener(this);
        e.setOnClickListener(this);
        f.setOnClickListener(this);
        g.setOnClickListener(this);
        a.setOnClickListener(this);
        b.setOnClickListener(this);
        db.setOnClickListener(this);
        eb.setOnClickListener(this);
        gb.setOnClickListener(this);
        ab.setOnClickListener(this);
        bb.setOnClickListener(this);
    }

    public String randomSoundResourcesName() {
        Random random = new Random();

        int low = 0;
        int high = SOUNDS.length;
        int index = random.nextInt(high - low) + low;

        return SOUNDS[index];
    }

    public void playSound(String resourceName) {
        int resourceId = getResources().getIdentifier(resourceName, "raw", getActivity().getPackageName());

        fileNameTextView.setText(resourceName);

        mediaPlayer = MediaPlayer.create(getActivity(), resourceId);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;

                playButton.setEnabled(true);
            }
        });
    }

    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            playButton.setEnabled(true);
        }
    }

    public void startQuiz() {
        resultTextView.setText(getResources().getString(getResources().getIdentifier("result_text_waiting", "string", getActivity().getPackageName())));
        statTextView.setText(appCore.generateStatText());

        // if not waiting for input, it's a new quiz
        if (waitingForInput == false) {
            questionNote = randomSoundResourcesName();
        }

        playSound(questionNote);

        waitingForInput = true;
    }

    public void endQuiz() {
        stopMediaPlayer();

        waitingForInput = false;
        questionNote = null;

        statTextView.setText(appCore.generateStatText());
    }
}
