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
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Buttons extends JPanel implements ActionListener{
  
  
  // These are all the Swing components in the JPanel organized with a combination of FlowLayout,BorderLayout, and BoxLayout
  private JButton selectphoto = new JButton("Select Photo");
  private JButton save = new JButton("Save");
  private JButton colorwarp = new JButton("ColorWarp");
  private JButton revert = new JButton("Revert");
  private JTextField targpix1owR = new JTextField("0",3);
  private JTextField targpix1owG = new JTextField("0",3);
  private JTextField targpix1owB = new JTextField("0",3);
  private JTextField targpixhighR = new JTextField("255",3);
  private JTextField targpixhighG = new JTextField("255",3);
  private JTextField targpixhighB = new JTextField("255",3);
  private JTextField finalpixR = new JTextField("0",3);
  private JTextField finalpixG = new JTextField("0",3);
  private JTextField finalpixB = new JTextField("0",3);
  private JLabel instruclabel = new JLabel("Click on a pixel to get it's coordinates and RGB values:");
  private JTextField coordsoutX = new JTextField("X",4);
  private JTextField coordsoutY = new JTextField("Y",4);
  private JTextField colorout = new JTextField("R,G,B",11);
  private JLabel instruclabel2 = new JLabel("Bounding Box:");
  private JLabel instruclabel3 = new JLabel("TopLeft:");
  private JTextField coordsintopleftX = new JTextField("0",4);
  private JTextField coordsintopleftY = new JTextField("0",4);
  private JLabel instruclabel4 = new JLabel("Bottomright:");  
  private JTextField coordsinbottomrightX = new JTextField("500",4);
  private JTextField coordsinbottomrightY = new JTextField("500",4);
  private JRadioButton boundingon = new JRadioButton("Bounding:On/Off");
  private JRadioButton offseton = new JRadioButton("Offset:On/Off");
  
  
  
  // These 3 JPanels are organized one on top of the other to make up each line in Buttons.
  
  private JPanel upper = new JPanel(new BorderLayout());
  private JPanel middle = new JPanel(new FlowLayout());
  private JPanel lower = new JPanel(new FlowLayout());
  
  
  private TestFrame frame;   //  this is the top level frame            
  
  
  // these are the user entry pixel value variables
  
  private int lowR;
  private int lowG;
  private int lowB;
  private int highR;
  private int highG;
  private int highB;
  private int finR;
  private int finG;
  private int finB;                                  
  
  
  public Buttons(){
    
    startButtons();
    
  }
  /** This method links the main TestFrame instance to this Buttons instance.
    * @param frame the top level container, a TestFrame object
   */                                   
  public void linkFrame(TestFrame frame){     
    this.frame=frame;
  }                                 
  /** This method sets up the buttons panel for the first user interaction window, before selecting an image to warp.
   */                                   
  public void startButtons(){      
    setLayout(new BoxLayout(this,BoxLayout.Y_AXIS)); 
    setMaximumSize(new Dimension(3000,300));
    selectphoto.setMaximumSize(new Dimension(100,50));
    upper.add(selectphoto);
    selectphoto.addActionListener(this);
    add(upper);          
  }
  /** This method sets up all of the buttons and other swing components once the user has selected the first image to warp.
   */                                   
  public void setupTools(){
   
   middle.setPreferredSize(new Dimension(frame.getWidth(),50));   
   add(middle); 
   middle.add(save);
   save.addActionListener(this); 
   middle.add(colorwarp);
   colorwarp.addActionListener(this);
   middle.add(revert);
   revert.addActionListener(this);
   middle.add(new JLabel("Target Pixels Colors LOW:"));
   middle.add(new JLabel("R:"));
   middle.add(targpix1owR);
   middle.add(new JLabel("G:"));
   middle.add(targpix1owG);
   middle.add(new JLabel("B:"));
   middle.add(targpix1owB);
   middle.add(new JLabel("Target Pixel Colors HIGH:"));
   middle.add(new JLabel("R:"));
   middle.add(targpixhighR);
   middle.add(new JLabel("G:"));
   middle.add(targpixhighG);
   middle.add(new JLabel("B:"));
   middle.add(targpixhighB);
   middle.add(new JLabel("Final Pixel Colors:"));
   middle.add(new JLabel("R:"));
   middle.add(finalpixR);
   middle.add(new JLabel("G:"));
   middle.add(finalpixG);
   middle.add(new JLabel("B:"));
   middle.add(finalpixB);
   middle.add(offseton);   
   lower.setPreferredSize(new Dimension(frame.getWidth(),50));
   add(lower);
   lower.add(instruclabel);
   lower.add(coordsoutX);
   lower.add(coordsoutY);
   lower.add(colorout);
   lower.add(instruclabel2);
   lower.add(instruclabel3);   
   lower.add(coordsintopleftX);
   lower.add(coordsintopleftY);
   lower.add(instruclabel4);
   lower.add(coordsinbottomrightX);
   lower.add(coordsinbottomrightY);
   lower.add(boundingon);
}
  /** This method converts all the user entries from Strings to Integers
   */ 
 public void grabColorValues(){
    
  try{
    lowR=  Integer.parseInt(targpix1owR.getText());
    lowG=  Integer.parseInt(targpix1owG.getText());
    lowB=  Integer.parseInt(targpix1owB.getText());
    highR= Integer.parseInt(targpixhighR.getText());
    highG= Integer.parseInt(targpixhighG.getText());
    highB= Integer.parseInt(targpixhighB.getText());
    finR=  Integer.parseInt(finalpixR.getText());
    finG=  Integer.parseInt(finalpixG.getText());
    finB=  Integer.parseInt(finalpixB.getText());  
  }catch(Throwable e){
    System.out.println("You've entered a non-integer, please fix it.");
  }
 }
 /** This method creates a new ColorWarper object, and passes to it all the user entries from the JTextFields. It then
   * performs logic to determine of bounddix box mode is on or off, and sets the coordinates appropriately
   */ 
 public void setWarper(){
   
   try{
   grabColorValues();
   ColorWarper warper = new ColorWarper(this,frame.getImage(),lowR,lowG,lowB,highR,highG,highB,finR,finG,finB);   
   
     if(boundingon.isSelected()){
       System.out.println("Bounding is enabled");
       warper.setBoundingBox(Integer.parseInt(coordsintopleftX.getText()),Integer.parseInt(coordsintopleftY.getText()),Integer.parseInt(coordsinbottomrightX.getText()),Integer.parseInt(coordsinbottomrightY.getText()));
     }   
     else{
       warper.setBoundingBox(0,0,frame.getImage().getWidth(),frame.getImage().getHeight());
       System.out.println("Bounding is disabled");
     }
     frame.setImage(warper.getWarpedImage());
     ImageIcon icon = new ImageIcon(frame.getImage());  
     frame.getScrollPane().setViewportView(new JLabel(icon));
     frame.getScrollPane().repaint();
     frame.repaint();
   }catch(Throwable e){
     System.out.println("You've entered a non-integer, please fix it.");
   }   
   
 }
 /**  This method displays the x,y,r,g,b data it receives from mouseclicks occuring in the top frame's scrollpane. It then
   *  displays them in the associated JTextfields.
   * 
   * @param x x coordinate of mouseclick
   * @param y y coordinate of mouseclick
   * @param r r value of clicked pixel
   * @param g g value of clicked pixel
   * @param b b value of clicked pixel
   */ 
 public void outValues(int x, int y, int r, int g, int b){ 
   coordsoutX.setText(Integer.toString(x));
   coordsoutY.setText(Integer.toString(y));
   colorout.setText(Integer.toString(r)+","+Integer.toString(g)+","+Integer.toString(b));
 }
 /** True or false if Bounding mode is selected
  *  @return boolean value if boundingon radio button selected or not
   */ 
 public boolean isBounding(){ 
   boolean retval;   
   if(boundingon.isSelected()){   
     retval=true;     
   }else{     
     retval=false;   
   }   
   return retval;
 }
 /** True or false if offset mode is selected
  *  @return boolean value if offseton radio button selected or not
   */ 
 public boolean isOffsetting(){ 
   boolean retval;   
   if(offseton.isSelected()){   
     retval=true;    
   }else{
     retval=false;
   }
   return retval;
 }
 /** Sets the top left corner of the bounding box
   * 
   * @param x x value top left
   * @param y y value top left
   * 
   */ 
 public void setBoundingLT(int x,int y){   
   coordsintopleftX.setText(Integer.toString(x));
   coordsintopleftY.setText(Integer.toString(y));
 }
 /** Sets the bottom right corner of the bounding box
   * 
   * @param x x value bottom right
   * @param y y value bottom left
   */ 
  public void setBoundingRB(int x, int y){    
    coordsinbottomrightX.setText(Integer.toString(x));
    coordsinbottomrightY.setText(Integer.toString(y)); 
 }                                    
//  public void saveRevert(){
//  last_image = new BufferedImage(curr_image.getWidth(),curr_image.getHeight(),curr_image.getType());
//  Graphics g2 = last_image.createGraphics();
//      for(int y=0;y<curr_image.getHeight();y++){
//        for(int x=0;x<curr_image.getWidth();x++){
//          int[] pixel = new int[4];
//          curr_image.getRaster().getPixel(x,y,pixel);
//          g2.setColor(new Color(pixel[0],pixel[1],pixel[2],pixel[3]));
//          g2.fillRect(x,y,1,1);          
//        }
//      }
//  }
 /** This is the actionPerformed method required to be implemented by ActionListener. It gives simple actions to the
   * selectphoto,colorwarp,and save buttons
   * @param e an ActionEvent
  */ 
   public void actionPerformed(ActionEvent e){  
    if(e.getSource()==selectphoto){      
      frame.openImageFile();
      frame.loadNewImage();   
    }   
    if(e.getSource()==colorwarp){     
      setWarper();      
    }
    if(e.getSource()==save){      
      frame.saveImageFile();    
    }
    
//    This method is not complete.  
//    if(e.getSource()==revert){      
//      
//      ImageIcon icon = new ImageIcon(last_image);  
//      scrollpane.setViewportView(new JLabel(icon));
//      scrollpane.repaint();      
//    }
  }
}