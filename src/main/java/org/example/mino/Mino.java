package org.example.mino;

import org.example.main.*;

import java.awt.*;
import java.security.Key;

public class Mino {

    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCounter = 0;

    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }
    public void setXY(int x, int y) { }
    public void updateXY(int direction) {

        checkRotationCollision();
        if (leftCollision == false && rightCollision == false && bottomCollision == false) {
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }
    public void getDirection1 () {}
    public void getDirection2 () {}
    public void getDirection3 () {}
    public void getDirection4 () {}
    public void checkMovementCollision () {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // Check frame collision
        // Left Wall
        for(int i = 0; i < b.length; i++) {
            if(b[i].x == PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // Right Wall
        for (int i = 0; i < b.length; i++) {
            if(b[i].x + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
            }
        }

        // Bottom floor
        for(int i = 0; i < b.length; i++) {
            if(b[i].y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }
    public void checkRotationCollision () {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkMovementCollision();

        // Check frame collision
        // Left Wall
        for(int i = 0; i < b.length; i++) {
            if(tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // Right Wall
        for (int i = 0; i < b.length; i++) {
            if(tempB[i].x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
            }
        }

        // Bottom floor
        for(int i = 0; i < b.length; i++) {
            if(tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }
    private void checkStaticBlockCollision () {

        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {

            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // check down
            for (int j = 0; j < b.length; j++) {
                if (b[j].x == targetX && b[j].y + Block.SIZE== targetY) {
                    bottomCollision = true;
                }
            }
            // check left
            for (int j = 0; j < b.length; j++) {
                if (b[j].x - Block.SIZE == targetX && b[j].y == targetY) {
                    leftCollision = true;
                }
            }
            // check right
            for (int j = 0; j < b.length; j++) {
                if (b[j].x + Block.SIZE == targetX && b[j].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }
    public void update() {

        if (deactivating) {
            deactivating();
        }

        // Move the mino
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1: getDirection2();break;
                case 2: getDirection3();break;
                case 3: getDirection4();break;
                case 4: getDirection1();break;
            }
            KeyHandler.upPressed = false;
        }
        checkMovementCollision();
        if (KeyHandler.downPressed) {
            // If the mino's bottom is not hitting, it can go down
            if (bottomCollision == false) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                // When moved down, reset the autoDropCounter
                autoDropCounter = 0;
            }


            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if (leftCollision == false) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;

            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            if (rightCollision == false) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }


            KeyHandler.rightPressed = false;
        }
        if (KeyHandler.spacePressed) {
            while (!bottomCollision) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                // Recalcula a colisão após mover
                checkMovementCollision();
            }

            // Após a descida direta, zera o autoDropCounter
            autoDropCounter = 0;
            KeyHandler.spacePressed = false;
        }
        if (bottomCollision) {
            deactivating = true;
        } else {
            autoDropCounter++; // the counter increases in every frame
            if (autoDropCounter == PlayManager.dropInterval) {
                // the mino goes down
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }


    }
    private void deactivating () {
        deactivateCounter++;

        // Wait 45 frames until diactivate
        if (deactivateCounter == 45) {

            deactivateCounter = 0;
            checkMovementCollision(); // check if the bottom is still hitting

            // if the bottom is still hitting after 45 frames, deactivate the mino
            if(bottomCollision) {
                active = false;
            }
        }
    }
    public void draw(Graphics2D g2) {

        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
    }
}
