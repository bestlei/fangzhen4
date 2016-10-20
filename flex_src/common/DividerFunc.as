package common
{
	import flash.events.MouseEvent;
	
	import mx.containers.HDividedBox;
	
	public class DividerFunc
	{
		private var dividerX:int;
		private var divide:Boolean = true;
		public function DividerFunc()
		{
		}
		public function dividerDoubleClick(hdb:HDividedBox):void{
			hdb.liveDragging = true;
			hdb.getDividerAt(0).doubleClickEnabled = true;
//			hdb.toolTip = "双击此处，展开或关闭左侧面板";
			hdb.getDividerAt(0).addEventListener(MouseEvent.DOUBLE_CLICK, function():void{
				if(divide){
					dividerX = hdb.getDividerAt(0).x;				
				}
				divide = !divide;
				hdb.getDividerAt(0).x == 2 ? hdb.getDividerAt(0).x = dividerX : hdb.getDividerAt(0).x = 2;
			});
		}
	}
}