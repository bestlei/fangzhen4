package skin
{
	import mx.skins.ProgrammaticSkin;
	import flash.display.Graphics;
	
	public class MenuBarNoBorderFillSkin extends ProgrammaticSkin
	{
		public function MenuBarNoBorderFillSkin()
		{
			super();
		}
		override protected function updateDisplayList(w:Number, h:Number):void
		{
			var backgroundAlpha:Number = getStyle("backgroundAlpha");
			var rollOverColor:uint=0xf1f1f1;
			graphics.clear();
			drawRoundRect(0,0,w,h,5,rollOverColor,backgroundAlpha);
		}
	}
	
}