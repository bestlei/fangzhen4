package skin
{
	import mx.controls.Label;
	import flash.display.Graphics;
	import mx.utils.ColorUtil;
	//自定义样式
	[Style(name="borderColor",type="unit",format="Color",inherit="no")]
	public class LabelBorder extends Label
	{
		public function LabelBorder()
		{
			super();
		}
	}
	
	override protected function updateDisplayList(w:Number,h:Number):void
	{
		super.updateDisplayList(w,h);
		graphics.clear();
		var borderColor:unit=getStyle("borderColor");
		graphics.drawRect(0,0,100,100,borderColor,backgroundAlpha);
	}
}