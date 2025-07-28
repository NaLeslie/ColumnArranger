package arrangecolumns;

import java.util.ArrayList;

/**
 *
 * @author Nathaniel
 */
public class Column {
    
    public Column(){
        header = "";
        data = new ArrayList<>();
    }
    
    private String header;
    private ArrayList<String> data;
    
    public void addDataEntry(String data_entry){
        data.add(data_entry);
    }    
    
    public Column getCopy(){
        Column copy = new Column();
        copy.header = this.header + "";
        for(int i = 0; i < this.data.size(); i++){
            copy.data.add(this.data.get(i));
        }
        return copy;
    }
    
    public String getDataEntry(int index){
        if(index < data.size()){
            return data.get(index);
        }
        return "NaN";
    }
    
    public int getDataLength(){
        return data.size();
    }
    
    public String getHeader(){
        return header;
    }
    
    public void setHeader(String header){
        this.header = header;
    }
    
}
