/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.functions;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Provides a Random function which returns a random long integer between a min
 * (first argument) and a max (second argument).
 *
 */
public class Random extends AbstractFunction implements Serializable {

    private static final long serialVersionUID = 232L;

    private static final List desc = new LinkedList();

    private static final String KEY = "__Random"; //$NON-NLS-1$

    static {
        desc.add(JMeterUtils.getResString("minimum_param")); //$NON-NLS-1$
        desc.add(JMeterUtils.getResString("maximum_param")); //$NON-NLS-1$
        desc.add(JMeterUtils.getResString("function_name_paropt")); //$NON-NLS-1$
    }

    private transient CompoundVariable varName, minimum, maximum;

    /**
     * No-arg constructor.
     */
    public Random() {
    }

    /**
     * Execute the function.
     *
     * @see Function#execute(SampleResult, Sampler)
     */
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {


        long min = Long.parseLong(minimum.execute().trim());
        long max = Long.parseLong(maximum.execute().trim());

        long rand = min + (long) (Math.random() * (max - min + 1));

        String randString = Long.toString(rand);

        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars != null && varTrim.length() > 0){// vars will be null on TestPlan
                vars.put(varTrim, randString);
            }
        }

        return randString;

    }

    /**
     * Set the parameters for the function.
     *
     * @see Function#setParameters(Collection)
     */
    public synchronized void setParameters(Collection parameters) throws InvalidVariableException {
        checkParameterCount(parameters, 2, 3);
        Object[] values = parameters.toArray();

        minimum = (CompoundVariable) values[0];
        maximum = (CompoundVariable) values[1];
        if (values.length>2){
            varName = (CompoundVariable) values[2];
        } else {
            varName = null;
        }

    }

    /**
     * Get the invocation key for this function.
     *
     * @see Function#getReferenceKey()
     */
    public String getReferenceKey() {
        return KEY;
    }

    /**
     * Get the description of this function.
     *
     * @see Function#getArgumentDesc()
     */
    public List getArgumentDesc() {
        return desc;
    }

}
