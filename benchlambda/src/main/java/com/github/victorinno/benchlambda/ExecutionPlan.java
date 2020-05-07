package com.github.victorinno.benchlambda;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({ "10", "100", "1000", "10000", "100000", "1000000" })
    public long iterations;

}