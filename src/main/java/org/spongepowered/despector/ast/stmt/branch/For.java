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
package org.spongepowered.despector.ast.stmt.branch;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.despector.ast.AstVisitor;
import org.spongepowered.despector.ast.insn.condition.Condition;
import org.spongepowered.despector.ast.stmt.Statement;
import org.spongepowered.despector.ast.stmt.StatementBlock;
import org.spongepowered.despector.ast.stmt.StatementVisitor;
import org.spongepowered.despector.ast.stmt.branch.Break.Breakable;
import org.spongepowered.despector.util.serialization.AstSerializer;
import org.spongepowered.despector.util.serialization.MessagePacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A for loop.
 */
public class For implements Statement, Breakable {

    private Statement init;
    private Condition condition;
    private Statement incr;
    private StatementBlock body;
    private List<Break> breaks = new ArrayList<>();

    public For(@Nullable Statement init, Condition condition, @Nullable Statement incr, StatementBlock body) {
        this.init = init;
        this.condition = checkNotNull(condition, "condition");
        this.incr = incr;
        this.body = checkNotNull(body, "body");
    }

    /**
     * Gets the initialization statement, may be null.
     */
    @Nullable
    public Statement getInit() {
        return this.init;
    }

    /**
     * Sets the initialization statement, may be null.
     */
    public void setInit(@Nullable Statement init) {
        this.init = init;
    }

    /**
     * Gets the loop condition.
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * Sets the loop condition.
     */
    public void setCondition(Condition condition) {
        this.condition = checkNotNull(condition, "condition");
    }

    /**
     * Gets the incrementing statement, may be null.
     */
    @Nullable
    public Statement getIncr() {
        return this.incr;
    }

    /**
     * Sets the incrementing statement, may be null.
     */
    public void setIncr(@Nullable Statement incr) {
        this.incr = incr;
    }

    /**
     * Gets the loop body.
     */
    public StatementBlock getBody() {
        return this.body;
    }

    /**
     * Sets the loop body.
     */
    public void setBody(StatementBlock block) {
        this.body = checkNotNull(block, "block");
    }

    @Override
    public List<Break> getBreaks() {
        return this.breaks;
    }

    @Override
    public void accept(AstVisitor visitor) {
        if (visitor instanceof StatementVisitor) {
            ((StatementVisitor) visitor).visitFor(this);
        }
        if (this.init != null) {
            this.init.accept(visitor);
        }
        this.condition.accept(visitor);
        if (this.incr != null) {
            this.incr.accept(visitor);
        }
        for (Statement stmt : this.body.getStatements()) {
            stmt.accept(visitor);
        }
    }

    @Override
    public void writeTo(MessagePacker pack) throws IOException {
        pack.startMap(6);
        pack.writeString("id").writeInt(AstSerializer.STATEMENT_ID_FOR);
        pack.writeString("init");
        if (this.init != null) {
            this.init.writeTo(pack);
        } else {
            pack.writeNil();
        }
        pack.writeString("condition");
        this.condition.writeTo(pack);
        pack.writeString("incr");
        if (this.incr != null) {
            this.incr.writeTo(pack);
        } else {
            pack.writeNil();
        }
        pack.writeString("breakpoints").startArray(this.breaks.size());
        for (Break br : this.breaks) {
            pack.writeInt(((Object) br).hashCode());
        }
        pack.endArray();
        pack.writeString("body");
        pack.startArray(this.body.getStatementCount());
        for (Statement stmt : this.body.getStatements()) {
            stmt.writeTo(pack);
        }
        pack.endArray();
        pack.endMap();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("for (");
        if (this.init != null) {
            sb.append(this.init);
        }
        sb.append(";");
        sb.append(this.condition);
        sb.append(";");
        if (this.incr != null) {
            sb.append(this.incr);
        }
        sb.append(") {\n");
        for (Statement insn : this.body.getStatements()) {
            sb.append("    ").append(insn).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof For)) {
            return false;
        }
        For insn = (For) obj;
        if (this.init != null) {
            if (!this.init.equals(insn.init)) {
                return false;
            }
        } else if (insn.init != null) {
            return false;
        }
        if (this.incr != null) {
            if (!this.incr.equals(insn.incr)) {
                return false;
            }
        } else if (insn.incr != null) {
            return false;
        }
        return this.condition.equals(insn.condition) && this.body.equals(insn.body);
    }

    @Override
    public int hashCode() {
        int h = 1;
        h = h * 37 + (this.init == null ? 0 : this.init.hashCode());
        h = h * 37 + (this.incr == null ? 0 : this.incr.hashCode());
        h = h * 37 + this.condition.hashCode();
        h = h * 37 + this.body.hashCode();
        return h;
    }

}
