package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

public class TextNote extends Note {
    private String content;
    public TextNote(String title){
        super(title);
    }
    public TextNote(String title, String content){
        super(title);
        this.content = content;
    }

    public boolean contains(String str){
        return super.contains(str) || content.toLowerCase().contains(str.toLowerCase());
    }

    public TextNote(File f) {
        super(f.getName());
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    public String getContent(){
        return content;
    }

    public static String getTextFromFile(String absolutePath) {
        StringBuilder contentbuffer = new StringBuilder();
        try{
            Scanner input = new Scanner(new File(absolutePath));
            while(input.hasNextLine()){
                contentbuffer.append(input.nextLine());
                if(input.hasNextLine())
                    contentbuffer.append("\n");
            }
            input.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return contentbuffer.toString();
    }

    public void exportTextToFile(String pathFolder){
        if(pathFolder.equals(""))
            pathFolder = ".";
        File file = new File(pathFolder + File.separator + getTitle().replaceAll(" ", "_") + ".txt" );
        try{
            PrintWriter output = new PrintWriter(file);
            output.write(content);
            output.flush();
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
