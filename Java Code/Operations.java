
import java.util.HashMap;
import java.util.Random;



public class Operations {
    
    Random rand = new Random();
    
    String end = "]";
	
    String startFood = "\"DOTS\": [";
    StringBuilder content = new StringBuilder("");
    StringBuilder finalizedFood;

    int foodCount = 0;
    volatile static HashMap<String, Food> availableFood = new HashMap<>();

    
    
    final Player p1 = new Player("P1",1, 0, 0, 0);
    final Player p2 = new Player("P2",2, 0, 44, 0);
    final Player p3 = new Player("P3",3, 0, 0, 44);
    final Player p4 = new Player("P4",4, 0, 44, 44);
    
    String startPlayers = "\"PLAYERS\": [";
    String finalizedPlayers;


    String jsonObj;

    // add a new food to the food list and json food list and updates the json object
    void addFood(char color, int x, int y){
            Food newFood = new Food(color, x, y);
            if(foodCount == 0){
                    content.append(newFood.toString());
                    foodCount ++;
            }

            else
                    content.append(","+newFood.toString());

          
            availableFood.put(x+","+y, newFood);
    }
    
    void generateFood(){
        String colors = "RGB";
        
        for(int i = 0; i < 12; i++){
            char currentColor = colors.charAt(rand.nextInt(colors.length()));
            int currentX = rand.nextInt(36) + 4;
            int currentY = rand.nextInt(36) + 4;
            
            addFood(currentColor, currentX, currentY);
        }
    }

    // update the content of json object's food list
    void foodJSON(){
            content = new StringBuilder("\n");

            int control = 1;
            for(String key : availableFood.keySet()){
                    content.append(availableFood.get(key).toString());
                    if(control < foodCount)
                            content.append(",");

                    control++;
            }
    }

    // finalizes the json object's food list
    void finalizeFood(){
            finalizedFood = new StringBuilder(startFood + content.toString() + end);
    }


    // finalizes the json object's players list
    void finalizePlayers(){
            finalizedPlayers = startPlayers + p1.toString() + "," + p2.toString() + "," + p3.toString() + "," + p4.toString() + end;
    }

    // moves a player
    void movePlayer(Player p, int plusX, int plusY){

            if(p.x + plusX >= 0 && p.x + plusX <= 44 && p.y + plusY >= 0 && p.y + plusY <= 44){
                    p.x = p.x + plusX;
                    p.y = p.y + plusY;

                    //eat(p);

                    finalizeJson();
            }

    }

    // decodes the keystrokes and move players
    void decodeMove(int inp, Player p){

            int addToX = 0;
            int addToY = 0;

            switch(inp){
                    case 37 : 
                            addToX = -1;
                            break;
                    case 38 : 
                            addToY = -1;
                            break;
                    case 39 : 
                            addToX = 1;
                            break;
                    case 40 : 
                            addToY = 1;
                            break;
            }

            movePlayer(p, addToX, addToY);
    }
	
	void collidePlayers(Player p){
		boolean collide = false;
		
		
	}
	
	// removes a food if a player eat it
	void eat(Player p){ 
		String place = p.x + "," + p.y;
		
		if(availableFood.containsKey(place)){
			
			Food removable = availableFood.get(place);
			
			switch(removable.color){
				case 'R' : 
					p.score = p.score + 1;
					break;
				case 'G' : 
					p.score = p.score + 2;
					break; 
				case 'B' :
					p.score = p.score + 4;
					break;
			}
			
			availableFood.remove(place);
			
			foodJSON();
			
			finalizeFood();
			
		}
	}
	
	// finalizes the json object
	void finalizeJson(){
		finalizeFood();
		finalizePlayers();
		jsonObj = "{" + finalizedFood.toString() + "," + finalizedPlayers + "}"; 
	}
}
