/*
 * Copyright (c) 2019 Faiz & Siegeln Software GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * The Software shall be used for Good, not Evil.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.faizsiegeln.njams.messageformat.v4.command.wrapper.response;

import com.faizsiegeln.njams.messageformat.v4.command.Command;
import com.faizsiegeln.njams.messageformat.v4.command.Instruction;
import com.faizsiegeln.njams.messageformat.v4.command.wrapper.InstructionMapper;
import com.faizsiegeln.njams.messageformat.v4.command.wrapper.NjamsMessageFormatException;
import com.faizsiegeln.njams.messageformat.v4.projectmessage.LogLevel;
import com.faizsiegeln.njams.messageformat.v4.projectmessage.LogMode;

import java.time.LocalDateTime;

import static com.faizsiegeln.njams.messageformat.v4.command.wrapper.InstructionConstants.*;

public class GetTracingResponse extends AbstractResponse {

    public static final Command COMMAND_FOR_THIS_CLASS = Command.GET_TRACING;

    public GetTracingResponse(Instruction instructionToWriteTo, int resultCode, String resultMessage,
            LocalDateTime startTime, LocalDateTime endTime, int iterations, boolean isDeepTrace)
            throws NjamsMessageFormatException {
        this(instructionToWriteTo, resultCode, resultMessage);

        this.instructionToAdapt.setResponseParameter(START_TIME_KEY,
                InstructionMapper.InstructionSerializer.serializeLocalDateTime(startTime));
        this.instructionToAdapt.setResponseParameter(END_TIME_KEY,
                InstructionMapper.InstructionSerializer.serializeLocalDateTime(endTime));
        this.instructionToAdapt.setResponseParameter(ITERATIONS_KEY,
                InstructionMapper.InstructionSerializer.serializeInteger(iterations));
        this.instructionToAdapt.setResponseParameter(DEEP_TRACE_KEY,
                InstructionMapper.InstructionSerializer.serializeBoolean(isDeepTrace));
    }

    public GetTracingResponse(Instruction instructionToWriteTo, int resultCode, String resultMessage)
            throws NjamsMessageFormatException {
        super(instructionToWriteTo, resultCode, resultMessage);
        validateCommand();
    }

    private void validateCommand() throws NjamsMessageFormatException {
        if (!instructionToAdapt.getCommand().equals(COMMAND_FOR_THIS_CLASS.commandString())) {
            throw new NjamsMessageFormatException(
                    "Command " + instructionToAdapt.getCommand() + " is not suitable for " + this.getClass());
        }
    }

    public GetTracingResponse(Instruction instructionToReadFrom) throws NjamsMessageFormatException {
        super(instructionToReadFrom);
        validateCommand();
    }

    public LogLevel getLogLevel() throws NjamsMessageFormatException {
        return InstructionMapper.InstructionParser
                .parseLogLevel(instructionToAdapt.getResponseParameterByName(LOG_LEVEL_KEY));
    }

    public boolean isExcluded() throws NjamsMessageFormatException {
        return InstructionMapper.InstructionParser
                .parseBoolean(instructionToAdapt.getResponseParameterByName(EXCLUDE_KEY));
    }

    public LogMode getLogMode() throws NjamsMessageFormatException {
        return InstructionMapper.InstructionParser
                .parseLogMode(instructionToAdapt.getResponseParameterByName(LOG_MODE_KEY));
    }
}