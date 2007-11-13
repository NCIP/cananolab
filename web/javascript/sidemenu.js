function addEvent(elm, evType, fn, useCapture) {
  if (elm.addEventListener) {
    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent) {
    var r = elm.attachEvent('on' + evType, fn);
    return r;
  } else {
    elm['on' + evType] = fn;
  }
}

function init() {
  var rootul = document.getElementById('menuroot');
  var lis = rootul.getElementsByTagName('li');
    for (var i = 0; i < lis.length; i++) {
      var node = lis[i];

      if(node.nodeName.toLowerCase() == 'li' &&
         node.getElementsByTagName('ul').length > 0) {
         	
         	if(node.className == 'toplist') {
        		addEvent(node, 'click', getClickFor(node), false);
        		// node.getElementsByTagName('a')[0].className += ' subheader';
        	} else {
        		addEvent(node, 'click', cancelBubling, false);
        	}
      }
   }
}

addEvent(window, 'load', init, false);

function getClickFor(node) {
  return function(e) { mclick(e, node); };
}

function mclick(e, targetElement) {
  var el = window.event ? targetElement : e ? e.currentTarget : null;
  if (!el) return;
  
  for (var i = 0; i < el.childNodes.length; i++) {
    var cnode = el.childNodes[i];
    
    var parentUl = el.parentNode;
    if(cnode.nodeName.toLowerCase() == 'ul' ) {
        if( cnode.style.display != 'block' ) {
  			closeAllSideMenu();
     		cnode.style.display = 'block';
     		var uls = cnode.getElementsByTagName('ul');
  			for (var u = 0; u < uls.length; u++) {
     			uls[u].style.display = 'block';
     		}

    	} else if(parentUl.nodeName.toLowerCase() == 'ul' 
    		/* && parentUl.className == 'slidingmenu' */ ) {
    		
    		cnode.style.display = 'none';
    		var uls = cnode.getElementsByTagName('ul');
     		for (var j = 0; j < uls.length; j++) {
     			uls[j].style.display = 'none';
     		}
    	}
    }
    if (window.event) {
      window.event.cancelBubble = true;
    }
    if (e && e.stopPropagation && e.preventDefault) {
      e.stopPropagation();
    }
  }
}

function closeAllSideMenu ()
{
	var ulroot = document.getElementById('menuroot');
	var menuuls = ulroot.getElementsByTagName('ul');
	for(var k=0; k<menuuls.length; k++) {
		menuuls[k].style.display = 'none';
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