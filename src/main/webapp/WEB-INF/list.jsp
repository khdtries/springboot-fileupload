<html>
<head>
<script src="js/angular.min.js"></script>
<style>
h2, h3 {background-color: powderblue;}
.tblHdr {background-color: darkGray;}
a {background-color: yellow;}
</style>
</head>
<body ng-app="listApp" ng-controller="listController">

<h2>Metadata List</h2>
<table style="text-align: left; width: 80%;" border="1" cellpadding="2" cellspacing="2">
  <tbody>
    <tr class="tblHdr">
      <td style="vertical-align: top; text-align: center;">id</td>
      <td style="vertical-align: top; text-align: center;">provider</td>
      <td style="vertical-align: top; text-align: center;">description</td>
      <td style="vertical-align: top; text-align: center;">file attached</td>
      <td style="vertical-align: top; text-align: center;">download file</td>
    </tr>
    <tr ng-repeat="metadata in allMetadata">
      <td style="vertical-align: top; text-align: center;">{{metadata.id}}</td>
      <td style="vertical-align: top; text-align: center;">{{metadata.provider}}</td>
      <td style="vertical-align: top; text-align: center;">{{metadata.desc}}</td>
      <td style="vertical-align: top; text-align: center;">{{metadata.filename}}</td>
      <!-- <td style="vertical-align: top;"><input type="button" ng-click="download(metadata.id)" value="download"></td> -->
      <td style="vertical-align: top; text-align: center;"><a ng-href="/download/{{metadata.id}}" target="_blank">download</td>
    </tr>
  </tbody>
</table>
<br><br>

<h3>[Search]</h3>
<form name="searchForm">
<table style="text-align: center; width: 20%;" border="0">
  <tbody>
    <tr class="tblHdr">
      <td>
        <select name="searchList" ng-model="searchBySelection" ng-change="updateOption(searchBySelection)"
            ng-options="oneOption as oneOption.name for oneOption in allOptions">
          <option value="">--select--</option>
        </select>
      </td>
      <td><input type="text" ng-model="optnVal"></td>
      <td><input type="button" value="search" ng-click="search()"></td>
    </tr>
  </tbody>
</table>
</form>
<br>
<a href="/">Go to File Upload page</a>
<script>
var listApp = angular.module('listApp', []);

listApp.controller("listController", function($scope, $http) {
	var gSelectionVal = "";
	
	$scope.allOptions = [{"id":"search all", "name":"search all"}
	                    ,{"id":"id", "name":"id"}
	                    ,{"id":"provider", "name":"provider name"}];
	
	$scope.getAll = function() {
		$http.get("/list")
		.success(function(data, status) {
			$scope.allMetadata = data;
		})
		.error(function() {
			alert("failed in retrieving all metadata list");
		});
	}
	
	$scope.updateOption = function(selection) {
		gSelectionVal = selection.id;
	}
	
	$scope.search = function() {
		var optionVal = $scope.optnVal;
		
		if(gSelectionVal == "") {
			alert("select id/provider name to search");
			return;
		}else if(gSelectionVal != "search all" && (optionVal == "" || optionVal == undefined)) {
			alert("no searching value");
			return;
		}
		
		var url = "";
		
		if(gSelectionVal == "search all") {
			url = "/list";
		} else {
			url = "/list/" + gSelectionVal + "/" + optionVal;	
		}
		
		$http.get(url)
		.success(function(data, status) {
			$scope.allMetadata = data;
		})
		.error(function() {
			alert("failed in searching list");
		});
	}
	
	$scope.download = function(uid) {
		var url = "/download/" + uid;
		
		$http.get(url)
		.success(function(data, status) {
			alert("download completed");
		})
		.error(function() {
			alert("failed in searching list");
		});
	}
	
	$scope.getAll();
});

</script>
</body>
</html>
