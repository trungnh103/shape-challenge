package com.trungnh103.shapeChallenge.common;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathExpressionEvaluator {

    public static String evalFormula(String areaFormula) {
        System.out.println("area: " + areaFormula);
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String result = "";
        try {
            result = engine.eval(areaFormula).toString();
        } catch (ScriptException ex) {
            return "";
        }
        return result;
    }

    public static boolean evalCondition(String conditionStr) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        boolean result = false;
        try {
            result = (boolean) engine.eval(conditionStr);
        } catch (ScriptException ex) {
            result = false;
        }
        return result;
    }
}
