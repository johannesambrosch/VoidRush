class Info {

  PImage bg, btnback, btnback2, btnright, btnleft;
  float stretchX, stretchY;
  PFont tiki, verdana;
  int mode; //0 General, 1 Abilities(Q+W), 2 Abilities(E+R), 3 About

  PApplet app;

  Info(PApplet app, float sX, float sY) {
    bg=loadImage("Info.png");
    stretchX=sX;
    stretchY=sY;
    this.app=app;

    btnback = loadImage("btn_back.png");
    btnback2 = loadImage("btn_back_hover.png");
    btnright = loadImage("arrow_right.png");
    btnleft = loadImage("arrow_left.png");
    tiki = loadFont("TikiTropic.vlw");
    verdana = loadFont("Verdana-12.vlw");

    mode = 0;
  }

  PGraphics displayInfo(PGraphics input) {

    input.beginDraw();
    input.image(bg, 0, 0, 320, 180);
    input.noStroke();
    input.fill(0, 150);
    input.rect(5, 5, 310, 170, 5);

    // ÜBERSCHRIFTEN
    input.textFont(tiki);
    input.textAlign(CENTER);
    if (mode==0) input.fill(255);
    else input.fill(150);
    input.text("General", 57, 25);       //5 Pixel Rand + (310 Pixel Zeichenbereich durch 3 durch 2)
    if (mode==1||mode==2) input.fill(255);
    else input.fill(150);
    input.text("Abilities", 160, 25);    //5 Pixel Rand + (310 Pixel Zeichenbereich durch 3 mal 2, dann 310/3/2 abgezogen)
    if (mode==3) input.fill(255);
    else input.fill(150);
    input.text("About", 263, 25);        //5 Pixel Rand + (310 Pixel Zeichenbereich, dann 310/3/2 abgezogen) 
    input.stroke(255);
    input.line(5, 30, 314, 30);

    input.textAlign(LEFT);
    input.textFont(verdana);
    input.fill(255);


    if (mode==0) {
      input.text("In Void Rush, you control Rek'Sai, a character\nfrom League of Legends.\n\nYour goal is to survive the levels by dodging\nobstacles, and to gain experience, which you\ncan use to unlock and improve your abilities.\n\nReach level 10 to use your ultimate ability!", 15, 50);
    } else if (mode==1) {
      input.text("Q - Queen's Wrath / Prey Seeker:\n   destroy small obstacles, usable 3 times\n   if burrowed, fire a single ranged shot instead\n\nW - Burrow / Unburrow:\n   Burrow underground, alters your other basic\n   abilities and allows you to pass below\n   hanging obstacles.", 15, 50);
      input.image(btnright, 280, 83);
    } else if (mode==2) {
      input.text("E - Furious Bite / Tunnel:\n   destroy any obstacle, high cooldown\n   if burrowed, dig a tunnel instead\n\nR - Void Rush:\n   Go back to where you came from because\n   racing the void is more fun anyway\n   ...Kappa", 15, 50);
      input.image(btnleft, 280, 83);
    } else {
      input.text("This game was developed as a semester\nproject at St. Pölten UAS in Austria in 2016.\n\nCoding & game design: Johannes Ambrosch\nCharacter art: Gabriel Körber\n\nLeague of Legends and Rek'Sai are\nproperty of Riot Games Inc.", 15, 50);
    }



    if (backButtonHovered()) input.image(btnback2, 290, 150);
    else input.image(btnback, 290, 150);

    input.endDraw(); 
    return input;
  }

  boolean backButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>150 * stretchY) return true;
    else return false;
  }


  boolean generalHovered() {
    if (mouseX<108 * stretchX&&mouseY<30 * stretchY) return true;
    else return false;
  }
  boolean abilitiesHovered() {
    if (mouseX>=108 * stretchX&&mouseX<=212 * stretchX&&mouseY<30 * stretchY) return true;
    else return false;
  }
  boolean aboutHovered() {
    if (mouseX>212 * stretchX&&mouseY<30 * stretchY) return true;
    else return false;
  }
  boolean abSwitchHovered() {
    if (mouseX>280 * stretchX&&mouseX<310 * stretchX&&mouseY>83 * stretchY&&mouseY<113 * stretchY) return true;
    else return false;
  }

  int click() {
    if (backButtonHovered()) return 0;
    else if (generalHovered()) return 1;
    else if (abilitiesHovered()) return 2;
    else if (abSwitchHovered()) return 3;
    else if (aboutHovered()) return 4;
    else return -1;
  }

  void setTab(int tab) {
    mode=tab;
  }

  void flipTab() {
    if (mode==1)mode=2; 
    else mode=1;
  }
}

