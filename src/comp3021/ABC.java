package comp3021;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ABC{
    public static void main(String[] args){
        Path path = Paths.get(".");
        Path absolutePath = path.toAbsolutePath();
        System.out.println(absolutePath.toString());
    }
}