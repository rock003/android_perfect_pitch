package com.bunnybun.perfectpitch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AppCore implements Serializable {
	private static volatile AppCore instance;

    final static String[] SOUNDS = {"A", "Ab", "B", "Bb", "C", "D", "Db", "E", "Eb", "F", "G", "Gb"};

    private List<Note> notes;
    private String[] questionNotes;

    private int numOfSoundsToPlay = 3;

	private AppCore() {
	    if (instance != null) {
	        throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        } else {
	        initNoteList();
        }
    }

    public static AppCore getInstance() {
	    if (instance == null) {
	        synchronized (AppCore.class) {
	            if (instance == null) {
	                instance = new AppCore();
                }
            }
        }

        return instance;
    }

    protected AppCore readResolve() {
	    return getInstance();
    }

    private void initNoteList() {
	    notes = new ArrayList<Note>();

	    for (String s : SOUNDS) {
	        notes.add(new Note(s));
        }
    }

    private Note getNoteByName(String name) {
        Note found = null;

        for (Note n : this.notes) {
            if (n.getName().equals(name)) {
                found = n;
            }
        }

        return found;
    }

    private int randomInt(int high) {
	    Random random = new Random();
	    int low = 0;
	    int result = 0;

	    if (high > 0) {
	        result = random.nextInt(high - low) + low;
        }

        return result;
    }

    private String[] generateQuestionNotes(int num) {
        String[] result = new String[num];
        ArrayList<String> soundsCopy = new ArrayList<String>(Arrays.asList(SOUNDS));

        for (int i = 0; i < num; i++) {
            int randomIndex = randomInt(soundsCopy.size());

            result[i] = soundsCopy.get(randomIndex);

            soundsCopy.remove(randomIndex);
        }

        return result;
    }

    // TODO: remove this later, maybe don't need this
    public static String[] getSOUNDS() {
        return SOUNDS;
    }

    // TODO: remove this later, maybe don't need this
    public List<Note> getNotes() {
        return notes;
    }

    public int getNumOfSoundsToPlay() {
        return numOfSoundsToPlay;
    }

    public void setNumOfSoundsToPlay(int numOfSoundsToPlay) {
        this.numOfSoundsToPlay = numOfSoundsToPlay;
    }

    public String[] getQuestionNotes() {
	    if (questionNotes == null) {
	        questionNotes = generateQuestionNotes(numOfSoundsToPlay);
        }

        return questionNotes;
    }

    public void resetQuestionNotes() {
	    questionNotes = null;
    }

//    public String firstLetterUppercase(String str) {
//	    String result = "";
//
//	    if (str.length() > 0) {
//	        if (str.length() == 1) {
//                result = str.toUpperCase();
//            } else {
//	            result = str.substring(0, 1).toUpperCase() + str.substring(1);
//            }
//        }
//
//	    return result;
//    }

    public void updateNoteStat(String name, boolean correct) {
	    Note note = getNoteByName(name);

	    note.updateStat(correct);
    }

    public String generateStatText() {
        String text = "";

        for (Note n : notes) {
            text += n.getName() + ": " + n.getNumCorrect() + " / " + n.getNumTotal() + " --- " + String.format("%.2f", n.getCorrectPercentage()) + "%" + System.getProperty("line.separator") + System.getProperty("line.separator");
        }

        return text;
    }
}
