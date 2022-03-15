package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Folder implements Comparable<Folder>, Serializable{
    private ArrayList<Note> notes;
    private String name;
    private static final long serialVersionUID = 2L;

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

    @Override
    public int compareTo(Folder o) {
        return name.compareTo(o.name);
    }

    public void sortNotes(){
        Collections.sort(notes);
    }

    public List<Note> searchNotes(String keywords){
        String[] tokens = keywords.split(" ");
        List<List<String>> searchlist = new LinkedList<List<String>>();
        List<String> requirement;
        int i = 0;
        while(i+1<tokens.length){
            if(tokens[i+1].toLowerCase().equals("or")==false){
                requirement = new LinkedList<String>(); 
                requirement.add(tokens[i]);
                searchlist.add(requirement);
            }else{
                requirement = new LinkedList<>();
                while(i+1<tokens.length && tokens[i+1].toLowerCase().equals("or")==true){
                    requirement.add(tokens[i]);
                    i+=2;
                }
                if(i+1<=tokens.length){
                    requirement.add(tokens[i]);
                }
                searchlist.add(requirement);
            }
            i+=1;
        }
        if(i<tokens.length){
            requirement = new LinkedList<>(); 
            requirement.add(tokens[i]);
            searchlist.add(requirement);
        }
    
        List<Note> output = new ArrayList<Note>();
        for(Note n: notes){
            boolean passAnd = true;
            for(List<String> or : searchlist){
                boolean passOr = false;
                for(String s: or){
                    if(n.contains(s)){
                        passOr = true;
                        break;
                    }
                }
                if(passOr==false){
                    passAnd = false;
                    break;
                }
            }
            if(passAnd){
                output.add(n);
            }
        }
        return output;
    }
}
