package com.bunnybun.perfectpitch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppCore implements Serializable {
	private static volatile AppCore instance;

    final static String[] SOUNDS = {"a", "ab", "b", "bb", "c", "d", "db", "e", "eb", "f", "g", "gb"};

    private List<Note> notes;

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

    public static String[] getSOUNDS() {
        return SOUNDS;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public String firstLetterUppercase(String str) {
	    String result = "";

	    if (str.length() > 0) {
	        if (str.length() == 1) {
                result = str.toUpperCase();
            } else {
	            result = str.substring(0, 1).toUpperCase() + str.substring(1);
            }
        }

	    return result;
    }

    public void updateNoteStat(String name, boolean correct) {
	    Note note = getNoteByName(name);

	    note.updateStat(correct);
    }

    public String generateStatText() {
        String text = "";

        for (Note n : notes) {
            text += firstLetterUppercase(n.getName()) + ": " + n.getNumCorrect() + "/" + n.getNumTotal() + " " + String.format("%.2f", n.getCorrectPercentage()) + "%" + System.getProperty("line.separator");
        }

        return text;
    }
}
