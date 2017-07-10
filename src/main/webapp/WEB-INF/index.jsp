<html>
<head>
<script src="js/angular.min.js"></script>
<style>
	h2 {background-color: powderblue;}
	a {background-color: yellow;}
</style>
</head>
<body>
<h2>Upload a file with metadata</h2>
*:required<br><br>
<form name="uploadForm" ng-app="fileApp" ng-controller="fileController">
<table style="text-align: left; width: 40%;" border="0" cellpadding="2" cellspacing="2">
    <tbody>
      <tr>
        <td style="vertical-align: top;">*File Provider:</td>
        <td style="vertical-align: top;"><input type="text" name="provider" ng-model="provider"></td>
      </tr>
      <tr>
        <td style="vertical-align: top;">*File Description:</td>
        <td style="vertical-align: top;"><input type="text" name="desc" ng-model="desc"></td>
      </tr>
      <tr>
        <td style="vertical-align: top;">*File to be uploaded:</td>
        <td style="vertical-align: top;"><input type="file" name="uploadedFile" file-model="uploadedFile"></td>
      </tr>
      <tr>
        <td style="vertical-align: top;" colspan="2"><input type="button" value="Submit metadata & file" ng-click="send()"></td>
        <!-- <td style="vertical-align: top;"></td> -->
      </tr>
    </tbody>
</table>
<br>
Uploaded Date and Time will be automatically set to Today.<br><br>
<a href="/listpage">Go to Metadata List page</a>
</form>
<script>
var fileapp = angular.module('fileApp', []);

fileapp.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

fileapp.controller('fileController', ['$scope','$http',
		function($scope, $http) {
			$scope.send = function() {
				var formdata = new FormData();
				formdata.append('uploadedFile', $scope.uploadedFile);
				formdata.append('provider', $scope.provider);
				formdata.append('desc', $scope.desc);
				
				var req = {
						method: "POST",
		    			url:"/upload",
		    			data: formdata,
		    			headers: {
		    				transformRequest:angular.identity,
		    				"Content-Type": undefined
		    			}
		    	};
		    	
				$http(req)
				.success(function(res) {
					alert("uploaded successfully");
				}).error(function(res) {
					alert("failed");
				});
			};
		} ]);
</script>
</body>
</html>
