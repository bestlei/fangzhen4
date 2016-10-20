package common {
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	
	import mx.containers.TitleWindow;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.controls.TextInput;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import twaver.Link;
	import twaver.Node;
	import twaver.network.Network;
	import twaver.network.interaction.CreateLinkInteractionHandler;
	
	public class CustomCreateLinkInteractionHandler extends CreateLinkInteractionHandler {
		private var from:Node, to:Node;
		
		public function CustomCreateLinkInteractionHandler(network:Network, linkClass:Class=null, callback:Function=null) {
			super(network, linkClass, callback);
		}
		
		override protected function createLink():Link {
			this.from = super.fromNode;
			this.to = super.toNode;
			var titleWindow:TitleWindow = new TitleWindow();
			titleWindow.title = "请输入链路信息";
			titleWindow.width = 200;
			titleWindow.height = 300;
			titleWindow.showCloseButton = true;
			titleWindow.addEventListener(CloseEvent.CLOSE, function(evt:CloseEvent):void {
				PopUpManager.removePopUp(titleWindow);
			});
			var labelFrom:Label = new Label();
			labelFrom.text="链路起始节点："+from.name;
			var labelTo:Label = new Label();
			labelTo.text="链路终止节点："+to.name;
			var label1:Label = new Label();
			label1.text="请输入链路名称";
			var textInput1:TextInput = new TextInput();
			textInput1.text="link01";
//			textInput1.addEventListener(KeyboardEvent.KEY_DOWN, function(evt:KeyboardEvent):void {
//				if(evt.keyCode == 13){
//					addLink(textInput.text);
//					PopUpManager.removePopUp(titleWindow);
//				}
//			});
			
			var label2:Label = new Label();
			label2.text="请输入链路带宽";
			var textInput2:TextInput = new TextInput();
			textInput2.text="0";
			
			var bnOk:Button=new Button();
			bnOk.label="确定";
			bnOk.addEventListener(MouseEvent.MOUSE_DOWN, function(evt:MouseEvent):void {
				
					addLink(textInput1.text,textInput2.text);
					PopUpManager.removePopUp(titleWindow);
				
			});
			var bnCancel:Button=new Button();
			bnCancel.label="取消";
			bnCancel.addEventListener(MouseEvent.MOUSE_DOWN, function(evt:MouseEvent):void {
				PopUpManager.removePopUp(titleWindow);
				
			});
//			textInput2.addEventListener(KeyboardEvent.KEY_DOWN, function(evt:KeyboardEvent):void {
//				if(evt.keyCode == 13){
//					addLink(textInput.text);
//					PopUpManager.removePopUp(titleWindow);
//				}
//			});
			titleWindow.addChild(labelFrom);
			titleWindow.addChild(labelTo);
			titleWindow.addChild(label1);
			titleWindow.addChild(textInput1);
			titleWindow.addChild(label2);
			titleWindow.addChild(textInput2);
			titleWindow.addChild(bnOk);
			titleWindow.addChild(bnCancel);
			PopUpManager.addPopUp(titleWindow, network, true);
			PopUpManager.centerPopUp(titleWindow);
			
			return null;
		}
		
		private function addLink(name:String,board:String):void {
			var link:Link = new linkClass(this.from, this.to);
			link.name = name;
			link.parent = this.network.currentSubNetwork;
			link.setClient("board",board);
			this.network.elementBox.add(link);
			this.network.selectionModel.setSelection(link);
			network.setDefaultInteractionHandlers();
		}
	}
}