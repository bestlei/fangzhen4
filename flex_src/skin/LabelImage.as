package skin
{
	import flash.display.Loader;
	import flash.net.URLLoader;
	import flash.net.URLrequest;
	
	import mx.controls.Label;

	//自定义背景属性
	[Style(name="imgPath",type="String",inherit="no")]
	public class LabelImage extends Label
	{
		private var_loader:Loader = ner Loader();
		public function LabelImage()
		{
			super();
		}
		override protected function updateDisplayList(w:Number,h:Number):void
		{
			super.updateDisplayList(w,h);
			_loader.load(new URLRequest(getStyle('imgPath')));
			addChild(_loader);
			_loader.x= -15;
			setChildIndex(_loader,0);
		}
	}
}