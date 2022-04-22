package base;

import java.io.File;
import java.io.Serializable;

public class ImageNote extends Note  {
    private File image;

    public ImageNote(String title){
        super(title);
    }
}
