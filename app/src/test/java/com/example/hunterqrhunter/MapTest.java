package com.example.hunterqrhunter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests implementation of method(s) used in QRMapScreen
 *
 */
public class MapTest {

    public MapTest() {
        // This constructor is empty, but it satisfies the requirement of having a public zero-argument constructor.
    }

    /**
     * Tests implementation of mapscore
     *
     */
    @Test
    public void maxscoretest() {
        List<Integer> scoreList = new ArrayList<Integer>();
        scoreList.clear();
        scoreList.add(0);
        assertEquals(0, maxscore(scoreList));
        scoreList.add(1);
        scoreList.add(2);
        scoreList.add(3);
        scoreList.add(4);
        assertEquals(4, maxscore(scoreList));
    }

    /**
     * Returns the highest score in an array
     * @return highestScore, which is the highest score in the List of scores
     * @param scoremax
     */
    public int maxscore(List<Integer> scoremax) {
        int highestScore = 0;
        for(int i=0;i<scoremax.size();i++){
            int num = scoremax.get(i);
            if (num > highestScore) {
                highestScore = i;
            }
        }
        return highestScore;
    }
}