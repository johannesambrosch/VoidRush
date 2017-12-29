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

  void moveGrid(int level, int qlvl, int wlvl, int elvl, int q1CD, int q2CD, int wCD, int e1CD, int e2CD, boolean burrowed, boolean casting) {
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

  void update(int level, int qlvl, int wlvl, int elvl, int q1CD, int q2CD, int wCD, int e1CD, int e2CD, boolean burrowed, boolean casting) {
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
            int rndobst = int(random(6));
            if (rndobst<4) placeobst = box;
            else if (rndobst==4) placeobst=barrel;
            else placeobst=shroom;
            obst.add(new Obstacle(351, 60, placeobst));
            obst.add(new Obstacle(351, 90, placeobst));
            obst.add(new Obstacle(351, 120, placeobst));
            spawnchance=2;
          } else rng=10;
        } else {    //      SMALL 2 per row Obstacles
          int rndobst = int(random(6));
          if (rndobst<4) placeobst = box;
          else if (rndobst==4) placeobst=barrel;
          else placeobst=shroom;
          int emptypos = int(random(3));
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

  PGraphics displayGrid(PGraphics input, PImage rek, int rekY, boolean rush) {
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


  //          KOLLISION FÜR VERSCHIEDENE TYPEN
  int collision(int rekY, int currentAbility, boolean burrowed) {
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
  boolean preyFound(int psX, int psY) {
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

  boolean isSlowed() {
    if (slowtimer>=millis()) return true;
    else return false;
  }

  void createPS(int rekY) {
    ps=new PreySeeker(30, rekY);
    psactive = true;
  }

  void createTunnel(int rekY, boolean t1) {
    if (t1)  obst.add(new Obstacle(20, rekY, tunnel1));
    else  obst.add(new Obstacle(20, rekY, tunnel2));
  }

  void putRek(int rekY, PImage rek) {
    obst.add(new Obstacle(0, rekY, rek));
    gameover=true;
    rek_over = rek;
  }
}

