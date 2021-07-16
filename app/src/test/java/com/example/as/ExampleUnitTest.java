package com.example.as;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test(){
        String str = "    ha      sa     ha   ha";
        String s = str.replaceAll("\\s+"," ").trim();
        String[] j = s.split(" ");
        System.out.println(s);
        System.out.println(j[1]);
    }
}