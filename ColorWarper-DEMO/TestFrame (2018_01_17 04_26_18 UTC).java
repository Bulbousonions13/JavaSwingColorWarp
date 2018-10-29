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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class TestFrame extends JFrame implements MouseListener{
  
//  private components;
  private Buttons buttons;          // single buttons class instance passed in via driver                               
  private JScrollPane scrollpane;   // JScrollPane used exclusively for loading/displaying images
  private JLabel label;             // This JLabel is the componenet that holds the image. it is then added to the above JScrollPane. 
  private BufferedImage curr_image; // This is the image currently displayed in the JScrollPane
//  private BufferedImage last_image; // To be used for undo operations
  
  private File curr_file;           // The current file curr_image was loaded from
  private String extension;         // The extension of the incoming/outgoing file.
  private JFileChooser chooser = new JFileChooser();  // The JFile Chooser instance used for the load/save dialogue windows
  private boolean start = false;    // set to false for the first logo image, becomes true once Select Photo is pressed once
  
  private int xpos;                 // The x position of mouse-click
  private int ypos;                 // They y position of mouse-click 
  
  public TestFrame(Buttons buttons){
    
    super("Color Warping Application");
    setLayout(new BorderLayout()); 
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    try{                            // Load the Intro image and setup the scrollpane
      label = new JLabel(new ImageIcon(ImageIO.read(new File("ColorWarp.png"))));    
      scrollpane = new JScrollPane(label);
      scrollpane.addMouseListener(this);
    }catch(IOException e){
      System.out.println("Intro Image Loading Error");
    } 
    scrollpane.setMaximumSize(new Dimension(getWidth(),getHeight()-100));
    getContentPane().add(scrollpane);
    getContentPane().add(buttons,BorderLayout.SOUTH);    
    this.buttons=buttons;
    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//  System.out.println(scrollpane.getWidth()+","+scrollpane.getHeight()); // for Testing
  
  }
  
  /** Returns the current image displayed in the JScrollPane
    * @return curr_image
   */ 
  public BufferedImage getImage(){
    return curr_image;
  }
  /** Sets the curr_image variable displayed in the JScrollPane
    * @param image accepts any BufferedImage object 
   */ 
  public void setImage(BufferedImage image){
    curr_image=image;
  }
  /** Uses the JFileChooser chooser to open a new image by setting the curr_file and curr_image variables to the selected 
   *  file. it also identifies the file extension and sets the extension variable.
   *  
   */ 
  public void openImageFile(){
    
    int returnVal = chooser.showOpenDialog(this);      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        curr_file = chooser.getSelectedFile();                     
      }      
      try{
        curr_image = ImageIO.read(curr_file);
      }catch(IOException e){
        System.out.println("Error Opening Image from File");
      }      
      String s =  curr_file.getPath();
      extension = s.substring(s.length()-3);
      System.out.println(extension);
      System.out.println(curr_image.getWidth()+","+curr_image.getHeight());
  }
  /** Uses the JFileChooser chooser instance to save an image.
    * 
   */ 
   public void saveImageFile(){  
    
      chooser.setDialogTitle("Specify a file to save");
      int userSelection = chooser.showSaveDialog(this);
 
      if (userSelection == JFileChooser.APPROVE_OPTION) {              
        try{
          ImageIO.write(curr_image, extension , chooser.getSelectedFile());
        }catch(IOException e){
          System.out.println("Error Saving File.");
          e.printStackTrace();
        }
      }
  }         
  /** @return the scrollpane instance diplaying the current image.
   */ 
   public JScrollPane getScrollPane(){   
    return scrollpane;
   }
   /** Loads the JLabel label with the curr_image, then sets the scrollpane to view the label. it also checks if this
     * is the first time and image is loaded, and if so, it sets up the tool buttons in the buttons JPanel.
     * 
   */   
   public void loadNewImage(){    
    
    try{    
      label = new JLabel(new ImageIcon(ImageIO.read(curr_file)));    
      scrollpane.setViewportView(label);
    }catch(IOException e){
      System.out.println("Image Loading Error");
    }    
    getContentPane().add(scrollpane);
    scrollpane.revalidate();
    scrollpane.repaint();
    revalidate();
    repaint();
    pack();
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    if (!start){
      start=true;
      buttons.setupTools();
    }
    System.out.println(scrollpane.getWidth()+","+scrollpane.getHeight());
  }
   /** This method is called by the mouseClick method implemented by the MouseListener. It is the logic that calculates 
     * the mouse-click coordinates of the JScrollPane. This needs to be done as the scrollbar
     * not only takes up pixel space, but the scrolled amount of both horizontal and vertical scrollbars changes the final click values.
     * This currently is not exact for very large images. If an image is 1980 x 1080 for example, 
     * the true image pixel values may be as much as 5 pixels off the mouse-click. This is due to 
     * the placement of images in the JScrollPane sizing algorithm, which I have not totally figured out.
     * @param e a mouseclick event received from the JScrollPane scrollpane
     * 
   */ 
   public void clickSetXY(MouseEvent e){
     
     try{       
       int xconstant = (scrollpane.getWidth()/2) - (curr_image.getWidth()/2);      
       int yconstant = (scrollpane.getHeight()/2) - (curr_image.getHeight()/2);      
       if(curr_image.getHeight()>scrollpane.getHeight()){
       xconstant-=8;
       yconstant+=(curr_image.getHeight()-scrollpane.getHeight())/2;
       }
       if(curr_image.getWidth()>scrollpane.getWidth()){
       yconstant+=8;
       xconstant+=(curr_image.getWidth()-scrollpane.getWidth())/2;
       }      
       int xscrolled = getScrollPane().getHorizontalScrollBar().getValue();
       int yscrolled = getScrollPane().getVerticalScrollBar().getValue();
       xpos = e.getX() + xscrolled - xconstant;
       ypos = e.getY() + yscrolled - yconstant;     
     }catch(Throwable a){     
       System.out.println("Mouse click out of bounds");
     }
   }
   /** This method calls clickSetXY(), then passes the mouseClick's coordinates, as well as the RGB values of that pixel to the native buttons method outValues();
    *  @param e a mouseclick event received from the JScrollPane scrollpane
    *  
   */ 
  
   public void mouseClicked(MouseEvent e){
   
     try{
      clickSetXY(e);      
      int[] pixel = new int[4];      
      curr_image.getRaster().getPixel(xpos,ypos,pixel);
      int r = pixel[0];
      int g = pixel[1];
      int b = pixel[2];
      
      buttons.outValues(xpos,ypos,r,g,b);
      }catch(Throwable a){     
       System.out.println("Mouse click out of bounds");
     }
   }
   /** This method is used when Bounding Box mode is active. It is the complement to MouseReleased and calls the clickSetXY method
     * and the setBoundingLT() method from Buttons to set the left top corner of the bounding box
    *  @param e a mouseclick event received from the JScrollPane scrollpane
    *  
   */ 
   public void mousePressed(MouseEvent e){
     
     if(buttons.isBounding()){
      try{ 
      clickSetXY(e);       
      buttons.setBoundingLT(xpos,ypos);
      }catch(Throwable a){     
        System.out.println("Mouse click out of bounds");
      }
     }     
   }   
   /** This method is used when Bounding Box mode is active. It is the complement to MousePressed and calls the clickSetXY method
     * and the setBoundingRB() method from Buttons to set the bottom right corner of the bounding box
    * @param e a mouseclick event received from the JScrollPane scrollpane
    * 
   */ 
   public void mouseReleased(MouseEvent e) {
   
      if(buttons.isBounding()){        
        try{
        clickSetXY(e);       
        buttons.setBoundingRB(xpos,ypos);
        }catch(Throwable a){     
          System.out.println("Mouse click out of bounds");
        }       
      }  
   }
   /** 
    * Necessary to implement mouseListener, but not required
   */ 
   public void mouseEntered(MouseEvent e){}
   /**
    * Necessary to implement mouseListener, but not required
   */ 
   public void mouseExited(MouseEvent e){}
}