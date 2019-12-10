package com.trungnh103.shapeChallenge.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathExpressionEvaluatorTest {
    @Test
    public void test_evalFormula() {
        String plus = "5+9";
        String minus = "100-1";
        String multiply = "9*3";
        String divide = "56/8";
        String plusResult = MathExpressionEvaluator.evalFormula(plus);
        String minusResult = MathExpressionEvaluator.evalFormula(minus);
        String multiplyResult = MathExpressionEvaluator.evalFormula(multiply);
        String divideResult = MathExpressionEvaluator.evalFormula(divide);
        assertEquals(plusResult, "14");
        assertEquals(minusResult, "99");
        assertEquals(multiplyResult, "27");
        assertEquals(divideResult, "7");

    }

    @Test
    public void test_evalCondition() {
        String greaterThan = "100>99";
        String equal = "70==70";
        String wrong = "8>=16";
        assertEquals(MathExpressionEvaluator.evalCondition(greaterThan), true);
        assertEquals(MathExpressionEvaluator.evalCondition(equal), true);
        assertEquals(MathExpressionEvaluator.evalCondition(wrong), false);
    }
}
