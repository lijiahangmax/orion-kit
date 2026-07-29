package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StacksTest {

    @Test
    public void testCurrentStacks() {
        List<Stacks.StackTrace> stacks = Stacks.currentStacks();
        assertNotNull(stacks);
        assertFalse(stacks.isEmpty());
    }

    @Test
    public void testCurrentStackByIndex() {
        Stacks.StackTrace stack = Stacks.currentStack(0);
        assertNotNull(stack);
        assertNotNull(stack.getClassName());
        assertNotNull(stack.getMethodName());
    }

    @Test
    public void testCurrentClass() {
        String className = Stacks.currentClass();
        assertNotNull(className);
    }

    @Test
    public void testCurrentMethod() {
        String method = Stacks.currentMethod();
        assertNotNull(method);
    }

    @Test
    public void testCurrentLineNumber() {
        int lineNumber = Stacks.currentLineNumber();
        // line number should be positive or -1 (if not available)
        assertTrue(lineNumber != 0);
    }

    @Test
    public void testToStackTrace() {
        StackTraceElement element = new Exception().getStackTrace()[0];
        Stacks.StackTrace trace = Stacks.toStackTrace(element);
        assertNotNull(trace);
        assertNotNull(trace.getClassName());
        assertNotNull(trace.getMethodName());
        assertNotNull(trace.getFileName());
    }

    @Test
    public void testToStackTraces() {
        StackTraceElement[] elements = new Exception().getStackTrace();
        List<Stacks.StackTrace> traces = Stacks.toStackTraces(elements);
        assertNotNull(traces);
        assertFalse(traces.isEmpty());
    }
}
