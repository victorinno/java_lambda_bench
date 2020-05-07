/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.victorinno.benchlambda;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@BenchmarkMode(Mode.All)
public class MyBenchmark {

    @State(Scope.Benchmark)
    public class ExecutionPlan {

        @Param({ "10", "100", "1000", "10000", "100000", "1000000" })
        public long iterations;

    }

    public static BinaryOperator<Integer> REDUCE = (a, b) -> a + b;
    public static Function<Integer, Integer> MAP = f -> f + 10;
    public static UnaryOperator<Integer> ITERATOR = f -> f + 1;

    private void normalLambda(final long times) {
        Stream.iterate(0, f -> f + 1).limit(times).map(f -> f + 10).reduce(0, (a, b) -> a + b);
    }

    private void parametersLambda(final long times, final UnaryOperator<Integer> iterator,
            final Function<Integer, Integer> map, final BinaryOperator<Integer> reduce) {
        Stream.iterate(0, iterator).limit(times).map(map).reduce(0, reduce);
    }

    private void constantLambda(final long times) {
        Stream.iterate(0, ITERATOR).limit(times).map(MAP).reduce(0, REDUCE);
    }

    @Benchmark
    public void testNormal(final ExecutionPlan plan) {
        normalLambda(plan.iterations);
    }

    @Benchmark
    public void testParameters(final ExecutionPlan plan) {
        parametersLambda(plan.iterations, f -> f + 1, f -> f + 10, (a, b) -> a + b);
    }

    @Benchmark
    public void testConstant(final ExecutionPlan plan) {
        constantLambda(plan.iterations);
    }
}
