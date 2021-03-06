package skin 
{
	import mx.skins.ProgrammaticSkin;
	import flash.display.Graphics;
	import mx.utils.ColorUtil;
	
	public class MenuBarActiveSkin extends ProgrammaticSkin
	{
		public function MenuBarActiveSkin()
		{
			super();
		}
		override protected function updateDisplayList(w:Number, h:Number):void
		{
			var backgroundAlpha:Number = getStyle("backgroundAlpha");
			var rollOverColor:uint=0x417eb7;
			graphics.clear();
			drawRoundRect(0,0,w,h,5,rollOverColor,backgroundAlpha);
		}
		
	}
}