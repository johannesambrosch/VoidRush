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

  PGraphics displaySettings(PGraphics input, boolean music, boolean sound) {

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

  boolean backButtonHovered() {
    if (mouseX>290 * stretchX&&mouseY>150 * stretchY) return true;
    else return false;
  }
  boolean musicHovered() {
    if (mouseX>190 * stretchX&&mouseX<268 * stretchX&&mouseY>42 * stretchY&&mouseY<72 * stretchY) return true;
    else return false;
  }
  boolean soundHovered() {
    if (mouseX>190 * stretchX&&mouseX<268 * stretchX&&mouseY>94 * stretchY&&mouseY<124 * stretchY) return true;
    else return false;
  }

  int click() {
    if(backButtonHovered()) return 0;
    else if(musicHovered()) return 1;
    else if(soundHovered()) return 2;
    else return -1;
  }

  
}


