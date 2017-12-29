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
      sandX[i]=int(random(320));
      sandY[i] = int(random(120))+60;
    }
  }

  PGraphics displayMainMenu(PGraphics input) {

    input.beginDraw();
    PImage bgsand = bg.get(); // frischen BG holen, Pixels verdunkeln
    color sand= color(80, 80, 0);
    for (int i=0; i<10; i++) {
      bgsand.set(sandX[i], sandY[i], sand);
      sandX[i]-=3;
      if (sandX[i]<0) {      // Wenn Sand links rausrutscht, neu generieren
        sandX[i] = int(random(100))+320;
        sandY[i] = int(random(120))+60;
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

  boolean startButtonHovered() {
    int btnX=320-86;
    int btnY = 100+59;
    if (mouseX>86 * stretchX&&mouseX<btnX * stretchX&&mouseY>100 * stretchY &&mouseY<btnY*stretchY && qd==false) return true;
    else return false;
  }
  boolean infoButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>120 * stretchY &&mouseY<150 * stretchY&& qd==false) return true;
    else return false;
  }
  boolean settingsButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>150 * stretchY && qd==false) return true;
    else return false;
  }
  boolean exitButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY<30 *stretchY && qd==false) return true;
    else return false;
  }
  
  
  
  boolean quitYesHovered(){
    if (mouseX>62 * stretchX&&mouseX<140 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && qd==true) return true;
    else return false;
  }
  boolean quitNoHovered(){
    if (mouseX>180 * stretchX&&mouseX<258 * stretchX&&mouseY>110 * stretchY &&mouseY<140*stretchY && qd==true) return true;
    else return false;
  }


  int click() {
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


  void showQD() {
    if(sound) player = minim.loadFile("openmenu.mp3");
    if(sound) player.play();
    qd=true;
  }
  void hideQD() {
    if(sound) player = minim.loadFile("closemenu.mp3");
    if(sound) player.play();
    qd=false;
  }
  boolean getQD() {
    return qd;
  }

}

