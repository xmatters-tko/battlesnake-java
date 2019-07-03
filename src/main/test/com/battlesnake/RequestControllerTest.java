package com.battlesnake;

import com.battlesnake.RequestController;

import org.junit.Assert;
import org.junit.Test;

import com.battlesnake.data.*;
public class RequestControllerTest {
    RequestController rc = new RequestController();
    
    @Test
    public void testDeterminePreviousMove() {
        int [] head = new int [] { 1, 1 };
        Assert.assertEquals(Move.DOWN, rc.determinePreviousMove(head, new int [] { 1, 0 }));
        Assert.assertEquals(Move.UP, rc.determinePreviousMove(head, new int [] { 1, 2 }));
        Assert.assertEquals(Move.RIGHT, rc.determinePreviousMove(head, new int [] { 0, 1 }));       
        Assert.assertEquals(Move.LEFT, rc.determinePreviousMove(head, new int [] { 2, 1 }));
        Assert.assertNull(rc.determinePreviousMove(head, new int [] { 2, 2 }));
    }
}
