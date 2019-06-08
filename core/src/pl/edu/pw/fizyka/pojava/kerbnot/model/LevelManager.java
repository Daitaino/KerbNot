package pl.edu.pw.fizyka.pojava.kerbnot.model;

import static pl.edu.pw.fizyka.pojava.kerbnot.model.Playable.BASE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import pl.edu.pw.fizyka.pojava.kerbnot.controller.WorldController;
import pl.edu.pw.fizyka.pojava.kerbnot.util.Constants;

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
        level.playable = new Playable(20000, 18000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 0.75e5f, level.world);
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
                               popup.setText("You must regain control! But don't worry, I will guide you.");
                               objectiveWindow.setText("Regain control of the ship.");
                           }
                       },
                time);
        time += 16;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Use RIGHT & LEFT arrow keys to control angular movement.");
                               popup.setText("Now stop that spinning.");
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
                popup.setText("SAS restored! \n The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning to 0.");
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

                popup.setText("That's better! Now use the UP & DOWN arrow keys to increase/ decrease" +
                        "your thrust. \n Try to reduce your velocity to zero " +
                        "(The velocity display is at the top).");
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
                popup.setText("OK! Now let's get our bearings! \n Use A & S keys to zoom out or zoom in, and press ESC for the Pause Menu." +
                        " Then let's turn up those engines!");
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
                popup.setText("Supercomputer connections restored! Trajectory calculation features online." +
                        " (Press T to activate it. It doesn't work unless you're moving.)");
                WorldController.controlState = 6;

                time = 5f;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       level.screen.getMinimap().setEnabled(true);
                                       popup.setText("Navigation system restored! Minimap online Look's like Earth's not too far away.");
                                   }
                               },
                        time);
                time += 8f;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("All systems restored! Now it's time to find your way back home! " +
                                               "This blue waypoint will guide you to your objective!");
                                       level.waypoint = new Waypoint(level, 14000, 9000, 1000);
                                       objectiveWindow.setText("Find your way back to Earth");
                                   }
                               },
                        time);
                time += 15;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("As said by Newton's first law of motion, unless a force (like thrust) " +
                                               "acts on you, you would drift at constant speed for ever! So better not run out of fuel in space!");
                                   }
                               },
                        time);
                time += 23;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("As Newton's second law of motion says, force is the rate of change of a body's" +
                                               "momentum, which its likelihood of preserving its speed. \n" +
                                               "The rocket is pretty heavy, so accelerating takes time. Be careful, because "
                                               + "that means stopping takes time too!");
                                   }
                               },
                        time);
                time += 28;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("See that trail behind the rocket? As Newton's third law of motion says, " +
                                               "forces exist in opposite-directioned pairs. \n" +
                                               "That's why you need to blast away tons of hot plasma to accelerate!");
                                   }
                               },
                        time);
                time += 23;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("Remember to slow down near Earth! Coming in too fast will make you crash!");
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
            }
        });
    }
}