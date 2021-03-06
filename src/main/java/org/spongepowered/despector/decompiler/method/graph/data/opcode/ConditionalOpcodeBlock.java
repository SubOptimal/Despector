/*
 * The MIT License (MIT)
 *
 * Copyright (c) Despector <https://despector.voxelgenesis.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.despector.decompiler.method.graph.data.opcode;

import org.spongepowered.despector.decompiler.method.graph.data.block.BlockSection;

/**
 * An opcode block holding a conditional jump.
 */
public class ConditionalOpcodeBlock extends OpcodeBlock {

    private OpcodeBlock else_target;
    private OpcodeBlock prefix;

    public ConditionalOpcodeBlock(int start, int end) {
        super(start, end);
    }

    /**
     * Gets the alternate target of this block. Only present for conditional
     * jumps where it represents the block to which control is passed if the
     * condition is false.
     */
    public OpcodeBlock getElseTarget() {
        return this.else_target;
    }

    /**
     * Has an alternate target.
     */
    public boolean hasElseTarget() {
        return this.else_target != null;
    }

    /**
     * Sets the alternate target.
     */
    public void setElseTarget(OpcodeBlock block) {
        this.else_target = block;
    }

    public OpcodeBlock getPrefix() {
        return this.prefix;
    }

    public void setPrefix(OpcodeBlock block) {
        this.prefix = block;
    }

    @Override
    public BlockSection toBlockSection() {
        throw new IllegalStateException("Unexpected conditional block");
    }

    @Override
    public void print() {
        super.print();
        System.out.println("    Target: " + (this.target != null ? this.target.getStart() : -1));
        System.out.println("    Else Target: " + (this.else_target != null ? this.else_target.getStart() : -1));
    }

    @Override
    public String getDebugHeader() {
        return "Conditional: " + this.start_pc + "-" + this.end_pc + " (target: " + (this.target != null ? this.target.getStart() : -1) + ", else_target: "
                + (this.else_target != null ? this.else_target.getEnd() : -1) + ")";
    }
}
