
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(urlPatterns = {"/NewServlet", "/update"})

public class NewServlet extends HttpServlet {

    int playerCount;
    Operations operations;
    Player [] plList; 
    
    volatile int moveBy;
    
    int [][] initialPositions = {{0,0}, {44,0}, {0,44}, {44,44}};
    HashMap <String, Player> currentPlayers;
    
     public void init(ServletConfig config) {
        currentPlayers =  new HashMap<String, Player>();
        playerCount = 0;
        
        operations = new Operations();
        plList = new Player[4];
        plList[0] = operations.p1;
        plList[1] = operations.p2;
        plList[2] = operations.p3;
        plList[3] = operations.p4;
        
        operations.generateFood();
        
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/event-stream");
        response.flushBuffer();
                
        PrintWriter out = response.getWriter();
        operations.finalizeJson();
        out.print("data: ");
        out.println(operations.jsonObj + "\n");
        
        if(playerCount < 4){
            HttpSession session = request.getSession(true);
            String key = session.getId();
            
            currentPlayers.put(key, plList[playerCount]);
            playerCount++;
            
            operations.finalizeJson();
            out.print("data: ");
            out.println(operations.jsonObj + "\n");
        }   
                 
        
        if(playerCount == 4){        
                
                while (!Thread.interrupted()){
                    synchronized(this){
                    out.print("data: ");
                    out.println(operations.jsonObj + "\n");
                    
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }   

    } 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
            
        //if("/update".equals(request.getServletPath())){
        if(playerCount < 4){
        synchronized(this){
            moveBy = Integer.parseInt(request.getParameter("keypress"));

            HttpSession session = request.getSession();
            String key = session.getId();

            Player p = currentPlayers.get(key);

            operations.decodeMove(moveBy, p);
            
            //doGet(request,response);
            notifyAll();
            
        }
        }
        
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
