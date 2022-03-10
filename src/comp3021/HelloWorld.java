package comp3021;

import java.util.LinkedList;
import java.util.List;

public class HelloWorld {
   
    public boolean hey(){
        return false;
    }


    public void p(String keywords){
        String[] tokens = keywords.split(" ");
        List<List<String>> searchlist = new LinkedList<List<String>>();
        
        int i = 0;
        while(i+1<tokens.length){
            if(tokens[i+1].toLowerCase().equals("or")==false){
                List<String> requirement = new LinkedList<>(); 
                requirement.add(tokens[i]);
                searchlist.add(requirement);
            }else{
                List<String> requirement = new LinkedList<>();
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
            List<String> requirement = new LinkedList<>(); 
            requirement.add(tokens[i]);
            searchlist.add(requirement);
        }
        

        for(List<String> r: searchlist){
            for(String s : r){
                System.out.print(s+" or ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        HelloWorld w = new ABC();
        w.p("hello may Or EMO");
    }

    
}
