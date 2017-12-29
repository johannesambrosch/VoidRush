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
      sandX[i]=int(random(320));
      sandY[i] = int(random(120))+60;
    }

    //          SET REKSAI POSITION
    rekY = 90;

    moveup = false;
    movedown = false;

    //    GENERATE GRID
    grid = new Grid(app, sound);
  }

  PGraphics displayGame(PGraphics input) {

    input.beginDraw();
    PImage bgsand = bg.get(); // frischen BG holen, Pixels verdunkeln
    color sand= color(80, 80, 0);
    for (int i=0; i<20; i++) {
      bgsand.set(sandX[i], sandY[i], sand);
      if (!paused&&!win) sandX[i]-=3;
      if (sandX[i]<0) {      // Wenn Sand links rausrutscht, neu generieren
        sandX[i] = int(random(100))+320;
        sandY[i] = int(random(120))+60;
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
      int maphpdrop = int(map(hpdrop, 0, 100, 0, 156));
      input.image(hpred, 82, 2, maphpdrop, 16);
      int maphp = int(map(health, 0, 100, 0, 156));
      input.image(hpbar, 82, 2, maphp, 16);  // from 80 to 160, only -2 on each side for border, same for height 0 to 20


      //          EXP BAR & Character Icon + Rect
      input.image(icon, 0, 0, 40, 40);
      input.rect(40, 0, 10, 40);
      int mapxp = int(map(exp, levels[lvl], levels[lvl+1], 0, 38));
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
            float cooldown = float((q1CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 233, 172); //TEXT ALWAYS AT 172 HEIGHT
            else input.text(int(cooldown), 233, 172);
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
            float cooldown = float((wCD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 258, 172);
            else input.text(int(cooldown), 258, 172);
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
            float cooldown = float((e1CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 283, 172);
            else input.text(int(cooldown), 283, 172);
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
            float cooldown = float((q2CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 233, 172);
            else input.text(int(cooldown), 233, 172);
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
            float cooldown = float((wCD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 258, 172);
            else input.text(int(cooldown), 258, 172);
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
            float cooldown = float((e2CD)-millis()) / 1000;
            if (cooldown<3) input.text(nfc(cooldown, 1), 283, 172);
            else input.text(int(cooldown), 283, 172);
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
  void press() {
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
  void release() {
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

  int click() {           // -1 = nothing, 0 = Pause Dialog 1234 = qwer, 5678 = lvl qwer, 9=Stay, 10=quit
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
  boolean qHovered() {
    if (mouseX>220 * stretchX&&mouseY>155 * stretchY&&mouseX<245 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }
  boolean wHovered() {
    if (mouseX>245 * stretchX&&mouseY>155 * stretchY&&mouseX<270 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }
  boolean eHovered() {
    if (mouseX>270 * stretchX&&mouseY>155 * stretchY&&mouseX<295 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }
  boolean rHovered() {
    if (mouseX>295 * stretchX&&mouseY>155 * stretchY&&mouseX<320 * stretchX &&mouseY<180 * stretchY) return true;
    else return false;
  }

  //      LevelUp Hover
  boolean lqHovered() {
    if (mouseX>220 * stretchX&&mouseY>130 * stretchY&&mouseX<245 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  boolean lwHovered() {
    if (mouseX>245 * stretchX&&mouseY>130 * stretchY&&mouseX<270 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  boolean leHovered() {
    if (mouseX>270 * stretchX&&mouseY>130 * stretchY&&mouseX<295 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  boolean lrHovered() {
    if (mouseX>295 * stretchX&&mouseY>130 * stretchY&&mouseX<320 * stretchX &&mouseY<155 * stretchY) return true;
    else return false;
  }
  boolean pauseButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY<30 *stretchY) return true;
    else return false;
  }
  boolean stayHovered() {
    if (mouseX>62 * stretchX&&mouseX<140 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && paused==true) return true;
    else return false;
  }
  boolean quitHovered() {
    if (mouseX>180 * stretchX&&mouseX<258 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && paused==true) return true;
    else return false;
  }

  //          PAUSE DIALOG
  void togglePD() {
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
  void useQ() {
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

  void q1Finish() {
    qcount=0;
  }

  void useW() {
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

  void useE() {
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

  void useR() {
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
  void levelQ() {
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

  void levelW() {
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

  void levelE() {
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

  void levelR() {

    if (sound) {
      abilitySounds = minim.loadFile("sound/ability_levelup.mp3");
      abilitySounds.play();
    }
    rlvl++;
  }

  void levelUp() {
    lvl++;
    if (sound) {
      abilitySounds = minim.loadFile("sound/level_up.mp3");
      abilitySounds.play();
    }
  }
}

