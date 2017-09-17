package com.example.jacob.wizardduel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayScreen extends AppCompatActivity {

    int player = 1;
    int p1Health = 100;
    int p2Health = 100;
    int p1Mana = 10;
    int p2Mana = 10;
    int maxMana = 100;
    int basic_cost = 2;
    int basic_damage = 1;
    int heavy_cost = 0;
    int heavy_damage = 2;
    int dpr_cost = 0;
    int dpr_base_damage = 0;
    int dpr_round_damage = 0;
    int dpr_rounds_left = 0;
    boolean p1_dpr = false;
    boolean p2_dpr = false;
    int shield_cost = 0;
    boolean shield_status = false;
    int heal_cost = 0;
    int heal_effect = 5;
    final int mana_per_round = 5;
    boolean turnOver = false;
    boolean gameOver = false;
    int refresh = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        final Button basic_attack = (Button) findViewById(R.id.basic_attack);
        final Button heavy_attack = (Button) findViewById(R.id.heavy_attack);
        final Button dpr_attack = (Button) findViewById(R.id.dpr_attack);
        final Button shield = (Button) findViewById(R.id.shield);
        final Button heal = (Button) findViewById(R.id.heal);

        updateUI(p1Health, p2Health);

        basic_attack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!checkShield(shield_status)) {
                    if (player == 1) {
                        p2Health -= basic_damage;
                    } else {
                        p1Health -= basic_damage;
                    }
                } else {
                    turnOffShield();
                }
                if(player == 1) {
                    p1Mana -= basic_cost;
                } else {
                    p2Mana -= basic_cost;
                }
                turnOver = true;
                updateUI(p1Health, p2Health);
            }
        });

        heavy_attack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!checkShield(shield_status)) {
                    if (player == 1) {
                        p2Health -= heavy_damage;
                    } else {
                        p1Health -= heavy_damage;
                    }
                } else {
                    turnOffShield();
                }
                if(player == 1) {
                    p1Mana -= heavy_cost;
                } else {
                    p2Mana -= heavy_cost;
                }
                turnOver = true;
                updateUI(p1Health, p2Health);
            }
        });

        dpr_attack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!checkShield(shield_status)) {
                    dpr_rounds_left = 6;
                    if (player == 1) {
                        p2Health -= dpr_base_damage;
                        p2_dpr = true;
                    } else {
                        p1Health -= dpr_base_damage;
                        p2_dpr = true;
                    }
                    dpr_rounds_left -= 1;
                } else {
                    turnOffShield();
                }
                if(player == 1) {
                    p1Mana -= dpr_cost;
                } else {
                    p2Mana -= dpr_cost;
                }
                turnOver = true;
                updateUI(p1Health, p2Health);
            }
        });

        shield.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shield_status = true;
                if(player == 1) {
                    p1Mana -= shield_cost;
                } else {
                    p2Mana -= shield_cost;
                }
                turnOver = true;
                updateUI(p1Health, p2Health);
            }
        });

        heal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (player == 1) {
                    p1Health += heal_effect;
                    if (p1Health > 100) {
                        p1Health = 100;
                    }
                } else {
                    p2Health += heal_effect;
                    if (p2Health > 100) {
                        p2Health = 100;
                    }
                }
                if(player == 1) {
                    p1Mana -= heal_cost;
                } else {
                    p2Mana -= heal_cost;
                }
                turnOver = true;
                updateUI(p1Health, p2Health);
            }
        });
    }

    void updateUI (int p1Health, int p2Health) {
        final TextView turnID = (TextView) findViewById(R.id.turnID);
        final TextView stats = (TextView) findViewById(R.id.stats);

        if (p1Health <= 0) {
            //print Player 2 wins
            gameOver = true;
        } else if (p2Health <= 0) {
            //print Player 1 wins
            gameOver = true;
        }

        if(gameOver){
            System.exit(0);
        }

        if (turnOver && player == 1) {
            p1Mana += mana_per_round;
            if (p1Mana > maxMana) {
                p1Mana = maxMana;
            }
            player = 2;
        } else if (turnOver && player == 2) {
            p2Mana += mana_per_round;
            if (p2Mana > maxMana) {
                p2Mana = maxMana;
            }
            player = 1;
        }


        if (player == 1) {
            turnID.setText("Player 1's Turn");
            checkForDPR(p1_dpr);
        } else {
            turnID.setText("Player 2's Turn");
            checkForDPR(p2_dpr);
        }
        stats.setText("P1 Health: " + p1Health + "/100\n" + "P1 Mana: " + p1Mana + "/100\n" + "P2 Health: " + p2Health + "/100\n" + "P2 Mana: " + p2Mana + "/100\n");
    }

    void checkForDPR(boolean status) {
        if(status) {
            if(player == 1) {
                p1Health -= dpr_round_damage;
                dpr_rounds_left -= 1;
                if(dpr_rounds_left == 0) {
                    p1_dpr = false;
                }
            } else {
                p2Health -= dpr_round_damage;
                dpr_rounds_left -= 1;
                if(dpr_rounds_left == 0) {
                    p2_dpr = false;
                }
            }
            if(p1Health <= 0) {
                //player 2 wins
                System.exit(0);
            } else if(p2Health <= 0) {
                //player 1 wins
                System.exit(0);
            }
        }
    }


    boolean checkShield(boolean s) {
        return s;
    }

    void turnOffShield() {
        shield_status = false;
    }
}
