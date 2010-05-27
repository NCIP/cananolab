/* 
 * for Add File link on Composition Nanomaterial Entity page
 */
function addFileClone() {
	var pattern = document.getElementById("filePatternDiv");
	var fileCount = document.getElementById("fileCount").firstChild.nodeValue;
	fileCount++;
	document.getElementById("fileCount").firstChild.nodeValue = fileCount;
	var clone = pattern.cloneNode(true);
	clone.setAttribute("id", "filePattern" + fileCount);
	clone.style.display = "block";
	
	//set File Name
	var fileNameEle = clone.getElementsByTagName("span")[0];
	fileNameEle.removeAttribute("id");
	fileNameEle.firstChild.nodeValue = "File #" + fileCount;
	
	//set File Type
	var fileTypeEle = clone.getElementsByTagName("select")[0];
	fileTypeEle.setAttribute("name", "fileType" + fileCount);
	fileTypeEle.setAttribute("id", "fileType" + fileCount);
	var linkEle = clone.getElementsByTagName("a")[0];
	var fileTd = document.getElementById("fileTd");
	fileTd.appendChild(clone);
	addEvent(linkEle, "click", removeFileTable, false);
	addEvent(fileTypeEle, "change", addNewFileTypeOption, false);
}
function addNewFileTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var optionName = "File Type";
		var selectId = selectEle.getAttribute("id");
		prompt2("New " + optionName + ":", "myfunction", selectId);
	}
}
function removeFileTable(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	while (el.nodeName.toLowerCase() != "div") {
		el = el.parentNode;
	}
	var parentEle = el.parentNode;
	parentEle.removeChild(el);
	var fileCount = document.getElementById("fileCount").firstChild.nodeValue;
	fileCount--;
	document.getElementById("fileCount").firstChild.nodeValue = fileCount;
}