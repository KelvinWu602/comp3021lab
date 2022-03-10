package base;

import java.util.Collections;
import java.util.Date;

public class Note implements Comparable<Note>{
    private Date date;
    private String title;

    public Note(String title){
        this.title = title;
        date = new Date(System.currentTimeMillis());
    }

    public String getTitle() {
        return title;
    }

    public boolean equals(Note note) {
        return title.equals(note.getTitle());
    }

    @Override
    public int compareTo(Note o) {
        int v = date.compareTo(o.date);
        if(v>0) return -1;
        if(v<0) return 1;
        return v;
    }

    public boolean contains(String str){
        return title.toLowerCase().contains(str.toLowerCase());
    }

    public String toString(){
        return date.toString() + "\t" + title;
    }
}