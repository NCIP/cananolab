function changeMenuStyle(obj, new_style) { 
  obj.className = new_style; 
}

function showCursor(){
	document.body.style.cursor='hand'
}

function hideCursor(){
	document.body.style.cursor='default'
}

function confirmDelete(){
  if (confirm('Are you sure you want to delete?')){
    return true;
    }else{
    return false;
  }
}

function get_tree()	{

		d = new dTree('d');
		
		d.config.target="";
		
		d.add(0,-1,'Workflow');
		d.add(1,0,'Pre-screening Assays','javascript:add_hist()', '', '', '');
		d.add(2,1,'STE-1','javascript:add_hist()', '', '', '');
		d.add(3,2,'run 1','javascript:add_hist()');
		d.add(4,3,'In','javascript:add_hist()');
		d.add(5,4,'NCL6-7105-1','javascript:gotoPage(\'viewAliquot.html\')', '', '', '');
		d.add(6,4,'NCL6-7105-2','javascript:add_hist()');
		d.add(7,3,'Out','javascript:add_hist()');
		d.add(8,7,'NCL6.vaf','javascript:add_hist()');
		d.add(9,2,'run 2','javascript:add_hist()');
		d.add(10,9,'In','javascript:add_hist()');
		d.add(11,9,'Out','javascript:add_hist()');
		d.add(12,1,'STE-2','javascript:add_hist()');
		d.add(13,1,'STE-3','javascript:add_hist()', '', '', '');
		d.add(14,1,'PCC-1','javascript:add_hist()', '', '', '');
		d.add(15,0,'In Vitro Assays','javascript:add_hist()', '', '', '');
		d.add(16,0,'In Vivo Assays','javascript:add_hist()', '', '', '');
		
		document.write(d);


}

function add_hist()	{
	//alert("added hist");
	document.getElementById('hists').options[document.getElementById('hists').options.length] = new Option("new text", "new value");
}

function gotoPage(pageURL)	{
	window.location.href=pageURL;
}


