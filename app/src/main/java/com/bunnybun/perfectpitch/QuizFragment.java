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

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment implements View.OnClickListener {
    final static String LOG_TAG = "QuizFragment";
    final static int MULTIPLE_CLICK_THRESHOLD = 1000;

    private long lastClickTime = 0;
    private long lastPlayTime = 0;

    private boolean waitingForInput;
    private ArrayList<String> answers;
    private int currentAnswerIndex;
    private String resultText;

    private AppCore appCore;

    MediaPlayer[] mediaPlayers;
    ImageButton playButton;
    TextView fileNameTextView;
    TextView resultTextView;
    TextView statTextView;
    TextView progressTextView;

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
        progressTextView = (TextView) view.findViewById(R.id.progressText);

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
            answers.add(answerNote);
            currentAnswerIndex++;

            progressTextView.setText(getResources().getString(getResources().getIdentifier("progress_text_waiting", "string", getActivity().getPackageName()), currentAnswerIndex, appCore.getNumOfSoundsToPlay()));

            if (Arrays.asList(appCore.getQuestionNotes()).contains(answerNote)) {
                resultText += getResources().getString(getResources().getIdentifier("result_text_correct", "string", getActivity().getPackageName()), appCore.firstLetterUppercase(answerNote)) + " ";

                appCore.updateNoteStat(answerNote, true);
            } else {
                resultText += getResources().getString(getResources().getIdentifier("result_text_wrong", "string", getActivity().getPackageName()), appCore.firstLetterUppercase(answerNote)) + " ";
            }

            resultTextView.setText(resultText.trim());
        } else {
            resultTextView.setText(getResources().getString(getResources().getIdentifier("result_text_error", "string", getActivity().getPackageName())));
        }

        if (currentAnswerIndex == appCore.getNumOfSoundsToPlay()) {
            updateWrongAnswerStat();

            endQuiz();
        }
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

    private void releaseMediaPlayers() {
        for (int i = 0; i < mediaPlayers.length; i++) {
            if (mediaPlayers[i] != null) {
                if (mediaPlayers[i].isPlaying()) {
                    mediaPlayers[i].stop();
                }

                mediaPlayers[i].release();
                mediaPlayers[i] = null;
            }
        }

        playButton.setEnabled(true);
    }

    private void updateWrongAnswerStat() {
        for (String note : appCore.getQuestionNotes()) {
            if (answers.contains(note) == false) {
                appCore.updateNoteStat(note, false);
            }
        }
    }

    public void playSounds(String[] questionNotes) {
        mediaPlayers = new MediaPlayer[questionNotes.length];
        String packageName = getActivity().getPackageName();

        // TODO: show the question note name for testing, remove later
        String questions = "";

        // setup media players
        for (int i = 0; i < questionNotes.length; i++) {
            int resourceId = getResources().getIdentifier(questionNotes[i], "raw", packageName);

            questions += appCore.firstLetterUppercase(questionNotes[i] + " ");

            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), resourceId);

            // TODO: find a way to achieve this?
//            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(0.75f));

            mediaPlayers[i] = mediaPlayer;
        }

        // setup next media players
        for (int i = 0; i < questionNotes.length - 1; i++) {
            mediaPlayers[i].setNextMediaPlayer(mediaPlayers[i + 1]);
        }

        // play from the first one
        mediaPlayers[0].start();

        // TODO: show the question note name for testing, remove later
        fileNameTextView.setText(questions);

        mediaPlayers[questionNotes.length - 1].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayers();
            }
        });
    }

    public void startQuiz() {
        // if not waiting for input, it's a new quiz
        if (waitingForInput == false) {
            // remove and reset answers and labels
            currentAnswerIndex = 0;
            answers = new ArrayList<String>();
            resultText = "";

            progressTextView.setText(getResources().getString(getResources().getIdentifier("progress_text_waiting", "string", getActivity().getPackageName()), currentAnswerIndex, appCore.getNumOfSoundsToPlay()));
            resultTextView.setText(resultText);
            statTextView.setText(appCore.generateStatText());

            appCore.resetQuestionNotes();
        }

        playSounds(appCore.getQuestionNotes());

        waitingForInput = true;
    }

    public void endQuiz() {
        releaseMediaPlayers();

        waitingForInput = false;
        appCore.resetQuestionNotes();

        statTextView.setText(appCore.generateStatText());
    }
}
