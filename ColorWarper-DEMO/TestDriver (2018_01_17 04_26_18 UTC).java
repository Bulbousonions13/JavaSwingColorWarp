/** 
 * This is the TestFrame class - the parent container for all GUI Componenets in the ColorWarp Application.
 * It is instantiated in the TestDriver class, which contains the main run method of the application.
 * 
 * @author  Edward Weiss
 * @version 1.0
 * @since   2017-05-07 
*/
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TestDriver{
  
   public static void main(String[] args){
     
    Buttons buttons = new Buttons(); // creates the Buttons instance to be used in the program
    TestFrame frame = new TestFrame(buttons); // creates the Testframe instance to be used as the top level window
    buttons.linkFrame(frame); // passes a reference of frame to the buttons instance
     
  }

}