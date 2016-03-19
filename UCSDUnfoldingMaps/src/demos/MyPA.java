package demos;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Kai on 2016-01-18.
 */
public class MyPA extends PApplet{

   PImage img;

    public void setup(){
        background(255);
        stroke(0);
        img = loadImage("http://cseweb.ucsd.edu/~minnes/palmTress.jpg", "jpg");
        img.resize(0, height);
        image(img, 0, 0);

    }
    public void draw() {
        int[] color = sunColorSec(second());                // calculate color code for sun
        fill(color[0], color[1], color[2]);                 // set sun color
        ellipse(width/4, height/5, width/4, height/5);      // draw sun
    }

    public int[] sunColorSec(float seconds){
        int[] rgb = new int[3];
        float diffFrom30 = Math.abs(30-seconds);

        float ratio = diffFrom30/30;
        rgb[0] = (int)(255*ratio);
        rgb[1] = (int)(255*ratio);
        rgb[2] = 0;

        return rgb;
    }

    public static void main(String[] args){


    }
}
