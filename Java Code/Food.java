
public class Food {
    
    char color;
    int x;
    int y;

    public Food(char color, int x, int y){
            this.color = color;
            this.x = x;
            this.y = y;
    }

    public String toString(){
            return "[\"" + color + "\"," + x + "," + y + "]";
    }
        
}
