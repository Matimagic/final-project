package com.finalproject.millionairesapplication;

import com.finalproject.millionairesapplication.controller.QuizController;
import com.finalproject.millionairesapplication.controller.QuizRestController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DifficultyValidatorTest {
    @Test
    public void testAll(){
        Assert.assertEquals("medium", QuizRestController.checkDifficulty("medium"));
        Assert.assertEquals("easy", QuizRestController.checkDifficulty("easy"));
        Assert.assertEquals("hard", QuizRestController.checkDifficulty("hard"));
        Assert.assertEquals("easy", QuizRestController.checkDifficulty("Hard"));
        Assert.assertEquals("easy", QuizRestController.checkDifficulty("meDium"));
        Assert.assertEquals("easy", QuizRestController.checkDifficulty("easY"));
        Assert.assertEquals("easy", QuizRestController.checkDifficulty(""));
        Assert.assertEquals("easy", QuizRestController.checkDifficulty(null));
    }
}
