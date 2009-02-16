function addEvent(elm, evType, fn, useCapture) {
	if (elm == null) {
		return false;
	}
	if (elm.addEventListener) {
		elm.addEventListener(evType, fn, useCapture);
		return true;
	} else {
		if (elm.attachEvent) {
			var r = elm.attachEvent("on" + evType, fn);
			return r;
		} else {
			elm["on" + evType] = fn;
		}
	}
}
function init() {
	var rootul = document.getElementById("menuroot");
	var lis = rootul.getElementsByTagName("li");
	for (var i = 0; i < lis.length; i++) {
		var node = lis[i];
		if (node.nodeName.toLowerCase() == "li") {
			if (node.className == "controlList" || node.className == "controlList2") {
				addEvent(node, "click", getClickFor(node), false);
			} else {
				addEvent(node, "click", cancelBubling, false);
			}
		}
	}
}
addEvent(window, "load", init, false);
function getClickFor(node) {
	return function (e) {
		mclick(e, node);
	};
}
function mclick(e, targetElement) {
	var el = window.event ? targetElement : e ? e.currentTarget : null;
	if (!el) {
		return;
	}
	for (var i = 0; i < el.childNodes.length; i++) {
		var cnode = el.childNodes[i];
		var parentUl = el.parentNode;
		if (cnode.nodeName.toLowerCase() == "ul") {
			if (el.className != "controlList2") {
				if (cnode.style.display != "block") {
					closeAllSideMenu(parentUl);
					cnode.style.display = "block";
					var uls = cnode.getElementsByTagName("ul");
					for (var u = 0; u < uls.length; u++) {
						if (uls[u].className != "sublist_5_control" && uls[u].className != "sublist_3_control") {
							uls[u].style.display = "block";
						}
					}
				} else {
					if (parentUl.nodeName.toLowerCase() == "ul") {
						cnode.style.display = "none";
						var uls = cnode.getElementsByTagName("ul");
						for (var j = 0; j < uls.length; j++) {
							uls[j].style.display = "none";
						}
					}
				}
			} else {
				if (cnode.style.display != "block") {
					cancelBubling(e);
					closeAllSideMenu(parentUl);
					cnode.style.display = "block";
					var uls = cnode.getElementsByTagName("ul");
					for (var u = 0; u < uls.length; u++) {
						uls[u].style.display = "block";
					}
				} else {
					if (parentUl.nodeName.toLowerCase() == "ul") {
						cancelBubling(e);
						cnode.style.display = "none";
						var uls = cnode.getElementsByTagName("ul");
						for (var j = 0; j < uls.length; j++) {
							uls[j].style.display = "none";
						}
					}
				}
			}
		}
		cancelBubling();
	}
}
function closeAllSideMenu(ulroot) {
	var menuuls = ulroot.getElementsByTagName("ul");
	for (var k = 0; k < menuuls.length; k++) {
		menuuls[k].style.display = "none";
	}
}
function cancelBubling(e) {
	if (window.event) {
		window.event.cancelBubble = true;
	}
	if (e && e.stopPropagation && e.preventDefault) {
		e.stopPropagation();
	}
}