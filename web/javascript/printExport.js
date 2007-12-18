function initPE() {
  var rootul = document.getElementById('printChara');
  var lis = rootul.getElementsByTagName('li');
    for (var i = 0; i < lis.length; i++) {
      var node = lis[i];

      if(node.className.toLowerCase() == 'pelist' &&
      	node.getElementsByTagName('ul').length > 0) {	
      	
        addEvent(node, 'mouseover', getMoverFor(node), false);
        addEvent(node, 'mouseout', getMoutFor(node), false);
      }
   }
   
  var rootul = document.getElementById('exportChara');
  var lis = rootul.getElementsByTagName('li');
    for (var i = 0; i < lis.length; i++) {
      var node = lis[i];
      if(node.className.toLowerCase() == 'pelist' &&
      	node.getElementsByTagName('ul').length > 0) {	
  	
        addEvent(node, 'mouseover', getMoverFor(node), false);
		addEvent(node, 'mouseout', getMoutFor(node), false);
      }
   }
}

addEvent(window, 'load', initPE, false);

function getMoverFor(node) {
  return function(e) { mover(e, node); };
}

function getMoutFor(node) {
  return function(e) { mout(e, node); };
}

function mover(e, targetElement) {
  var el = window.event ? targetElement : e ? e.currentTarget :
      null;
  if (!el) return;
  clearTimeout(el.outTimeout);
  
  for (var i = 0; i < el.childNodes.length; i++) {
    var node = el.childNodes[i];
    if (node.nodeName.toLowerCase() == 'ul') {
      node.style.display = 'block';
    }
  }
}

function mout(e, targetElement) {
  var el = window.event ? targetElement : e ? e.currentTarget :
      null;
  if (!el) return;
  el.outTimeout = setTimeout(function() { mout2(el); }, 300);
}

function mout2(el) {
  for (var i = 0; i < el.childNodes.length; i++) {
    var node = el.childNodes[i];
    if (node.nodeName.toLowerCase() == 'ul') {
      node.style.display = 'none';
    }
  }
}