/** 
 * This is the TestFrame class - the parent container for all GUI Componenets in the ColorWarp Application.
 * It is instantiated in the TestDriver class, which contains the main run method of the application.
 * 
 * @author  Edward Weiss
  * @version 1.0
  * @since   2017-05-07 
*/
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

public class ColorWarper{
  
  private BufferedImage curr_image;
  private BufferedImage out;
  
  
  private int lowR;  // The lower limits of the user entry selecting targetable pixels
  private int lowG;
  private int lowB;
  private int highR; // The upper limits of the user entry selecting targetable pixels
  private int highG;
  private int highB;
  private int finR;  // The final color of the pixels being targeted
  private int finG;
  private int finB;
  private int tlx;   // The boundaries of the bounding box
  private int tly;
  private int brx;
  private int bry;
  private Color lowColor; // Variables of the Color class that can be used for convenience in later code
  private Color highColor;
  private Color finalColor;
  private Color offsetColor; // the color to be used in offset mode
  private int offR;            
  private int offG;            
  private int offB;
  private Buttons buttons; // the Buttons instance passed to this class on instantiation
  
  /**
   * @param buttons the Buttons instance passed to this class on instantiation
   * @param curr_image the image to be colorwarped
   * @param lowR lower Red limit
   * @param lowG lower Green limit
   * @param lowB lower Blue limit
   * @param highR upper Red limit
   * @param highG upper Green limit
   * @param highB upper Blue limit
   * @param finR target Red Value
   * @param finG target Green Value
   * @param finB target Blue Value
   */ 
  public ColorWarper(Buttons buttons, BufferedImage curr_image, int lowR,int lowG,int lowB,int highR,int highG,int highB,int finR,int finG,int finB){
     
     this.buttons=buttons;
     this.curr_image=curr_image;
     this.lowR=lowR;
     this.lowG=lowG;
     this.lowB=lowB;
     this.highR=highR;
     this.highG=highG;
     this.highB=highB;
     this.finR=finR;
     this.finG=finG;
     this.finB=finB;
     lowColor = new Color(lowR,lowG,lowB);
     highColor = new Color(highR,highG,highB);     
     finalColor = new Color(finR,finG,finB);
     offsetColor=new Color(0,0,0);
     this.tlx=0;
     this.tly=0;
     this.brx=0;
     this.bry=0;     
  }
  /** This method actually performs the logic that determines if the current pixel being read is in the range of the user entry
    * @param pixel The 4 element array holding the pixels RGBT values
   */ 
  public boolean logic(int[] pixel){   
    boolean result=false;    
    if( (pixel[0]>=lowR)&&(pixel[0]<=highR) && (pixel[1]>=lowG)&&(pixel[1]<=highG) && (pixel[2]>=lowB)&&(pixel[2]<=highB) ){       
      result=true;    
    }   
    return result;
  }
  /** This method sets the top left and bottom right corner of the bounding box.
    * @param tlx passed from mousePressed in the linked TestFrame instance
    * @param tly passed from mousePressed in the linked TestFrame instance
    * @param brx passed from mousePressed in the linked TestFrame instance
    * @param bry passed from mousePressed in the linked TestFrame instance
   */   
  public void setBoundingBox(int tlx, int tly, int brx, int bry ){    
    this.tlx=tlx;
    this.tly=tly;
    this.brx=brx;
    this.bry=bry;  
  }
  /**
   * This method is the workhorse of the class. It reads through the pixels of the curr_image, alters them if they are
   * within the user's entry range, and then writes them to another BufferedImage called out. If the bounding box
   * is activated, there is additional logic to calculate the new colors. Finally it returns the BufferdImage out.    
   * @return the BufferedImage out, which has been altered based on the warping parameters;
   */ 
  public BufferedImage getWarpedImage(){      
    
      out = curr_image;      
      Graphics2D g2 = out.createGraphics();     
      try{          
      for(int y=tly;y<bry;y++){
        for(int x=tlx;x<brx;x++){          
          int[] pixel = new int[4];
          curr_image.getRaster().getPixel(x,y,pixel);
          
          if(buttons.isOffsetting()){
            
            int offR;
            int offG;
            int offB;
            offR = pixel[0]+finR;
            if(offR>255){offR=255;}
            offG = pixel[1]+finG;
            if(offG>255){offG=255;}
            offB = pixel[2]+finB;
            if(offB>255){offB=255;}   
            
            g2.setColor(new Color(offR,offG,offB));
            }else{              
              g2.setColor(finalColor);          
            }     
            if(logic(pixel)){
              g2.fillRect(x,y,1,1);
            }
        }
      }      
      }catch(Throwable a){
       System.out.println("Offset Error");
       a.printStackTrace();
      }      
      return out; 
  }
}