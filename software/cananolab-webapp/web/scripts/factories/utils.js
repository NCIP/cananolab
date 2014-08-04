'use strict';
var app = angular.module('angularApp');
app.factory("utilsService", function(){

	return {
		popImage: function(imgSrc, imgId) {
			// pops up image and resizes window //

			var popImg = new Image();
			popImg.src = imgSrc;
			if (imgWindow != null && imgWindow.open) {
				imgWindow.close();
				// t = null;
			}
			var topPos = 50;
			var leftPos = 50;
			var maxWidth = 800;
			var maxHeight = 800;
			if (popImg.width > 0) {
				var width = popImg.width + 20;
				var height = popImg.height + 20;
				if (width > height) {
					if (width > maxWidth) {
						var ratio = maxWidth / width;
						width = maxWidth;
						height = ratio * height;
					}
				} else {
					if (height > maxHeight) {
						var ratio = maxHeight / height;
						height = maxHeight;
						width = ratio * width;
					}
				}
				imgWindow = window.open("", "charFileWindow", "width=" + width + ",height=" + height + ",left=" + leftPos + ",top=" + topPos);
				imgWindow.document.writeln("<html><head><title>Characterization File</title></head>");
				imgWindow.document.writeln("<body onLoad=\"self.focus();\" bgcolor=\"#FFFFFF\">");
				imgWindow.document.writeln("<img width='" + (width - 20) + "' height='" + (height - 20) + "' styleId='" + imgId + "' src='" + imgSrc + "'/>");
				imgWindow.document.writeln("</body></html>");
			} else {
				imgWindow = window.open("", "charFileWindow", "left=" + leftPos + ",top=" + topPos);
				imgWindow.document.writeln("<html><head><title>Characterization File</title></head>");
				imgWindow.document.writeln("<body onLoad=\"resizePopup();\" bgcolor=\"#FFFFFF\">");
				imgWindow.document.writeln("<img id='popImage' styleId='" + imgId + "' src='" + imgSrc + "'/>");
				imgWindow.document.writeln("</body></html>");
			}
		}
		,
		getParameterFromURL: function(name) {
			    // gets parameter from url and returns the value //
			    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
			    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
			    results = regex.exec(location.search);
			    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
		},

		isHashEmpty: function(obj) {
			//checks to see if hash is empty //
			for (var key in obj)
				if(obj.hasOwnProperty(key))
			 		return false;
				return true;			
		},

		resultCount: function(params,length,pageLength) {
			// returns result count and displaying items x of y on results pages //
			if (params.page==1) {
				var html = length + ' items found, displaying ';
				html += 1 +'-'+ pageLength;
				return html;
			}
			else {
				var html = length + ' items found, displaying ';
				html += params.count*(params.page-1)+1;
				html +=' - ';
				html += ((params.page-1)*params.count+1)+pageLength-1;
				return html;
			}
		}

	}

});