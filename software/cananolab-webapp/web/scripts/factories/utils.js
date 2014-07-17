app.factory("sortService", function(){
	// Service unsorts hashmap. Angular auto sorts //

	return {
		getUnsorted: function(hash) {
	        if (!hash) {
	            return [];
	        }
	        return Object.keys(hash);			
		}
	}

});