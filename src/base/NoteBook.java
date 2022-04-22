package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements Serializable{
    private ArrayList<Folder> folders;
    private static final long serialVersionUID = 1L;

    public NoteBook(){
        folders = new ArrayList<>();
    }

    public boolean createTextNote(String folderName, String title){
        TextNote note = new TextNote(title,"");
        return insertNote(folderName, note);
    }

    public boolean createTextNote(String folderName, String title, String content){
        TextNote note = new TextNote(title,content);
        return insertNote(folderName, note);
    }

    public boolean createImageNote(String folderName, String title){
        ImageNote note = new ImageNote(title);
        return insertNote(folderName, note);
    }

    public ArrayList<Folder> getFolders(){
        return folders;
    }

    private boolean insertNote(String folderName, Note note){
        Folder folder = null;
        for(Folder f: folders){
            if(f.getName().equals(folderName)){
                folder = f;
            }
        }
        if(folder == null){
            folder = new Folder(folderName);
            folders.add(folder);
        }
        for(Note n: folder.getNotes()){
            if(n.equals(note)){
                System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
                return false;
            }
        }
        folder.addNote(note);
        return true;
    }

    public void sortFolders(){
        for(Folder f: folders){
            f.sortNotes();
        }
        Collections.sort(folders);
    }

    public List<Note> searchNotes(String keywords){
        List<Note> output = new ArrayList<>();
        for(Folder f: folders){
            output.addAll(f.searchNotes(keywords));
        }
        return output;
    }

    public boolean save(String file){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addFolder(String foldername){
        folders.add(new Folder(foldername));
    }

    public NoteBook(String file) {
        NoteBook nb = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);
            nb = (NoteBook) in.readObject();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        if(nb!=null)
            this.folders = nb.folders;
    }
}
