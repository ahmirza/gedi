package gedi.plot.aesthetics.paint;

import java.awt.Color;
import java.awt.Paint;
import java.util.function.DoubleFunction;
import java.util.function.Function;

import gedi.plot.GPlotContext;
import gedi.plot.GPlotUtils;
import gedi.plot.aesthetics.Aesthetic2;
import gedi.plot.aesthetics.x.XAesthetic;
import gedi.plot.aesthetics.x.XContinuousAesthetic;
import gedi.plot.aesthetics.x.XDiscreteAesthetic;
import gedi.plot.renderables.legend.Legend2;
import gedi.plot.renderables.legend.PaintContinuousLegend;
import gedi.plot.scale.GPlotTicks;
import gedi.plot.scale.transformer.ContinuousContinuousScale;
import gedi.plot.scale.transformer.ContinuousDiscreteScale;
import gedi.plot.scale.transformer.DiscreteInvertibleTransformer;
import gedi.plot.scale.transformer.DoubleInvertibleTransformer;
import gedi.plot.scale.transformer.ToContinuousScale;
import gedi.plot.scale.transformer.ToDiscreteScale;
import gedi.util.FunctorUtils;
import gedi.util.datastructure.dataframe.DataColumn;
import gedi.util.gui.ColorPalettes;

public class PaintContinuousAesthetic extends PaintAesthetic {

	private ContinuousContinuousScale contiScale;
	private Function<GPlotContext,DoubleFunction<Color>> toColor;


	public PaintContinuousAesthetic(DataColumn<?> col) {
		this(col, ContinuousContinuousScale.compute(FunctorUtils.constantFunction(GPlotUtils.inferScaling(col)), col, ctx->DoubleInvertibleTransformer.identity),
			ColorPalettes.Blues.getContinuousMapper(2, -1));
	}

	
	public PaintContinuousAesthetic(DataColumn<?> col, ContinuousContinuousScale contiScale, DoubleFunction<Color> toColor) {
		super(col, null, new PaintContinuousLegend());
		this.contiScale = contiScale;
		this.toColor = ctx->toColor;
		scale = (ctx,c,row)->PaintContinuousAesthetic.this.toColor.apply(ctx).apply(PaintContinuousAesthetic.this.contiScale.transform(ctx, c, row));
		discreteScale();
	}
	
	public PaintContinuousAesthetic discreteScale() {
		toColor = ctx->{
			GPlotTicks ticks = contiScale.getTicks(ctx);
			return ColorPalettes.Blues.getContinuousMapper(ticks.allsubs());
		};
		return this;
	}
	


	public ContinuousContinuousScale getContiScale() {
		return contiScale;
	}
	
	public Function<GPlotContext, DoubleFunction<Color>> getToColor() {
		return toColor;
	}
	

}
