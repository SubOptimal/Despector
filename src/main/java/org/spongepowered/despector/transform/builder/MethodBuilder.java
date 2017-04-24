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
package org.spongepowered.despector.transform.builder;

import org.spongepowered.despector.ast.SourceSet;
import org.spongepowered.despector.ast.type.MethodEntry;

import java.util.function.Consumer;

public class MethodBuilder {

    private Consumer<MethodEntry> callback;
    private String name;

    public MethodBuilder() {

        reset();
    }

    MethodBuilder(Consumer<MethodEntry> callback) {
        this();
        this.callback = callback;
    }

    public MethodBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MethodBuilder reset() {
        this.name = null;
        return this;
    }

    public MethodEntry build(SourceSet set) {
        MethodEntry mth = new MethodEntry(set);
        mth.setName(this.name);

        if (this.callback != null) {
            this.callback.accept(mth);
        }
        return mth;
    }

}
