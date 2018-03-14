/**
 * 
 *    Copyright 2017 Florian Erhard
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 */
package gedi.util.algorithm.string.suffixarray;

/**
 * An adapter for constructing suffix arrays on character sequences.
 * 
 * @see SuffixArrays#create(CharSequence)
 * @see SuffixArrays#create(CharSequence, ISuffixArrayBuilder)
 */
final class CharSequenceAdapter
{
    private final ISuffixArrayBuilder delegate;

    /**
     * Last mapped input in {@link #buildSuffixArray(CharSequence)}.
     */
    int [] input;

    /**
     * Construct an adapter with a given underlying suffix array construction strategy.
     * The suffix array builder should accept non-negative characters, with a possibly
     * large alphabet size.
     * 
     * @see DensePositiveDecorator
     */
    public CharSequenceAdapter(ISuffixArrayBuilder builder)
    {
        this.delegate = builder;
    }

    /**
     * Construct a suffix array for a given character sequence.
     */
    public int [] buildSuffixArray(CharSequence sequence)
    {
        /*
         * Allocate slightly more space, some suffix construction strategies need it and
         * we don't want to waste space for multiple symbol mappings.
         */

        this.input = new int [sequence.length() + SuffixArrays.MAX_EXTRA_TRAILING_SPACE];
        for (int i = sequence.length() - 1; i >= 0; i--)
        {
            input[i] = sequence.charAt(i);
        }

        final int start = 0;
        final int length = sequence.length();

        final ISymbolMapper mapper = new DensePositiveMapper(input, start, length);
        mapper.map(input, start, length);

        return delegate.buildSuffixArray(input, start, length);
    }
}
