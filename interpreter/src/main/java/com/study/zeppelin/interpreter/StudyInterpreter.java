package com.study.zeppelin.interpreter;

import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by dltkr on 2017-01-14.
 */
public class StudyInterpreter extends Interpreter {
    static {
        Interpreter.register("study", "study", StudyInterpreter.class.getName());
    }
    Logger logger = LoggerFactory.getLogger(StudyInterpreter.class);

    public StudyInterpreter(Properties properties) {
        super(properties);
        logger.info("Interpreter is initialized");
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public InterpreterResult interpret(String s, InterpreterContext interpreterContext) {
        return null;
    }

    @Override
    public void cancel(InterpreterContext interpreterContext) {

    }

    @Override
    public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override
    public int getProgress(InterpreterContext interpreterContext) {
        return 0;
    }
}
