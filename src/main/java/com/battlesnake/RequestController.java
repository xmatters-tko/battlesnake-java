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

@RestController
public class RequestController {

    @RequestMapping(value="/start", method=RequestMethod.POST, produces="application/json")
    public StartResponse start(@RequestBody StartRequest request) {
        return new StartResponse()
                .setName("Simple Snake")
                .setColor("#FF3400")
                .setHeadUrl("http://vignette1.wikia.nocookie.net/nintendo/images/6/61/Bowser_Icon.png/revision/latest?cb=20120820000805&path-prefix=en")
                .setHeadType(HeadType.DEAD)
                .setTailType(TailType.PIXEL)
                .setTaunt("I won't run into walls!");
    }

    @RequestMapping(value="/move", method=RequestMethod.POST, produces = "application/json")
    public MoveResponse move(@RequestBody MoveRequest request) {
        MoveResponse moveResponse = new MoveResponse();
        Snake mySnake = findOurSnake(request); // kind of handy to have our snake at this level

        // determine what moves we can make without dying
        List<Move> possibleMoves = findSafeMoves(request, mySnake.getCoords()[0], mySnake.getCoords()[1]);

        // apply strategy to pick what direction to go
        if (!possibleMoves.isEmpty()) {
            // Improve on this strategy
            Move previousDirection = determinePreviousMove(mySnake.getCoords()[0], mySnake.getCoords()[1]);
            if (possibleMoves.contains(previousDirection)) {
                return moveResponse.setMove(previousDirection).setTaunt("I remembered which way I was going");
            } else {
                return moveResponse.setMove(possibleMoves.get(0)).setTaunt("Time to go another direction");
            }
        } else {
            return moveResponse.setMove(Move.DOWN).setTaunt("Oh Drat!");
        }
    }

    @RequestMapping(value="/end", method=RequestMethod.POST)
    public Object end() {
        // No response required
        Map<String, Object> responseObject = new HashMap<String, Object>();
        return responseObject;
    }

    /*
     *  Go through the snakes and find your team's snake
     *  
     *  @param  request The MoveRequest from the server
     *  @return         Your team's snake
     */
    private Snake findOurSnake(MoveRequest request) {
        String myUuid = request.getYou();
        List<Snake> snakes = request.getSnakes();
        return snakes.stream().filter(thisSnake -> thisSnake.getId().equals(myUuid)).findFirst().orElse(null);
    }

    /*
     *  Evaluate each direction and determine if it is a safe move
     *  
     *  @param  request The MoveRequest from the server
     *  @param  head    The point of our snake's head
     *  @param  neck    The point of our snake's neck (where we just came from)
     *  @return         A list of safe moves
     */
    public ArrayList<Move> findSafeMoves(MoveRequest request, int[] head, int[] neck) {
        ArrayList<Move> safeMoves = new ArrayList<>();

        // analyze right
        int[] right = new int[] {head[0] + 1, head[1]};
        if (analyzePossibleMove(request, head, neck, right)) {
            safeMoves.add(Move.RIGHT);
        }

        // analyze up
        int[] up = new int[] { head[0], head[1] - 1 };
        if (analyzePossibleMove(request, head, neck, up)) {
            safeMoves.add(Move.UP);
        }

        // analyze left
        int[] left = new int[] { head[0] - 1, head[1] };
        if (analyzePossibleMove(request, head, neck, left)) {
            safeMoves.add(Move.LEFT);
        }

        // analyze down
        int[] down = new int[] { head[0], head[1] + 1 };
        if (analyzePossibleMove(request, head, neck, down)) {
            safeMoves.add(Move.DOWN);
        }

        return safeMoves;
    }

    /*
     *  Analyze a possible move and determine if it is safe
     *  
     *  @param  request         The MoveRequest from the server
     *  @param  head            The point of our snake's head
     *  @param  neck            The point of our snake's neck (where we just came from)
     *  @param  possibleMove    The point we are considering moving to
     *  @return                 true if the move is safe, otherwise false
     */
    public boolean analyzePossibleMove(MoveRequest request, int[] head, int[] neck, int[] possibleMove) {
        if (coordinatesEquals(neck, possibleMove)) {
            System.out.println("don't go backwards");
            return false;
        }

        // don't hit the walls
        if (possibleMove[0] < 0 || possibleMove[0] >= request.getWidth() || 
                possibleMove[1] < 0 || possibleMove[1] >= request.getHeight()) {
            System.out.println("don't hit the wall");
            return false;
        }

        // what else can we check here
        
        return true;
    }
    
    /*
     *  Based on the location of the snake's head and neck, determine what move you just made
     *  
     *  @param  head            The point of our snake's head
     *  @param  neck            The point of our snake's neck (where we just came from)
     *  @return                 The Move that you chose last turn
     */
    public Move determinePreviousMove(int[] head, int[] neck) {
        if (coordinatesEquals(new int[] { head[0], head[1] - 1 }, neck)) {
            return Move.DOWN;
        } else if (coordinatesEquals(new int[] { head[0], head[1] + 1 }, neck)) {
            return Move.UP;
        }  else if (coordinatesEquals(new int[] { head[0] - 1, head[1]}, neck)) {
            return Move.RIGHT;
        }  else if (coordinatesEquals(new int[] { head[0] + 1, head[1] }, neck)) {
            return Move.LEFT;
        }
        return null;
    }

    // Yes, I know this could be more terse
    public boolean coordinatesEquals(int[] oneArray, int[] secondArray) {
        if (oneArray.length != secondArray.length) {
            return false;
        }

        for (int x = 0; x < oneArray.length; x++) {
            if (oneArray[x] != secondArray[x]) {
                return false;
            }
        }

        return true;
    }  

}
