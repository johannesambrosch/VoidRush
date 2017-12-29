import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import gifAnimation.*; 
import ddf.minim.spi.*; 
import ddf.minim.signals.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.ugens.*; 
import ddf.minim.effects.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Void_Rush extends PApplet {










PGraphics screen;
MainMenu main;
Game game;
Info info;
Settings settings;
float stretchX, stretchY;
boolean music = true, sound = true;
int mode=0;
/*  0 = Main Menu
 1 = Normal Game
 2 = Void Rush
 3 = Settings
 4 = Info
 */

Minim minim;
AudioPlayer bgmusic;
AudioPlayer leave;
AudioPlayer smallClick;

public void setup() {
  //ANZEIGE GENERIEREN
  /*if (displayWidth>=1600&&displayHeight>=900) size(1280, 720);
   else if (displayWidth>=1280&&displayHeight>=720) size(960, 540);
   else if (displayWidth>=800&&displayHeight>=600) size(640, 400);
   else size(320, 180);*/
  size(displayWidth, displayHeight);
  frameRate(40);
  stretchX = width/320;
  stretchY = height/180;
  screen = createGraphics(320, 180);

  minim = new Minim(this);

  //SAVES LADEN
  String[] save = loadStrings("save.log");
  try {
    if (save[0].equals("0"))music = false;
    if (save[1].equals("0"))sound = false;
  }
  catch(Exception e) {
    println(e);
  }


  //HINTERGRUNDMUSIK
  if (music) bgmusic = minim.loadFile("EnergyDrink.mp3");
  if (music) bgmusic.setGain(-10);
  if (music) bgmusic.loop();



  //Hauptmen\u00fc erstellen
  main = new MainMenu(this, stretchX, stretchY);
}

public void draw() {
  background(0);
  smooth(0);
  if (mode==0) {
    image(main.displayMainMenu(screen), 0, 0, width, height);
  } else if (mode==1) {
    image(game.displayGame(screen), 0, 0, width, height);
  } else if (mode==2) {
  } else if (mode==3) {
    image(settings.displaySettings(screen, music, sound), 0, 0, width, height);
  } else if (mode==4) {
    image(info.displayInfo(screen), 0, 0, width, height);
  }
}


//          MOUSE PRESSED
public void mousePressed() {
  if (mouseButton==LEFT) {



    if (mode==0) {          // Mode = Main Menu
      int clicked = main.click();   
      if (clicked==0) {    //-1 = nothing, 0 = Start Game, 1 = Exit, 2 = Settings, 3 = Info, 4 = Quit, 5 = Not Quit
        mode=1;
        game = new Game(this, stretchX, stretchY, sound);
      } else if (clicked==1) {
        main.showQD();
      } else if (clicked==2) {
        mode = 3;
        settings = new Settings(this, stretchX, stretchY);
      } else if (clicked==3) {
        mode = 4;
        info = new Info(this, stretchX, stretchY);
      } else if (clicked==4) {
        exit();
      } else if (clicked==5) {
        main.hideQD();
      }
    } else if (mode==1) {
      int result = game.click();
      if (result==0) game.togglePD();
      else if (result==1) game.useQ();
      else if (result==2) game.useW();
      else if (result==3) game.useE();
      else if (result==4) game.useR();
      else if (result==5) game.levelQ();
      else if (result==6) game.levelW();
      else if (result==7) game.levelE();
      else if (result==8) game.levelR();
      else if (result==9) game.togglePD();
      else if (result==10) {
        game.togglePD();
        mode=0;
      }
    } else if (mode==2) {
    } else if (mode==3) { //Mode = Settings
      int clicked = settings.click(); // -1 = nothing, 0 = back, 1 = music, 2 = sound
      if (clicked==0) {
        main = new MainMenu(this, stretchX, stretchY);
        mode=0;
        if (sound) leave = minim.loadFile("closemenu.mp3");
        if (sound) leave.play();
      } else if (clicked==1) {
        smallClick = minim.loadFile("button.mp3");
        if (sound) smallClick.play();

        music = !music;

        //ABSPEICHERN!
        try {
          String[] save = loadStrings("save.log");
          if (music) save[0] = "1";
          else save[0] = "0";
          saveStrings("data/save.log", save);
        }
        catch(Exception e)
        {
          resetSave();
        }


        if (music) {
          bgmusic = minim.loadFile("EnergyDrink.mp3");
          bgmusic.setGain(-10);
          bgmusic.loop();
        } else {
          bgmusic.pause();
        }
      } else if (clicked==2) {
        if (!sound) smallClick = minim.loadFile("button.mp3");
        if (!sound) smallClick.play();
        sound = !sound;

        //ABSPEICHERN
        try {
          String[] save = loadStrings("save.log");
          if (sound) save[1] = "1";
          else save[1] = "0";
          saveStrings("data/save.log", save);
        }
        catch (Exception e)
        {
          resetSave();
        }
      }
    } else if (mode==4) { //Mode = Info
      int clicked = info.click(); // -1 = nothing, 0 = back, 1 = General, 2 = Abilities(Q+W), 3 = Abilities(E+R), 4 = About
      if (clicked==0) {
        main = new MainMenu(this, stretchX, stretchY);
        mode=0;
        if (sound) leave = minim.loadFile("closemenu.mp3");
        if (sound) leave.play();
      } else if (clicked==1) {
        info.setTab(0);
        if (sound) smallClick = minim.loadFile("button.mp3");
        if (sound) smallClick.play();
      } else if (clicked==2) {
        info.setTab(1);
        if (sound) smallClick = minim.loadFile("button.mp3");
        if (sound) smallClick.play();
      } else if (clicked==3) {
        info.flipTab();
        if (sound) smallClick = minim.loadFile("button.mp3");
        if (sound) smallClick.play();
      } else if (clicked==4) {
        info.setTab(3);
        if (sound) smallClick = minim.loadFile("button.mp3");
        if (sound) smallClick.play();
      }
    }
  }
}

//          KEY PRESSED
public void keyPressed() {
  if (mode==0) {
    if (key==27) {
      if (!main.getQD()) {
        main.showQD();
        key=0;
      } else {
        main.hideQD(); 
        key=0;
      }
    }
    if (key==ENTER) {
      if (main.getQD()) {
        exit();
      } else {
        mode=1;
        game = new Game(this, stretchX, stretchY, sound);
      }
    }
  }

  if (mode == 1) {
    if (key==27) { 
      game.togglePD();
      key=0;
    } else if (game.paused && key==ENTER) {
      if (sound) leave = minim.loadFile("closemenu.mp3");
      if (sound) leave.play();
      mode=0;
    } else {
      game.press();
    }
  }

  if (mode==3) {
    if (key==27) {
      key=0;
      main = new MainMenu(this, stretchX, stretchY);
      mode=0;
      if (sound) leave = minim.loadFile("closemenu.mp3");
      if (sound) leave.play();
    }
  }

  if (mode==4) {
    if (key==27) {
      key=0;
      main = new MainMenu(this, stretchX, stretchY);
      mode=0;
      if (sound) leave = minim.loadFile("closemenu.mp3");
      if (sound) leave.play();
    }
  }
}

//          KEY RELEASED
public void keyReleased() {
  if (mode == 1) {
    game.release();
  }
}


public boolean sketchFullScreen() {
  return true;
}

public void resetSave() {
  String[] save = new String[3];
  save[0] = "1";
  save[1] = "1";
  save[2] = "0";
  saveStrings("data/save.log", save);
}

class Game {

  PImage bg, rek, hpbar, xpbar, icon, hpred;
  PImage rek1, rek2, rek_q, rek_w1, rek_w2, rek_e1, rek_e2, rek_r;

  PImage q1, q2, w1, w2, e1, e2, r1;
  PImage q1g, q2g, w1g, w2g, e1g, e2g, r1g;
  PImage q1a, q2a, w1a, w2a, e1a, e2a, r1a;

  PImage pd, pds, pdq, pds2, pdq2, btnquit, btnquit2;

  Grid grid;

  PFont tiki, verdana;

  int q1CD, q2CD, wCD, e1CD, e2CD, q1MaxCD, q2MaxCD, wMaxCD, e1MaxCD, e2MaxCD;

  int[] sandX, sandY;
  float stretchX, stretchY;

  //LEVELUP MECHANICS
  int qlvl, wlvl, elvl, rlvl, lvl;
  boolean lvlup;
  PImage imglvl, imglvl2;
  boolean shift;

  int rekY;
  int health;
  int hpdrop;
  int tickSec;
  int pausetimer;
  int[] levels;
  int exp;

  boolean moveup;
  boolean movedown;
  boolean burrowed;
  boolean sound;

  boolean gameover, win, paused, rush;

  //FOR ABILITIES EXTRA
  int qcount, qtimeout;
  int casttime;
  boolean casting;
  int currentAbility;
  /* CURRENT ABILITY
   
   0 = None
   1 = Q Cast Activated (Q1)
   2 = Burrowing (W1)
   3 = Furious Bite (E1)
   4 = Prey Seeker (Q2)
   5 = Unburrowing (W2)
   6 = Tunneling (E2)
   
   7=Void Rush
   */

  PApplet app;


  Minim minim;
  AudioPlayer startSound;
  AudioPlayer abilitySounds;
  AudioPlayer abilitySounds2;
  AudioPlayer rekSound1;
  AudioPlayer rekSound2;
  AudioPlayer menusound;



  Game(PApplet app, float sX, float sY, boolean sound) {
    bg=loadImage("MainMenu.png");
    rek=loadImage("rek/rek1.png");
    hpbar = loadImage("healthbar.png");
    hpred = loadImage("hpdrop.png");
    xpbar = loadImage("xpbar.png");
    icon = loadImage("CharIcon.png");

    q1=loadImage("q1.png");
    q2=loadImage("q2.png");
    w1=loadImage("w1.png");
    w2=loadImage("w2.png");
    e1=loadImage("e1.png");
    e2=loadImage("e2.png");
    r1=loadImage("r1.png");

    q1g=loadImage("q1g.png");
    q2g=loadImage("q2g.png");
    w1g=loadImage("w1g.png");
    w2g=loadImage("w2g.png");
    e1g=loadImage("e1g.png");
    e2g=loadImage("e2g.png");
    r1g=loadImage("r1g.png");

    q1a=loadImage("q1_active.png");
    q2a=loadImage("q2_active.png");
    w1a=loadImage("w1_active.png");
    w2a=loadImage("w2_active.png");
    e1a=loadImage("e1_active.png");
    e2a=loadImage("e2_active.png");
    r1a=loadImage("r1_active.png");

    rek1=loadImage("rek/rek1.png");
    rek2=loadImage("rek/rek2.png");
    rek_q=loadImage("rek/rek_q1.png");
    rek_w1=loadImage("rek/rek_w1.png");
    rek_w2=loadImage("rek/rek_w2.png");
    rek_e1=loadImage("rek/rek_e1.png");
    rek_e2=loadImage("rek/rek_e2.png");
    rek_r=loadImage("rek/rek_r.png");


    pd=loadImage("quitmain.png");
    pds=loadImage("qm_stay.png");
    pdq=loadImage("qm_quit.png");
    pds2=loadImage("qm_stay_hover.png");
    pdq2=loadImage("qm_quit_hover.png");
    btnquit=loadImage("btn_exit.png");
    btnquit2=loadImage("btn_exit_hover.png");

    tiki = loadFont("TikiTropic.vlw");
    verdana = loadFont("Verdana-12.vlw");

    imglvl = loadImage("lvl.png");
    imglvl2 = loadImage("lvl_hover.png");

    qlvl=0;
    wlvl=0;
    elvl=0;
    rlvl=0;
    lvl=0;
    lvlup=false;

    this.sound=sound;

    shift=false;

    q1CD = 0;
    q2CD = 0;
    wCD = 0;
    e1CD=0;
    e2CD=0;

    health = 100;
    hpdrop = 100;
    tickSec = millis()+1000;
    levels = new int[12];
    levels[0]=0;
    levels[1]=200;
    levels[2]=450;
    levels[3]=750;
    levels[4]=1100;
    levels[5]=1500;
    levels[6]=1950;
    levels[7]=2450;
    levels[8]=3000;
    levels[9]=3600;
    levels[10]=4250;
    levels[11]=9999;
    exp=0;

    gameover=false;
    win=false;
    paused=false;
    rush=false;

    //Set Abilities
    burrowed=false;
    qcount=0;
    currentAbility=0;
    casttime=0;
    qtimeout=0;

    //Apply Stretch & Settings
    stretchX=sX;
    stretchY=sY;
    this.app=app;

    minim = new Minim(this.app);

    if (sound) startSound = minim.loadFile("clicksound2.mp3");
    if (sound) startSound.play();

    //Generate Sand (Legacy?)
    sandX=new int[20];
    sandY=new int[20];
    for (int i=0; i<20; i++) {
      sandX[i]=PApplet.parseInt(random(320));
      sandY[i] = PApplet.parseInt(random(120))+60;
    }

    //          SET REKSAI POSITION
    rekY = 90;

    moveup = false;
    movedown = false;

    //    GENERATE GRID
    grid = new Grid(app, sound);
  }

  public PGraphics displayGame(PGraphics input) {

    input.beginDraw();
    PImage bgsand = bg.get(); // frischen BG holen, Pixels verdunkeln
    int sand= color(80, 80, 0);
    for (int i=0; i<20; i++) {
      bgsand.set(sandX[i], sandY[i], sand);
      if (!paused&&!win) sandX[i]-=3;
      if (sandX[i]<0) {      // Wenn Sand links rausrutscht, neu generieren
        sandX[i] = PApplet.parseInt(random(100))+320;
        sandY[i] = PApplet.parseInt(random(120))+60;
      }
    }
    //if(burrowed) input.tint(80);
    input.image(bgsand, 0, 0, 320, 180);
    input.tint(255);

    //          MOVE REKSAI                    NOTE: IMPLEMENT SLOW
    if (!paused&&!win) {
      if (grid.isSlowed()) {
        if (moveup&&!casting) rekY-=1;
        if (movedown&&!casting) rekY+=1;
      } else {
        if (moveup&&!casting) rekY-=2;
        if (movedown&&!casting) rekY+=2;
      }
      if (rekY<60)rekY=60;
      if (rekY>120)rekY=120;
    }


    //          UPDATE HEALTH
    if (!gameover&&!paused&&!win) {
      if (tickSec < millis()) {
        if (burrowed) health+=10;
        else health+=5;
        exp+=10;
        if (lvl<10) {
          if (exp>levels[lvl+1]) {
            levelUp();
          }
        }
        if (exp>4250)exp=4250;
        tickSec+=1000;
        if (health>100)health=100;
      }
    }

    if (health<0) {
      gameover=true;
      grid.putRek(rekY, rek);
      health=100;
    }

    //          DISPLAY AND UPDATE GRID
    if (!paused&&!win) grid.moveGrid(lvl, qlvl, wlvl, elvl, q1CD, q2CD, wCD, e1CD, e2CD, burrowed, casting);
    input = grid.displayGrid(input, rek, rekY, rush);


    //          GAME OVER SCREEN
    if (gameover) {
      input.textAlign(CENTER);
      input.textFont(tiki);
      input.fill(0);
      input.text("GAME OVER!", 160, 80);
      input.textFont(verdana);
      input.text("Press 'ESC' to get back to main Menu", 160, 120);
      input.fill(255);
    }

    if (win) {
      input.textAlign(CENTER);
      input.textFont(tiki);
      input.fill(120, 0, 180);
      input.text("YOU WON!", 160, 80);
      input.textFont(verdana);
      input.text("Press 'ESC' to get back to main Menu", 160, 120);
      input.fill(255);
    }

    input.textFont(verdana);


    if (!gameover&&!win&&!paused) {
      //          RECTANGLES BEHIND ICONS
      input.fill(0);
      input.noStroke();
      input.rect(80, 0, 160, 20, 4);
      input.rect(220, 155, 100, 25);

      //          HEALTHBAR

      if (hpdrop>health) hpdrop-=2;
      if (hpdrop<health) hpdrop=health;
      int maphpdrop = PApplet.parseInt(map(hpdrop, 0, 100, 0, 156));
      input.image(hpred, 82, 2, maphpdrop, 16);
      int maphp = PApplet.parseInt(map(health, 0, 100, 0, 156));
      input.image(hpbar, 82, 2, maphp, 16);  // from 80 to 160, only -2 on each side for border, same for height 0 to 20


      //          EXP BAR & Character Icon + Rect
      input.image(icon, 0, 0, 40, 40);
      input.rect(40, 0, 10, 40);
      int mapxp = PApplet.parseInt(map(exp, levels[lvl], levels[lvl+1], 0, 38));
      if (lvl==10)mapxp=38;
      input.image(xpbar, 41, 1+(38-mapxp), 8, mapxp);  // from 0 to 40

      //          CALCULATE LEVEL MECHANIC
      if ((qlvl+wlvl+elvl+rlvl) < lvl) {
        lvlup = true;
        input.fill(210, 180, 45);
        //        Display Ability Levelup Icons
        if (qlvl<3) {
          input.rect(220, 130, 25, 25);
          if (lqHovered()) {
            input.image(imglvl2, 221, 131, 23, 23);
          } else
          {
            input.image(imglvl, 221, 131, 23, 23);
          }
        }
        if (wlvl<3) {
          input.rect(245, 130, 25, 25);
          if (lwHovered()) {
            input.image(imglvl2, 246, 131, 23, 23);
          } else {
            input.image(imglvl, 246, 131, 23, 23);
          }
        }
        if (elvl<3) {
          input.rect(270, 130, 25, 25);
          if (leHovered()) {
            input.image(imglvl2, 271, 131, 23, 23);
          } else {
            input.image(imglvl, 271, 131, 23, 23);
          }
        }
        if (lvl==10)
        {
          input.rect(295, 130, 25, 25);
          if (lrHovered()) {
            input.image(imglvl2, 296, 131, 23, 23);
          } else {
            input.image(imglvl, 296, 131, 23, 23);
          }
        }
      } else {
        lvlup = false;
      }

      //          ABILITY ICONS (TINT blue, after draw tint 255)
      if (!burrowed) {
        if (qlvl>0) {
          if (millis()>=q1CD) {
            if (qHovered() || qcount>0 ||currentAbility == 1) input.image(q1a, 221, 156, 23, 23);
            else input.image(q1, 221, 156, 23, 23);
          } else {
            input.tint(40, 40, 255);
            input.image(q1g, 221, 156, 23, 23);
            input.tint(255);
            input.fill(255);
            input.textAlign(CENTER);
            float cooldown = PApplet.parseFloat((q1CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 233, 172); //TEXT ALWAYS AT 172 HEIGHT
            else input.text(PApplet.parseInt(cooldown), 233, 172);
          }
        } else input.image(q1g, 221, 156, 23, 23);
        if (wlvl>0) {
          if (millis()>=wCD) {
            if (wHovered() || currentAbility == 2) input.image(w1a, 246, 156, 23, 23);
            else input.image(w1, 246, 156, 23, 23);
          } else {
            input.tint(40, 40, 255);
            input.image(w1g, 246, 156, 23, 23);
            input.tint(255);
            input.fill(255);
            input.textAlign(CENTER);
            float cooldown = PApplet.parseFloat((wCD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 258, 172);
            else input.text(PApplet.parseInt(cooldown), 258, 172);
          }
        } else input.image(w1g, 246, 156, 23, 23);
        if (elvl>0) {
          if (millis()>=e1CD) {
            if (eHovered() || currentAbility == 3) input.image(e1a, 271, 156, 23, 23);
            else input.image(e1, 271, 156, 23, 23);
          } else {
            input.tint(40, 40, 255);
            input.image(e1g, 271, 156, 23, 23);
            input.tint(255);
            input.fill(255);
            input.textAlign(CENTER);
            float cooldown = PApplet.parseFloat((e1CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 283, 172);
            else input.text(PApplet.parseInt(cooldown), 283, 172);
          }
        } else input.image(e1g, 271, 156, 23, 23);
      }

      //         ABILITY ICONS WHEN BURROWED
      else {
        if (qlvl>0) {
          if (millis()>=q2CD) {
            if (qHovered() || currentAbility == 4) input.image(q2a, 221, 156, 23, 23);
            else input.image(q2, 221, 156, 23, 23);
          } else {
            input.tint(40, 40, 255);
            input.image(q2g, 221, 156, 23, 23);
            input.tint(255);
            input.fill(255);
            input.textAlign(CENTER);
            float cooldown = PApplet.parseFloat((q2CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 233, 172);
            else input.text(PApplet.parseInt(cooldown), 233, 172);
          }
        } else input.image(q2g, 221, 156, 23, 23);
        if (wlvl>0) {
          if (millis()>=wCD) {
            if (wHovered() || currentAbility == 5) input.image(w2a, 246, 156, 23, 23);
            else input.image(w2, 246, 156, 23, 23);
          } else {
            input.tint(40, 40, 255);
            input.image(w2g, 246, 156, 23, 23);
            input.tint(255);
            input.fill(255);
            input.textAlign(CENTER);
            float cooldown = PApplet.parseFloat((wCD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 258, 172);
            else input.text(PApplet.parseInt(cooldown), 258, 172);
          }
        } else input.image(w2g, 246, 156, 23, 23);
        if (elvl>0) {
          if (millis()>=e2CD) {
            if (eHovered() || currentAbility == 6) input.image(e2a, 271, 156, 23, 23);
            else input.image(e2, 271, 156, 23, 23);
          } else {
            input.tint(40, 40, 255);
            input.image(e2g, 271, 156, 23, 23);
            input.tint(255);
            input.fill(255);
            input.textAlign(CENTER);
            float cooldown = PApplet.parseFloat((e2CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 283, 172);
            else input.text(PApplet.parseInt(cooldown), 283, 172);
          }
        } else input.image(e2g, 271, 156, 23, 23);
      }

      //          QUIT BUTTON
      if (pauseButtonHovered()) input.image(btnquit2, 290, 0);
      else input.image(btnquit, 290, 0);


      //           ULTI ICON
      if (rlvl>0) {
        if (rHovered()) input.image(r1a, 296, 156, 23, 23);
        else input.image(r1, 296, 156, 23, 23);
      } else input.image(r1g, 296, 156, 23, 23);
    }

    if (paused) {          // PAUSE DIALOG
      input.fill(125, 125, 125, 180);
      input.noStroke();
      input.rect(0, 0, 320, 180);
      input.image(pd, 40, 30);
      if (stayHovered()) input.image(pds2, 160-78-20, 110);
      else input.image(pds, 160-78-20, 110);
      if (quitHovered()) input.image(pdq2, 160+20, 110);
      else input.image(pdq, 160+20, 110);
    }

    //          END OF DRAW
    input.endDraw(); 


    //          NON-DRAW CALCULATIONS


    if (!paused) {

      //          Check if Q1 is timed out, goes on cooldown
      if (qtimeout <= millis() && qcount > 0) {
        q1Finish();
        q1CD = millis() + q1MaxCD;
      }

      //          Check if Cast time is over, put all abilities on cooldown
      if (casttime<=millis()) {
        casting = false;
        if (currentAbility==1&&qcount==0) q1CD = millis() + q1MaxCD;
        else if (currentAbility == 2) {
          wCD = millis() + wMaxCD;
          burrowed= !burrowed;
        } else if (currentAbility == 3) e1CD = millis() + e1MaxCD;
        else if (currentAbility == 4) {
          q2CD = millis() + q2MaxCD;
          grid.createPS(rekY);
        } else if (currentAbility == 5) {
          wCD = millis() + wMaxCD;
          burrowed = !burrowed;
        } else if (currentAbility == 6) {
          e2CD = millis() + e2MaxCD;
          grid.createTunnel(rekY, false);
        } else if (currentAbility==7) {
          rush=true;
        }

        currentAbility=0;
        if (burrowed)rek=rek2;
        else rek=rek1;
      }
    }

    //          COLLISION DETECTION
    if (!gameover&&!paused&&!win) {
      int damage = grid.collision(rekY, currentAbility, burrowed);
      if (damage>0) health-=damage;
      if (damage == 70) {
        burrowed=!burrowed;
        casttime=millis();
      }
      if (damage<0) exp-=damage;
    }

    //          END OF VOID
    return input;
  }

  //          KEY PRESSED
  public void press() {
    if (keyCode==SHIFT) {
      shift=true;
      key=0;
    }
    if (keyCode==UP) {
      moveup=true;
    } else if (keyCode==DOWN) {
      movedown = true;
    }

    if (!gameover&&!paused&&!win) {
      if (!shift) {
        if (!casting) {
          if (key=='Q'||key=='q') useQ();
          if (key=='W'||key=='w') useW();
          if (key=='E'||key=='e') useE();
          if (key=='R'||key=='r') useR();
        } else {
          if (sound) {
            abilitySounds = minim.loadFile("sound/ability_unavailable.mp3");
            if (key=='W'||key=='w') abilitySounds.play();
            if (key=='Q'||key=='q') abilitySounds.play();
            if (key=='E'||key=='e') abilitySounds.play();
            if (key=='R'||key=='r') abilitySounds.play();
          }
        }
      } else if (shift&&lvlup) { 
        if ((key=='Q'||key=='q')&& qlvl<3) levelQ();
        if ((key=='W'||key=='w')&& wlvl<3) levelW();
        if ((key=='E'||key=='e')&& elvl<3) levelE();
        if ((key=='R'||key=='r')&& rlvl==0 && lvl==10) levelR();
      } else {

        if ((key=='Q'||key=='q'||key=='W'||key=='w'||key=='E'||key=='e'||key=='R'||key=='r')&&sound) {
          abilitySounds = minim.loadFile("sound/ability_unavailable.mp3");
          abilitySounds.play();
        }
      }



      //                                                                                            DEFINITELY REMOVE!!! Debug ONLY!
      if (key=='L'||key=='l') levelUp();
    }
  }

  //          KEY RELEASED
  public void release() {
    if (keyCode==SHIFT) {
      shift=false;
      key=0;
    }
    if (keyCode==UP) {
      moveup=false;
    } else if (keyCode==DOWN) {
      movedown = false;
    }
  }

  public int click() {           // -1 = nothing, 0 = Pause Dialog 1234 = qwer, 5678 = lvl qwer, 9=Stay, 10=quit
    if (!gameover&&!paused&&!win) {
      if (pauseButtonHovered()) return 0;
      else if (qHovered()) return 1;
      else if (wHovered()) return 2;
      else if (eHovered()) return 3;
      else if (rHovered()) return 4;
      else if (lvlup==true&&lvl<10&&lqHovered()) return 5;
      else if (lvlup==true&&lvl<10&&lwHovered()) return 6;
      else if (lvlup==true&&lvl<10&&leHovered()) return 7;
      else if (lvlup==true&&lvl==10&&lrHovered()) return 8;
      else return -1;
    } else if (paused&&stayHovered()) return 9;
    else if (paused&&quitHovered()) return 10;
    else return -1;
  }

  //      Ability Hover
  public boolean qHovered() {
    if (mouseX>220 * stretchX&&mouseY>155 * stretchY&&mouseX<245 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }
  public boolean wHovered() {
    if (mouseX>245 * stretchX&&mouseY>155 * stretchY&&mouseX<270 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }
  public boolean eHovered() {
    if (mouseX>270 * stretchX&&mouseY>155 * stretchY&&mouseX<295 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }
  public boolean rHovered() {
    if (mouseX>295 * stretchX&&mouseY>155 * stretchY&&mouseX<320 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }

  //      LevelUp Hover
  public boolean lqHovered() {
    if (mouseX>220 * stretchX&&mouseY>130 * stretchY&&mouseX<245 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  public boolean lwHovered() {
    if (mouseX>245 * stretchX&&mouseY>130 * stretchY&&mouseX<270 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  public boolean leHovered() {
    if (mouseX>270 * stretchX&&mouseY>130 * stretchY&&mouseX<295 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  public boolean lrHovered() {
    if (mouseX>295 * stretchX&&mouseY>130 * stretchY&&mouseX<320 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  public boolean pauseButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY<30 *stretchY) return true;
    else return false;
  }
  public boolean stayHovered() {
    if (mouseX>62 * stretchX&&mouseX<140 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && paused==true) return true;
    else return false;
  }
  public boolean quitHovered() {
    if (mouseX>180 * stretchX&&mouseX<258 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && paused==true) return true;
    else return false;
  }

  //          PAUSE DIALOG
  public void togglePD() {
    if (sound) {
      if (paused) {
        menusound = minim.loadFile("closemenu.mp3");
        tickSec=millis()+1000;
        int diff=millis()-pausetimer;
        q1CD+=diff;
        q2CD+=diff;
        wCD+=diff;
        e1CD+=diff;
        e2CD+=diff;
        casttime+=diff;
      } else {
        menusound = minim.loadFile("openmenu.mp3");
        pausetimer=millis();
      }
      menusound.play();
    }
    paused=!paused;
  }



  //          ABILITY USE, does NOT put on Cooldown, sets Cast Timer!
  public void useQ() {
    if (!burrowed && q1CD<= millis() && qlvl>0) {
      qcount++;
      casting = true;
      casttime = millis() + 500;
      currentAbility=1;
      if (sound) {
        abilitySounds = minim.loadFile("sound/rek_q1_cast.mp3");
        abilitySounds.play();
      }
      rek=rek_q;
      if (qcount==1) {
        qtimeout=millis()+6000;
        if (sound) {
          abilitySounds2 = minim.loadFile("sound/rek_q1_bg.mp3");
          abilitySounds2.play();
        }
      }
      if (qcount==3) {
        q1Finish();
        if (sound) {
          abilitySounds2.pause();
        }
      }
    } else if (burrowed && q2CD<=millis() && qlvl>0) {
      casting = true;
      casttime = millis() + 50;
      currentAbility=4;
      if (sound) {
        abilitySounds = minim.loadFile("sound/rek_q2.mp3");
        abilitySounds.play();
      }
    } else {
      if (sound) {
        abilitySounds = minim.loadFile("sound/ability_unavailable.mp3");
        abilitySounds.play();
      }
    }
  }

  public void q1Finish() {
    qcount=0;
  }

  public void useW() {
    if (wCD <= millis() && wlvl>0) {
      casting = true;
      casttime = millis() + 300;
      if (!burrowed) {
        currentAbility=2;
        rek=rek_w1;
        if (sound) {
          abilitySounds = minim.loadFile("sound/rek_w1.mp3");
          abilitySounds.play();
        }
      } else {
        currentAbility = 5;
        rek=rek_w2;
        if (sound) {
          abilitySounds = minim.loadFile("sound/rek_w2.mp3");
          abilitySounds.play();
        }
      }
    } else {
      if (sound) {
        abilitySounds = minim.loadFile("sound/ability_unavailable.mp3");
        abilitySounds.play();
      }
    }
  }

  public void useE() {
    if (!burrowed && e1CD <= millis() && elvl>0) {
      casting = true;
      casttime = millis() + 400;
      currentAbility=3;
      rek=rek_e1;
      if (sound) {
        abilitySounds = minim.loadFile("sound/rek_e1.mp3");
        abilitySounds.play();
      }
    } else if (burrowed && e2CD <= millis() && elvl>0) {
      casting = true;
      casttime = millis() + 1500;
      currentAbility=6;
      grid.createTunnel(rekY, true);
      rek=rek_e2;
      if (sound) {
        abilitySounds = minim.loadFile("sound/rek_e2.mp3");
        abilitySounds.play();
      }
    } else {
      if (sound) {
        abilitySounds = minim.loadFile("sound/ability_unavailable.mp3");
        abilitySounds.play();
      }
    }
  }

  public void useR() {
    if (rlvl==1) {
      if (sound) {
        abilitySounds = minim.loadFile("sound/rek_r.mp3");
        abilitySounds.play();
      }
      casting=true;
      casttime=millis()+1500;
      burrowed=true;
      currentAbility=7;
      win=true;
      rek=rek_r;
    } else
    {
      if (sound) {
        abilitySounds = minim.loadFile("sound/ability_unavailable.mp3");
        abilitySounds.play();
      }
    }
  }

  //          ABILITY LEVEL UP
  public void levelQ() {
    qlvl++;
    if (sound) {
      abilitySounds = minim.loadFile("sound/ability_levelup.mp3");
      abilitySounds.play();
    }
    if (qlvl==1) {
      q1MaxCD = 7000;
      q2MaxCD = 10000;
    } else if (qlvl==2) {
      q1MaxCD = 5000;
      q2MaxCD = 8000;
    } else {
      q1MaxCD = 3000;
      q2MaxCD = 6000;
    }
  }

  public void levelW() {
    wlvl++;
    if (sound) {
      abilitySounds = minim.loadFile("sound/ability_levelup.mp3");
      abilitySounds.play();
    }
    if (wlvl==1) {
      wMaxCD = 8000;
    } else if (wlvl==2) {
      wMaxCD = 5000;
    } else {
      wMaxCD = 2000;
    }
  }

  public void levelE() {
    elvl++;
    if (sound) {
      abilitySounds = minim.loadFile("sound/ability_levelup.mp3");
      abilitySounds.play();
    }
    if (elvl==1) {
      e1MaxCD = 20000;
      e2MaxCD = 16000;
    } else if (elvl==2) {
      e1MaxCD = 15000;
      e2MaxCD = 12000;
    } else {
      e1MaxCD = 10000;
      e2MaxCD = 8000;
    }
  }

  public void levelR() {

    if (sound) {
      abilitySounds = minim.loadFile("sound/ability_levelup.mp3");
      abilitySounds.play();
    }
    rlvl++;
  }

  public void levelUp() {
    lvl++;
    if (sound) {
      abilitySounds = minim.loadFile("sound/level_up.mp3");
      abilitySounds.play();
    }
  }
}

class Grid {

  ArrayList<Obstacle> obst = new ArrayList<Obstacle>();
  int counter;
  boolean active;
  int rekOff = -10;
  int spawnchance = 2;

  PreySeeker ps;
  boolean psactive;

  int slowtimer;
  int rushdist;

  PApplet app;

  PImage box, barrel, shroom, tree, rocks, boulder, stones;
  PImage box2, barrel2, cloud, tree2, rocks2, boulder2, cloud2;

  PImage tunnel1, tunnel2;

  boolean sound;

  boolean gameover;
  PImage rek_over;

  Minim minim;
  AudioPlayer explosion;
  AudioPlayer prey;

  Grid(PApplet app, boolean sound) {
    counter=0;
    active=true;

    this.app=app;

    minim = new Minim(app);

    box = loadImage("obst/box.png");
    barrel = loadImage("obst/barrel.png");
    shroom = loadImage("obst/shroom.png");
    tree = loadImage("obst/palm_fallen.png");
    rocks = loadImage("obst/rocks.png");
    boulder = loadImage("obst/boulder.png");
    stones = loadImage("obst/stones.png");
    box2 = loadImage("obst/shattered/box.png");
    barrel2 = loadImage("obst/shattered/barrel.png");
    tree2 = loadImage("obst/shattered/palm_fallen.png");
    rocks2 = loadImage("obst/shattered/rocks.png");
    boulder2 = loadImage("obst/shattered/boulder.png");
    cloud = loadImage("obst/shattered/cloud.png");
    cloud2 = loadImage("obst/shattered/cloud2.png");
    tunnel1=loadImage("obst/tunnel1.png");
    tunnel2=loadImage("obst/tunnel2.png");
    spawnchance=10;
    slowtimer=0;
    this.sound=sound;
    psactive = false;
    gameover=false;
    rushdist=0;
  }

  public void moveGrid(int level, int qlvl, int wlvl, int elvl, int q1CD, int q2CD, int wCD, int e1CD, int e2CD, boolean burrowed, boolean casting) {
    for (Obstacle o : obst) {
      o.move();
    }
    counter++;
    if (counter==10) {
      counter=0;
      update(level, qlvl, wlvl, elvl, q1CD, q2CD, wCD, e1CD, e2CD, burrowed, casting);
    }

    for (int i=0; i<obst.size (); i++) {
      if (obst.get(i).placeX() < -200) obst.remove(i);
    }
    if (psactive) {
      if (preyFound(ps.getX(), ps.getY())) {
        psactive = false;
      }
    }
  }

  public void update(int level, int qlvl, int wlvl, int elvl, int q1CD, int q2CD, int wCD, int e1CD, int e2CD, boolean burrowed, boolean casting) {
    if (active && !casting) {
      float rng = random(100);
      if (rng<spawnchance) {

        PImage placeobst;

        //30% Chance for each 2 Row and 3 Row of Small Obstacles, 10% each for Rocks, Boulder, Palm tree and Stones
        rng = random(100);

        if (rng>90) {
          if (!burrowed || millis()>=wCD-1500) {
            obst.add(new Obstacle(351, 60, 0, 0, stones));
            spawnchance=2;
          } else rng=10;
        } else if (rng>80) {
          if ((millis()>=wCD - 1500 || burrowed) && (wlvl>=1 || level>=3)) {
            obst.add(new Obstacle(351, 60, -30, -40, tree));
            spawnchance=0;
          } else rng=10;
        } else if (rng>70) {
          if ((millis()>=e2CD - 1500 && (burrowed||millis()>=wCD-1500)) && ((elvl>=1&&wlvl>=1) || level>=3)) {
            obst.add(new Obstacle(351, 60, -5, 0, boulder));
            spawnchance=-4;
          } else rng=10;
        } else if (rng>60) {
          if (((millis()>=e1CD - 1500 && (!burrowed || millis()>=wCD-1500)) || (millis()>=e2CD - 1500 && (burrowed || millis()>=wCD-1500) && wlvl>=1)) && (elvl>=1 || level>=3)) {
            obst.add(new Obstacle(351, 60, 0, 0, rocks));
            spawnchance=0;
          } else rng=10;
        } else if (rng>30) {
          if (millis()> q1CD - 1500 && (qlvl>=1 || level>=3)) {
            int rndobst = PApplet.parseInt(random(6));
            if (rndobst<4) placeobst = box;
            else if (rndobst==4) placeobst=barrel;
            else placeobst=shroom;
            obst.add(new Obstacle(351, 60, placeobst));
            obst.add(new Obstacle(351, 90, placeobst));
            obst.add(new Obstacle(351, 120, placeobst));
            spawnchance=2;
          } else rng=10;
        } else {    //      SMALL 2 per row Obstacles
          int rndobst = PApplet.parseInt(random(6));
          if (rndobst<4) placeobst = box;
          else if (rndobst==4) placeobst=barrel;
          else placeobst=shroom;
          int emptypos = PApplet.parseInt(random(3));
          if (emptypos!=0) obst.add(new Obstacle(351, 60, placeobst));
          if (emptypos!=1) obst.add(new Obstacle(351, 90, placeobst));
          if (emptypos!=2) obst.add(new Obstacle(351, 120, placeobst));
          spawnchance=2;
        }
      } else if (spawnchance==0) spawnchance = 2;
      else if (spawnchance<0) spawnchance++;
      else spawnchance*=2;
    }
  }

  public PGraphics displayGrid(PGraphics input, PImage rek, int rekY, boolean rush) {
    for (Obstacle o : obst) {
      if (o.placeY() <= rekY && (o.getPic() == box || o.getPic() == barrel || o.getPic() == shroom || o.getPic()==stones || o.getPic()==box2 || o.getPic()==barrel2 || o.getPic()==tree2 || o.getPic()==rocks2)) input.image(o.getPic(), o.placeX(), o.placeY());
    }
    if (!gameover) input.image(rek, rushdist, rekY+rekOff);
    if(rush) rushdist+=10;
    else {
      for (Obstacle o : obst) {
        if (o.getPic () == rek_over) input.image(o.getPic(), o.placeX(), o.placeY());
      }
    }

    for (Obstacle o : obst) {
      PImage pic = o.getPic();
      if (o.placeY() > rekY &&!(pic == tree || pic == rocks || pic == boulder || pic==cloud || pic==cloud2 || pic==boulder2 || pic==tunnel1 || pic==tunnel2)) input.image(o.getPic(), o.placeX(), o.placeY());
    }
    if (psactive) {
      input=ps.zeichnen(input);
      ps.move();
      if (ps.kill()) {
        psactive=false;
      }
    }
    for (Obstacle o : obst) {
      PImage pic = o.getPic();
      if (pic == tree || pic == rocks || pic == boulder || pic==cloud || pic==cloud2 || pic==boulder2 || pic==tunnel1 || pic==tunnel2) input.image(o.getPic(), o.placeX(), o.placeY());
    }



    return input;
  }


  //          KOLLISION F\u00dcR VERSCHIEDENE TYPEN
  public int collision(int rekY, int currentAbility, boolean burrowed) {
    for (int i=0; i<obst.size (); i++) {
      PImage compare = obst.get(i).getPic();
      if (compare == box) {
        if (obst.get(i).placeX() < 60 && obst.get(i).placeX() > 10 && obst.get(i).placeY()-15 <= rekY && obst.get(i).placeY()+15 > rekY) {
          if (currentAbility==6) return 0;
          obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), box2));
          obst.remove(i);
          if (sound) {
            explosion = minim.loadFile("sound/box_explode.mp3");
            explosion.play();
          }
          if (currentAbility==1||currentAbility==3) return -20;
          else return 25;
        }
      }
      if (compare == shroom || compare == barrel) {
        if (obst.get(i).placeX() < 60 && obst.get(i).placeX() > 10 && obst.get(i).placeY()-15 <= rekY && obst.get(i).placeY()+15 > rekY) {
          if (currentAbility==6) return 0;
          if (compare==barrel) {
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), barrel2));
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), cloud));
          } else {
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), cloud2));
          }
          obst.remove(i);
          if (sound) {
            if (compare==shroom) explosion = minim.loadFile("sound/shroom_explode.mp3");
            else explosion = minim.loadFile("sound/barrel_explode.mp3");
            explosion.play();
          }
          if (currentAbility==1||currentAbility==3) {
            slowtimer = millis()+2000;
            return -30;
          } else {
            slowtimer = millis()+2000;
            return 40;
          }
        }
      }
      if (compare == stones) {
        if (obst.get(i).placeX() < 60 && obst.get(i).placeX() > 10) {
          if (burrowed && currentAbility != 5) {
            slowtimer= millis() + 2000;
            return 70;
          }
        }
      }
      if (compare == tree) {
        if (obst.get(i).hbX() < 60 && obst.get(i).hbX() > 10) {
          if (!burrowed && currentAbility != 2 && currentAbility != 3) {
            slowtimer= millis() + 2000;
            if (sound) {
              explosion = minim.loadFile("sound/box_explode.mp3");
              explosion.play();
            }
            return 70;
          }
          if (!burrowed && currentAbility == 3) {
            if (sound) {
              explosion = minim.loadFile("sound/box_explode.mp3");
              explosion.play();
            }
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), tree2));
            obst.remove(i);
            return -50;
          } else return 0;
        }
      }
      if (compare == rocks) {
        if (obst.get(i).hbX() < 60 && obst.get(i).hbX() > 10) {
          if (currentAbility != 3 && currentAbility != 6) {
            slowtimer= millis() + 2000;
            if (sound) {
              explosion = minim.loadFile("sound/barrel_explode.mp3");
              explosion.play();
            }
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), rocks2));
            obst.remove(i);
            return 75;
          }
          if (currentAbility == 3) {
            if (sound) {
              explosion = minim.loadFile("sound/barrel_explode.mp3");
              explosion.play();
            }
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), rocks2));
            obst.remove(i);
            return -50;
          } else return 0;
        }
      }

      if (compare == boulder) {
        if (obst.get(i).hbX() < 60 && obst.get(i).hbX() > -70) {
          if (currentAbility != 6) {
            slowtimer= millis() + 2000;
            if (sound) {
              explosion = minim.loadFile("sound/barrel_explode.mp3");
              explosion.play();
            }
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), boulder2));
            obst.remove(i);
            return 80;
          } else return 0;
        }
      }
    }
    return 0;
  }


  //KOLLISION VON PREYSEEKER
  public boolean preyFound(int psX, int psY) {
    for (int i=0; i<obst.size (); i++) {
      PImage compare = obst.get(i).getPic();
      if (compare == box) {
        if (obst.get(i).placeX() < psX+35 && obst.get(i).placeX() > psX+15 && obst.get(i).placeY()-15 <= psY && obst.get(i).placeY()+15 > psY) {
          obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), box2));
          obst.remove(i);
          if (sound) {
            explosion = minim.loadFile("sound/box_explode.mp3");
            explosion.play();
            prey = minim.loadFile("sound/rek_q2_hit.mp3");
            prey.play();
          }
          return true;
        }
      }
      if (compare == shroom || compare == barrel) {
        if (obst.get(i).placeX() < psX+35 && obst.get(i).placeX() > psX+15 && obst.get(i).placeY()-15 <= psY && obst.get(i).placeY()+15 > psY) {
          if (compare==barrel) {
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), barrel2));
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), cloud));
          } else {
            obst.add(new Obstacle(obst.get(i).placeX(), obst.get(i).placeY(), cloud2));
          }
          obst.remove(i);
          if (sound) {
            if (compare==shroom) explosion = minim.loadFile("sound/shroom_explode.mp3");
            else explosion = minim.loadFile("sound/barrel_explode.mp3");
            explosion.play();
            prey = minim.loadFile("sound/rek_q2_hit.mp3");
            prey.play();
          }
          return true;
        }
      }

      if (compare == rocks) {
        if (obst.get(i).hbX() < psX+35 && obst.get(i).hbX() > psX+15) {
          if (sound) {
            prey = minim.loadFile("sound/rek_q2_hit.mp3");
            prey.play();
          }
          return true;
        }
      }

      if (compare == boulder) {
        if (obst.get(i).hbX() < psX+35 && obst.get(i).hbX() > psX-65) {
          if (sound) {
            prey = minim.loadFile("sound/rek_q2_hit.mp3");
            prey.play();
          }
          return true;
        }
      }
    }
    return false;
  }

  public boolean isSlowed() {
    if (slowtimer>=millis()) return true;
    else return false;
  }

  public void createPS(int rekY) {
    ps=new PreySeeker(30, rekY);
    psactive = true;
  }

  public void createTunnel(int rekY, boolean t1) {
    if (t1)  obst.add(new Obstacle(20, rekY, tunnel1));
    else  obst.add(new Obstacle(20, rekY, tunnel2));
  }

  public void putRek(int rekY, PImage rek) {
    obst.add(new Obstacle(0, rekY, rek));
    gameover=true;
    rek_over = rek;
  }
}

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

  public PGraphics displayInfo(PGraphics input) {

    input.beginDraw();
    input.image(bg, 0, 0, 320, 180);
    input.noStroke();
    input.fill(0, 150);
    input.rect(5, 5, 310, 170, 5);

    // \u00dcBERSCHRIFTEN
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
      input.text("This game was developed as a semester\nproject at St. P\u00f6lten UAS in Austria in 2016.\n\nCoding & game design: Johannes Ambrosch\nCharacter art: Gabriel K\u00f6rber\n\nLeague of Legends and Rek'Sai are\nproperty of Riot Games Inc.", 15, 50);
    }



    if (backButtonHovered()) input.image(btnback2, 290, 150);
    else input.image(btnback, 290, 150);

    input.endDraw(); 
    return input;
  }

  public boolean backButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>150 * stretchY) return true;
    else return false;
  }


  public boolean generalHovered() {
    if (mouseX<108 * stretchX&&mouseY<30 * stretchY) return true;
    else return false;
  }
  public boolean abilitiesHovered() {
    if (mouseX>=108 * stretchX&&mouseX<=212 * stretchX&&mouseY<30 * stretchY) return true;
    else return false;
  }
  public boolean aboutHovered() {
    if (mouseX>212 * stretchX&&mouseY<30 * stretchY) return true;
    else return false;
  }
  public boolean abSwitchHovered() {
    if (mouseX>280 * stretchX&&mouseX<310 * stretchX&&mouseY>83 * stretchY&&mouseY<113 * stretchY) return true;
    else return false;
  }

  public int click() {
    if (backButtonHovered()) return 0;
    else if (generalHovered()) return 1;
    else if (abilitiesHovered()) return 2;
    else if (abSwitchHovered()) return 3;
    else if (aboutHovered()) return 4;
    else return -1;
  }

  public void setTab(int tab) {
    mode=tab;
  }

  public void flipTab() {
    if (mode==1)mode=2; 
    else mode=1;
  }
}

class MainMenu {

  PImage bg, title, startButton, startButton2, btnexit, btnsettings, btninfo, btnexit2, btnsettings2, btninfo2;
  PImage qdw, qdy, qdn, qdy2, qdn2; //QuitDialogWindow, Yes, No
  int[] sandX, sandY;
  float stretchX, stretchY;
  Gif rek;

  boolean qd = false;
  PApplet app;


  Minim minim;
  AudioPlayer player;


  MainMenu(PApplet app, float sX, float sY) {
    bg=loadImage("MainMenu.png");
    title=loadImage("Title.png");
    rek=new Gif(app, "rek/rek1.gif");
    rek.loop();
    btnexit=loadImage("btn_exit.png");
    btninfo=loadImage("btn_info.png");
    btnsettings=loadImage("btn_settings.png");
    btnexit2=loadImage("btn_exit_hover.png");
    btninfo2=loadImage("btn_info_hover.png");
    btnsettings2=loadImage("btn_settings_hover.png");
    startButton=loadImage("StartButton.png");
    startButton2=loadImage("StartButton_hover.png");
    qdw=loadImage("QuitDialog.png");
    qdy=loadImage("qd_yes.png");
    qdn=loadImage("qd_no.png");
    qdy2=loadImage("qd_yes_hover.png");
    qdn2=loadImage("qd_no_hover.png");
    stretchX=sX;
    stretchY=sY;
    this.app=app;

    minim = new Minim(this.app);

    sandX=new int[10];
    sandY=new int[10];
    for (int i=0; i<10; i++) {
      sandX[i]=PApplet.parseInt(random(320));
      sandY[i] = PApplet.parseInt(random(120))+60;
    }
  }

  public PGraphics displayMainMenu(PGraphics input) {

    input.beginDraw();
    PImage bgsand = bg.get(); // frischen BG holen, Pixels verdunkeln
    int sand= color(80, 80, 0);
    for (int i=0; i<10; i++) {
      bgsand.set(sandX[i], sandY[i], sand);
      sandX[i]-=3;
      if (sandX[i]<0) {      // Wenn Sand links rausrutscht, neu generieren
        sandX[i] = PApplet.parseInt(random(100))+320;
        sandY[i] = PApplet.parseInt(random(120))+60;
      }
    }
    input.image(bgsand, 0, 0, 320, 180);

    input.image(rek, 0, 80);

    input.image(title, 53, 20);

    if (exitButtonHovered()) input.image(btnexit2, 320-30, 0);
    else input.image(btnexit, 320-30, 0);

    if (settingsButtonHovered()) input.image(btnsettings2, 320-30, 180-30);
    else input.image(btnsettings, 320-30, 180-30);

    if (infoButtonHovered()) input.image(btninfo2, 320-30, 180-60);
    else input.image(btninfo, 320-30, 180-60);

    if (startButtonHovered()) input.image(startButton2, 86, 100);
    else input.image(startButton, 86, 100);


    if (qd) {          // QUITDIALOG
      input.fill(125, 125, 125, 180);
      input.noStroke();
      input.rect(0, 0, 320, 180);
      input.image(qdw,40,30);
      if(quitYesHovered()) input.image(qdy2,160-78-20,110);
      else input.image(qdy,160-78-20,110);
      if(quitNoHovered()) input.image(qdn2,160+20,110);
      else input.image(qdn,160+20,110);
    }

    input.endDraw(); 
    return input;
  }

  public boolean startButtonHovered() {
    int btnX=320-86;
    int btnY = 100+59;
    if (mouseX>86 * stretchX&&mouseX<btnX * stretchX&&mouseY>100 * stretchY &&mouseY<btnY*stretchY && qd==false) return true;
    else return false;
  }
  public boolean infoButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>120 * stretchY &&mouseY<150 * stretchY&& qd==false) return true;
    else return false;
  }
  public boolean settingsButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>150 * stretchY && qd==false) return true;
    else return false;
  }
  public boolean exitButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY<30 *stretchY && qd==false) return true;
    else return false;
  }
  
  
  
  public boolean quitYesHovered(){
    if (mouseX>62 * stretchX&&mouseX<140 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && qd==true) return true;
    else return false;
  }
  public boolean quitNoHovered(){
    if (mouseX>180 * stretchX&&mouseX<258 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && qd==true) return true;
    else return false;
  }


  public int click() {
    if (!qd) {
      if (startButtonHovered()) {
        return 0;
      } else if (exitButtonHovered()) {
        return 1;
      } else if (settingsButtonHovered()) {
        if(sound) player = minim.loadFile("openmenu.mp3");
        if(sound) player.play();
        return 2;
      } else if (infoButtonHovered()) {
        if(sound) player = minim.loadFile("openmenu.mp3");
        if(sound) player.play();
        return 3;
      } else return -1;
    } else {
      if(quitYesHovered()) return 4;
      else if(quitNoHovered()) return 5;
      else return -1;
    }
  }


  public void showQD() {
    if(sound) player = minim.loadFile("openmenu.mp3");
    if(sound) player.play();
    qd=true;
  }
  public void hideQD() {
    if(sound) player = minim.loadFile("closemenu.mp3");
    if(sound) player.play();
    qd=false;
  }
  public boolean getQD() {
    return qd;
  }

}

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

  public void move() {
    xpos-=3;
  }
  
  public int placeX(){
     return xpos+offsetX; 
  }
  
  public int placeY(){
     return ypos+offsetY; 
  }
  
  public int hbX(){
    return xpos;
  }
  
  public int hbY(){
    return ypos;
  }
  
  public PImage getPic(){
     return pic; 
  }
}

class PreySeeker {

  int xpos, ypos;
  PImage pic;

  PreySeeker(int x, int y) {
    xpos=x;
    ypos=y;
    pic=loadImage("preyseeker.png");
  }


  public void move() {
    xpos+=5;
  }

  public PGraphics zeichnen(PGraphics input) {
    input.image(pic, xpos, ypos);
    return input;
  }

  public boolean kill() {
    if (xpos>220) return true;
    else return false;
  }

  public int getX() {
    return xpos;
  }

  public int getY() {
    return ypos;
  }
}

class Settings {

  PImage bg, btnback, btnback2, btnon, btnoff, btnon2, btnoff2;
  float stretchX, stretchY;

  PApplet app;

  Settings(PApplet app, float sX, float sY) {
    bg=loadImage("Settings.png");
    stretchX=sX;
    stretchY=sY;
    this.app=app;

    btnback = loadImage("btn_back.png");
    btnback2 = loadImage("btn_back_hover.png");
    btnon = loadImage("set_on.png");
    btnoff = loadImage("set_off.png");
    btnon2 = loadImage("set_on_hover.png");
    btnoff2 = loadImage("set_off_hover.png");
  }

  public PGraphics displaySettings(PGraphics input, boolean music, boolean sound) {

    input.beginDraw();
    input.image(bg, 0, 0, 320, 180);

    if (backButtonHovered()) input.image(btnback2, 290, 150);
    else input.image(btnback, 290, 150);
    
    if(music){
      if(musicHovered())input.image(btnon2,190,42);
      else input.image(btnon,190,42);
    }
    else{
       if(musicHovered()) input.image(btnoff2,190,42);
       else input.image(btnoff,190,42);
    }
    if(sound){
      if(soundHovered())input.image(btnon2,190,94);
      else input.image(btnon,190,94);
    }
    else{
       if(soundHovered()) input.image(btnoff2,190,94);
       else input.image(btnoff,190,94);
    }
    input.endDraw(); 
    return input;
  }

  public boolean backButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>150 * stretchY) return true;
    else return false;
  }
  public boolean musicHovered() {
    if (mouseX>190 * stretchX&&mouseX<268 * stretchX&&mouseY>42 * stretchY&&mouseY<72 * stretchY) return true;
    else return false;
  }
  public boolean soundHovered() {
    if (mouseX>190 * stretchX&&mouseX<268 * stretchX&&mouseY>94 * stretchY&&mouseY<124 * stretchY) return true;
    else return false;
  }

  public int click() {
    if(backButtonHovered()) return 0;
    else if(musicHovered()) return 1;
    else if(soundHovered()) return 2;
    else return -1;
  }

  
}


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Void_Rush" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
