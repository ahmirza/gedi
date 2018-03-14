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
package gedi.core.data.mapper;

import java.util.function.DoubleBinaryOperator;

import gedi.core.data.numeric.DenseGenomicNumericProvider;
import gedi.core.data.numeric.GenomicNumericProvider;
import gedi.core.reference.ReferenceSequence;
import gedi.core.region.GenomicRegion;
import gedi.gui.genovis.pixelMapping.PixelLocationMapping;
import gedi.util.datastructure.array.NumericArray;
import gedi.util.datastructure.array.NumericArray.NumericArrayType;
import gedi.util.mutable.MutablePair;

@GenomicRegionDataMapping(fromType={GenomicNumericProvider.class,GenomicNumericProvider.class},toType=GenomicNumericProvider.class)
public class GenomicNumericComputingMapper implements GenomicRegionDataMapper<MutablePair<GenomicNumericProvider,GenomicNumericProvider>, GenomicNumericProvider>{

	private DoubleBinaryOperator fun;
	
	public GenomicNumericComputingMapper() {
		this((a,b)->a+b);
	}


	public GenomicNumericComputingMapper(DoubleBinaryOperator fun) {
		this.fun = fun;
	}

	@Override
	public GenomicNumericProvider map(ReferenceSequence reference,
			GenomicRegion region,PixelLocationMapping pixelMapping,
			MutablePair<GenomicNumericProvider,GenomicNumericProvider> data) {
		
		if (data.Item1.getNumDataRows()!=data.Item2.getNumDataRows()) throw new RuntimeException("Input for computation not compatible!");
		
		NumericArray[] re = new NumericArray[data.Item1.getNumDataRows()];
		for (int i=0; i<re.length; i++) {
			re[i] = NumericArray.createMemory(region.getTotalLength(), NumericArrayType.Double);
			double[] val = new double[region.getTotalLength()];
			for (int p=0; p<val.length; p++)
				val[p] = fun.applyAsDouble(data.Item1.getValue(reference,region.map(p),i), data.Item2.getValue(reference,region.map(p),i));
			
		}
		return  new DenseGenomicNumericProvider(reference,region,re);
	}


	
}
