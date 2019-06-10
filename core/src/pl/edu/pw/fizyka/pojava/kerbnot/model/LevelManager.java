package pl.edu.pw.fizyka.pojava.kerbnot.model;

import static pl.edu.pw.fizyka.pojava.kerbnot.model.Playable.BASE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import pl.edu.pw.fizyka.pojava.kerbnot.controller.WorldController;
import pl.edu.pw.fizyka.pojava.kerbnot.util.Constants;
import pl.edu.pw.fizyka.pojava.kerbnot.util.GamePreferences;

public class LevelManager {

    static float time;

    public static Level createLevel() {
        final Level level = new Level();
        level.setState(Level.State.PAUSED);
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");

        level.map = new Map(Gdx.graphics.getWidth() * 600, Gdx.graphics.getHeight() * 700);

        //Earth
        level.planets.add(new Planet(14000, 9000, 6.0f * 1e25f, 650, null, level.world, 1));
        //Moon
        level.planets.add(new Planet(7000, 12000, 1.0f * 1.0e25f, 170, level.planets.get(0), level.world, 3));
        level.planets.get(1).setOrbitPreset(true);
        //initialization of the rocket
        //level.playable = new Playable(20000, 18000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 0.75e5f, level.world);
        level.playable = new Playable(20000, 18000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, Playable.getStartingFuel(), level.world);
        level.playable.getBody().setLinearVelocity(5f, 2f);
        level.playable.getBody().setAngularVelocity(5f);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(14000, 9000, 1000, level.playable) {
            @Override
            public void triggerAction() {
                if (level.playable.getBody().getLinearVelocity().len() < 80) {
                    WorldController.controlState = 7;
                    level.setState(Level.State.FINISHED);
                }
            }
        };
        level.triggers.add(earthTrig);

        time = 6f;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.getMinimap().setEnabled(false);
                               TrajectorySimulator.enabled = false;
                               WorldController.controlState = 1;
                           }
                       },
                time);
        time += 2;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText(GamePreferences.getInstance().isEnglishActive() ? "You must regain control! But don't worry, I will guide you." :
                            	   "Musisz odzyskac kontrole! Ale nie martw sie, poprowadze Cie.");
                               objectiveWindow.setText(GamePreferences.getInstance().isEnglishActive() ? "Regain control of the ship." :
                            	   "Odzyskaj kontrole nad statkiem.");
                           }
                       },
                time);
        time += 16;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText(GamePreferences.getInstance().isEnglishActive() ? "Use RIGHT & LEFT arrow keys to control angular movement." :
                            	   "Uzywaj prawej i lewej strzalki, aby kontrolowac ruch obrotowy.");
                               popup.setText(GamePreferences.getInstance().isEnglishActive() ? "Now stop that spinning." :
                            	   "A teraz zatrzymaj krecenie sie.");
                               WorldController.controlState = 2;
                           }
                       },
                time);

        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 2 && level.playable.getBody().getAngularVelocity() < 0.25;
            }

            @Override
            public void triggerAction() {
                popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                		"SAS restored! \n The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning to 0." : 
                		"System Sas odzyskany! \n Prawy shift uruchamia Sas, a to automatycznie redukuje obracanie sie do 0.");
                WorldController.controlState = 3;
            }
        });

        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 3 && level.playable.getBody().getAngularVelocity() == 0 && level.playable.getSASEnabled();
            }

            @Override
            public void triggerAction() {

                popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                		"That's better! Now use the UP & DOWN arrow keys to increase/ decrease" +
                        "your thrust. \n Try to reduce your velocity to zero " +
                        "(The velocity display is at the top)." :
                        "Tak lepiej! Teraz uzywaj gornej i dolnej strzalki aby zwiekszac/zmniejszac" +
                        "si³e ciagu. \n Sprobuj zredukowac swoja predkosc do zera " +
                        "(Predkosc wyswietla sie na gorze ekranu)");
                WorldController.controlState = 4;
            }
        });
        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 4 && (level.playable.getBody().getLinearVelocity().len() < 1.5);
            }

            @Override
            public void triggerAction() {
                popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                		"OK! Now let's get our bearings! \n Use A & S keys to zoom out or zoom in, and press ESC for the Pause Menu." +
                        " Then let's turn up those engines!" :
                        "OK! Teraz mozesz rozeznac sie w otoczeniu! \n Uzywajac klawiszy A i S mozesz oddalac i przyblizac widok, " +
                        "a klikajac ESC wlaczysz pauze.");
                level.playable.setCurrentThrust(0);
                WorldController.controlState = 5;
            }
        });

        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 5 && level.playable.getCurrentThrust() > 100;
            }

            @Override
            public void triggerAction() {
                TrajectorySimulator.enabled = true;
                popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                		"Supercomputer connections restored! Trajectory calculation features online." +
                        " (Press T to activate it. It doesn't work unless you're moving.)" :
                        "Odzyskano polaczenie z superkomputerem! Kalkulacja trajektorii odbywa sie online." +
                        "(Nacisnij T aby ja aktywowac. Nie zadziala, dopoki nie bedziesz sie ruszac.)");
                WorldController.controlState = 6;

                time = 5f;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       level.screen.getMinimap().setEnabled(true);
                                       popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                                    		   "Navigation system restored! Minimap online Look's like Earth's not too far away." :
                                    		   "Odzyskano system nawigacji! Wyglada na to, ze Ziemia nie jest daleko.");
                                   }
                               },
                        time);
                time += 8f;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                                    		   "All systems restored! Now it's time to find your way back home! " +
                                               "This blue waypoint will guide you to your objective!" : 
                                    		   "Wszystkie systemy dzialaja poprawnie! Czas znalezc droge do domu! " +
                                               "Niebieska strzalka poprowadzi Cie do celu.");
                                       level.waypoint = new Waypoint(level, 14000, 9000, 1000);
                                       objectiveWindow.setText(GamePreferences.getInstance().isEnglishActive() ? "Find your way back to Earth" :
                                    	   "Znajdz droge do domu");
                                   }
                               },
                        time);
                time += 15;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                                    		   "As said by Newton's first law of motion, unless a force (like thrust) " +
                                               "acts on you, you would drift at constant speed for ever! So better not run out of fuel in space!" :
                                    		   "Zgodnie z pierwsz¹ zasada Newtona, jesli nie dziala na Ciebie zadna sila (np. sila ciagu), " +
                                               "bedziesz juz zawsze poruszac sie ze stala predkoscia. Lepiej, zeby nie zabraklo Ci paliwa w kosmosie!");
                                   }
                               },
                        time);
                time += 23;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                                    		   "As Newton's second law of motion says, force is the rate of change of a body's" +
                                               "momentum, which its likelihood of preserving its speed. \n" +
                                               "The rocket is pretty heavy, so accelerating takes time. Be careful, because "
                                               + "that means stopping takes time too!" :
                                    		   "Jak mowi druga zasada Newtona, sila zmienia przyspieszenie ciala. " +
                                               "Rakieta jest dosc ciezka, wiec rozpedzenie sie zabierze troche czasu. " +
                                    		   "Uwazaj, bo to oznacza, ze hamowanie tez troche potrwa!");
                                   }
                               },
                        time);
                time += 28;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                                    		   "See that trail behind the rocket? As Newton's third law of motion says, " +
                                               "forces exist in opposite-directioned pairs. \n" +
                                               "That's why you need to blast away tons of hot plasma to accelerate!" :
                                    		   "Widzisz ten slad za rakieta? Jak mowi trzecia zasada Newtona, " +
                                               "sily wystepuja parami: sa to sily akcji i reakcji. " + 
                                    		   "Dlatego, aby sie rozpedzic, musisz wystrzelic za soba tony goracej plazmy!");
                                   }
                               },
                        time);
                time += 23;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText(GamePreferences.getInstance().isEnglishActive() ? 
                                    		   "Remember to slow down near Earth! Coming in too fast will make you crash!" :
                                    		   "Pamietaj, aby zwolnic w poblizu Ziemi! Jesli bedziesz poruszac sie zbyt szybko, rozbijesz sie!");
                                   }
                               },
                        time);

            }
        });

        return level;
    }

    private static void addDefaultTriggers(final Level level) {
        // out of map trigger
        level.triggers.add(new Trigger() {
            @Override
            protected boolean isTriggeredInternal() {
                Vector2 pos = level.playable.getBody().getPosition();
                int width = level.map.getWidth();
                int height = level.map.getHeight();

                return pos.x < 0 || pos.y < 0 || pos.x > width || pos.y > height;
            }

            @Override
            public void triggerAction() {
                if (!Constants.DEBUG) {
                    level.healthLost();
                }
                else if (Constants.DEBUG)
                	level.healthLost();
            }
        });

        // fuel depletion trigger
        level.triggers.add(new Trigger() {
            @Override
            protected boolean isTriggeredInternal() {
                return level.playable.getFuelLeft() <= 0;
            }

            @Override
            public void triggerAction() {
                if (!Constants.DEBUG) {
                    level.healthLost();
                }
                else if (Constants.DEBUG)
                	level.healthLost();
            }
        });
    }
}