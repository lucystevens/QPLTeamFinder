var app = angular.module('directives', []);

app.directive('imports', function() {
    return {
        restrict: 'E',
        templateUrl: 'templates/imports.html'
    };
});

app.directive('postcodeValidation', function() {
  return {
    require: 'ngModel',
    link: function(scope, element, attr, mCtrl) {
      function myValidation(value) {
        if(/[A-Za-z]{1,2}[0-9]+.*/.test(value)) {
          mCtrl.$setValidity('charE', true);
        } else {
          mCtrl.$setValidity('charE', false);
        }
        return value;
      }
      mCtrl.$parsers.push(myValidation);
    }
  };
});
