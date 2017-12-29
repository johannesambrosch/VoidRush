class Obstacle {

  int xpos, ypos;
  int offsetX, offsetY;
  PImage pic;

  Obstacle(int x, int y, int ox, int oy, PImage p) {
    xpos=x;
    ypos=y;
    offsetX = ox;
    offsetY = oy;
    pic=p;
  }
  
  Obstacle(int x, int y, PImage p) {
    xpos=x;
    ypos=y;
    offsetX = 0;
    offsetY = 0;
    pic=p;
  }

  void move() {
    xpos-=3;
  }
  
  int placeX(){
     return xpos+offsetX; 
  }
  
  int placeY(){
     return ypos+offsetY; 
  }
  
  int hbX(){
    return xpos;
  }
  
  int hbY(){
    return ypos;
  }
  
  PImage getPic(){
     return pic; 
  }
}

