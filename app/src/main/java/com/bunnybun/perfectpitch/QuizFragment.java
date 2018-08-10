package com.bunnybun.perfectpitch;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    TextView questionTextView;
    TextView resultTextView;
    TextView progressTextView;
    EditText settingNumNoteView;

    OnQuizEndListener quizEndCallback;

    public QuizFragment() {
        // Required empty public constructor
    }

    public interface OnQuizEndListener {
        void onQuizEnd();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        playButton = (ImageButton) view.findViewById(R.id.playButton);
        questionTextView = (TextView) view.findViewById(R.id.questionText);
        resultTextView = (TextView) view.findViewById(R.id.resultText);
        progressTextView = (TextView) view.findViewById(R.id.progressText);
        settingNumNoteView = (EditText) view.findViewById(R.id.settingNumNotes);

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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnQuizEndListener) {
            quizEndCallback = (OnQuizEndListener) context;
        }
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
                answerNote = "C";

                break;

            case R.id.btn_d:
                answerNote = "D";

                break;

            case R.id.btn_e:
                answerNote = "E";

                break;

            case R.id.btn_f:
                answerNote = "F";

                break;

            case R.id.btn_g:
                answerNote = "G";

                break;

            case R.id.btn_a:
                answerNote = "A";

                break;

            case R.id.btn_b:
                answerNote = "B";

                break;

            case R.id.btn_db:
                answerNote = "Db";

                break;

            case R.id.btn_eb:
                answerNote = "Eb";

                break;

            case R.id.btn_gb:
                answerNote = "Gb";

                break;

            case R.id.btn_ab:
                answerNote = "Ab";

                break;

            case R.id.btn_bb:
                answerNote = "Bb";

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
                resultText += getResources().getString(getResources().getIdentifier("result_text_correct", "string", getActivity().getPackageName()), answerNote) + " ";

                appCore.updateNoteStat(answerNote, true);
            } else {
                resultText += getResources().getString(getResources().getIdentifier("result_text_wrong", "string", getActivity().getPackageName()), answerNote) + " ";
            }

            resultTextView.setText(resultText.trim());
        } else {
            resultTextView.setText(getResources().getString(getResources().getIdentifier("result_text_error", "string", getActivity().getPackageName())));
        }

        if (currentAnswerIndex == appCore.getNumOfSoundsToPlay()) {
            questionTextView.setText(TextUtils.join(", ", appCore.getQuestionNotes()));

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

        // setup media players
        for (int i = 0; i < questionNotes.length; i++) {
            int resourceId = getResources().getIdentifier(questionNotes[i].toLowerCase(), "raw", packageName);

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
            int numNotes = Integer.parseInt(settingNumNoteView.getText().toString());

            if (numNotes == 0 || numNotes >= appCore.SOUNDS.length) {
                return;
            } else {
                appCore.setNumOfSoundsToPlay(numNotes);

                settingNumNoteView.setFocusable(false);
            }

            // remove and reset answers and labels
            currentAnswerIndex = 0;
            answers = new ArrayList<String>();
            resultText = "";

            progressTextView.setText(getResources().getString(getResources().getIdentifier("progress_text_waiting", "string", getActivity().getPackageName()), currentAnswerIndex, appCore.getNumOfSoundsToPlay()));
            resultTextView.setText(resultText);
            questionTextView.setText("");

            appCore.resetQuestionNotes();
        }

        playSounds(appCore.getQuestionNotes());

        waitingForInput = true;
    }

    public void endQuiz() {
        releaseMediaPlayers();

        waitingForInput = false;
        appCore.resetQuestionNotes();

        quizEndCallback.onQuizEnd();

        settingNumNoteView.setFocusableInTouchMode(true);
    }
}
