
public class Player {
    
    String name;
    int index;
    int score;
    int x;
    int y;
	
	Player(String name, int index, int score, int x, int y){
		this.name = name;
                this.index = index;
		this.score = score;
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return "[\"" + name + "\", " + score + ", " + x + ", " + y + "]";
	}
        
}
