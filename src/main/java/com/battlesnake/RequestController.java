/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.battlesnake;

import com.battlesnake.data.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@RestController
public class RequestController {

    private static final Logger LOGGER = Logger.getLogger("RequestController");

    @RequestMapping(value="/start", method=RequestMethod.POST, produces="application/json")
    public StartResponse start(@RequestBody StartRequest request) {
        return new StartResponse()
                .setName("NANO SNAKE")
                .setColor("#FF0000")
                .setHeadUrl("http://vignette1.wikia.nocookie.net/nintendo/images/6/61/Bowser_Icon.png/revision/latest?cb=20120820000805&path-prefix=en")
                .setHeadType(HeadType.DEAD)
                .setTailType(TailType.PIXEL)
                .setTaunt("Yay!");
    }

    @RequestMapping(value="/move", method=RequestMethod.POST, produces = "application/json")
    public MoveResponse move(@RequestBody MoveRequest request) {

        int boardWidth = request.getWidth();
        int boardHeight = request.getHeight();

        Snake me = getMySnake(request.getYou(), request.getSnakes());

        int[] head = me.getCoords()[0];
        int headX = head[0];
        int headY = head[1];

        Move move = Move.DOWN;
        Move foodMove = getFood(me.getCoords(), request.getFood());
        
        List<Move> possibleMoves = new ArrayList<>();
        if (!isPositionSuicidal(request.getSnakes(), headX, headY - 1, boardWidth, boardHeight)) {
            possibleMoves.add(Move.UP);
        }
        if (!isPositionSuicidal(request.getSnakes(), headX, headY + 1, boardWidth, boardHeight)) {
            possibleMoves.add(Move.DOWN);
        }
        if (!isPositionSuicidal(request.getSnakes(), headX - 1, headY, boardWidth, boardHeight)) {
            possibleMoves.add(Move.LEFT);
        }
        if (!isPositionSuicidal(request.getSnakes(), headX + 1, headY, boardWidth, boardHeight)) {
            possibleMoves.add(Move.RIGHT);
        }

        String taunt = "Boo!";
        if (possibleMoves.isEmpty()) {
            taunt = "Nooooooo!";
            possibleMoves.add(Move.DOWN);
        }
        
        if(possibleMoves.contains(foodMove)) {
        	move = foodMove;
        } else {
        	move = possibleMoves.get(ThreadLocalRandom.current().nextInt(possibleMoves.size()));
        }

        return new MoveResponse()
                .setMove(move)
                .setTaunt(taunt);
    }

    @RequestMapping(value="/end", method=RequestMethod.POST)
    public Object end() {
        // No response required
        Map<String, Object> responseObject = new HashMap<String, Object>();
        return responseObject;
    }

    private Snake getMySnake(String myId, ArrayList<Snake> snakes) {
        for (Snake snake : snakes) {
            if (snake.getId().equals(myId)) {
                return snake;
            }
        }
        return null;
    }
    
    public Move getFood(int[][] ourCoords, int[][] foodCoords) {
	  if(ourCoords[0][0] > foodCoords[0][0]) return Move.LEFT;
	  if(ourCoords[0][0] < foodCoords[0][0]) return Move.RIGHT;
	  if(ourCoords[0][1] > foodCoords[0][1]) return Move.UP;
	  if(ourCoords[0][1] < foodCoords[0][1]) return Move.DOWN;
  	  return null;
    }

    /**
     * Determines whether the next move will kill us for sure. Does not check for possibility of other snake's head moving
     * into this space.
     *
     * @param newX proposed new X position
     * @param newY proposed new Y position
     * @param boardWidth num of X positions
     * @param boardHeight num of Y positions
     * @return true if the next move would kill us for sure, otherwise false
     */
    private boolean isPositionSuicidal(List<Snake> snakes, int newX, int newY, int boardWidth, int boardHeight) {

        // Do we run into a wall?
        if (newX < 0 || newX == boardWidth) {
            return true;
        }
        if (newY < 0 || newY == boardHeight) {
            return true;
        }

        // Do we run into a snake's existing body segment? Check current position of every segment except
        // for the snake's tail, since it will have moved.
        for (Snake snake : snakes) {
            for (int i = 0; i < snake.getCoords().length - 1; i++) {

                int snakeX = snake.getCoords()[i][0];
                int snakeY = snake.getCoords()[i][1];

                if (newX == snakeX && newY == snakeY) {
                    return true;
                }
            }
        }

        return false;

    }

    public boolean closerToFood(int[][] ourCoords, int[][] otherCoords, int[] foodCoords) {
	  int ourDistance = Math.abs((ourCoords[0][0] - foodCoords[0]) + (ourCoords[0][1] - foodCoords[1]));
	  int theirDistance = Math.abs((otherCoords[0][0] - foodCoords[0]) + (otherCoords[0][1] - foodCoords[1]));
	  
	  if(ourDistance > theirDistance) return false;
	  return true;
    }
  
}
