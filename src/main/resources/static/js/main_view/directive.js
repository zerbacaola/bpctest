'use strict';

mainModule.directive('bpcStDecorate', function() {
    return {
        restrict : 'A',
        require : '^stTable',
        scope : {
            bpcStDecorate : '@',
            bpcStDecorateFn : '='
        },

        link : function (scope, element, attributes, stController) {
            if (angular.isDefined(scope.bpcStDecorateFn) && typeof scope.bpcStDecorateFn === 'function') {
                for (var fn in stController) {
                    if (stController.hasOwnProperty(fn) && typeof stController[fn] === 'function' && fn === scope.bpcStDecorate) {
                        const commonFn = stController[fn];
                        const controller = stController;
                        stController[fn] = function() {
                            arguments[arguments.length++] = commonFn;
                            arguments[arguments.length++] = controller;
                            return scope.bpcStDecorateFn.apply(null, arguments);
                        };
                        break;
                    }
                }
            }
        }
    };
});