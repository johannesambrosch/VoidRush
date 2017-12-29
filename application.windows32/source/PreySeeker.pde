class PreySeeker {

  int xpos, ypos;
  PImage pic;

  PreySeeker(int x, int y) {
    xpos=x;
    ypos=y;
    pic=loadImage("preyseeker.png");
  }


  void move() {
    xpos+=5;
  }

  PGraphics zeichnen(PGraphics input) {
    input.image(pic, xpos, ypos);
    return input;
  }

  boolean kill() {
    if (xpos>220) return true;
    else return false;
  }

  int getX() {
    return xpos;
  }

  int getY() {
    return ypos;
  }
}

