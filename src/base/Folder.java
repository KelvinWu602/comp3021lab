package base;

import java.util.ArrayList;

public class Folder {
    private ArrayList<Note> notes;
    private String name;

    public Folder(String name){
        this.name = name;
        notes = new ArrayList<>();
    }

    public void addNote(Note note){
        notes.add(note);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Note> getNotes(){
        return notes;
    }

    public boolean equals(Folder folder){
        return name.equals(folder.getName());
    }

    @Override
    public String toString(){
        int nText = 0;
        int nImage = 0;
        for(Note note: notes){
            if(note instanceof TextNote) nText++;
            if(note instanceof ImageNote) nImage++;
        }
        return name+":"+nText+":"+nImage;
    }
}
