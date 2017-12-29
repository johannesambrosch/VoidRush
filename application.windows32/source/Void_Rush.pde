import gifAnimation.*;

import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

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

void setup() {
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



  //Hauptmenü erstellen
  main = new MainMenu(this, stretchX, stretchY);
}

void draw() {
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
void mousePressed() {
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
void keyPressed() {
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
void keyReleased() {
  if (mode == 1) {
    game.release();
  }
}


boolean sketchFullScreen() {
  return true;
}

void resetSave() {
  String[] save = new String[3];
  save[0] = "1";
  save[1] = "1";
  save[2] = "0";
  saveStrings("data/save.log", save);
}

